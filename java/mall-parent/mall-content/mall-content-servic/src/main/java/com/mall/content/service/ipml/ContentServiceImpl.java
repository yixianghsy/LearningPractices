package com.mall.content.service.ipml;

import com.mall.content.mapper.TbContentMapper;
import com.mall.content.service.ContentService;
import com.mall.modules.content.TbContent;
import com.mall.modules.content.TbContentExample;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 内容管理Service
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class ContentServiceImpl  implements ContentService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private TbContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;


    @Override
    public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);

        List<TbContent> tbContents = new ArrayList<>();
        if (categoryId == 0L) {
            tbContents = contentMapper.getAllContentList();
        } else {
            tbContents = contentMapper.getContentListByCategoryId(categoryId);
        }

        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setRows(tbContents);
        easyUIDataGridResult.setTotal(pageInfo.getTotal());
        return easyUIDataGridResult;
    }

    @Override
    public List<TbContent> getContentList(Long cid) {
        // 查询缓存
        try {
            List<TbContent> contents = (List<TbContent>) redisTemplate.opsForHash().get(CONTENT_KEY, cid.toString());
            System.out.println("read redis catch data...");
            if (!contents.isEmpty() && contents != null) {
                return contents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 根据cid查询内容列表
        List<TbContent> list = contentMapper.getContentListByCategoryId(cid);
        // 向缓存中添加数据
        try {
            redisTemplate.opsForHash().put(CONTENT_KEY, cid.toString(), list);
            System.out.println("write redis catch data...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public E3Result addContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insertContent(content);
        //缓存同步
        redisTemplate.opsForHash().delete(CONTENT_KEY, content.getCategoryId().toString());
        return E3Result.ok();
    }
}
