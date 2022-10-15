package com.e3mall.content.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.mapper.pojo.TbContent;
import com.e3mall.mapper.pojo.TbContentExample;
import com.e3mall.utils.E3Result;
import com.e3mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent tbContent) {
        logger.debug("this is debug level");
        logger.info("this is info level ");
        logger.warn("this is warn level ");
        logger.error("this is error level");
        //将内容数据插入到内容表
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        contentMapper.insert(tbContent);
        //插入到数据库
        return E3Result.ok();
    }

    /**
     * 根据内容分类id查询内容列表
     * <p>Title: getContentListByCid</p>
     * <p>Description: </p>
     * @param cid
     * @return
     * @see com.e3mall.content.service...getContentListByCid(long)
     */
    @Override
    public List<TbContent> getContentListByCid(Long cid) {
        //查询缓存
        try {
            //如果缓存中有直接响应结果
            Object o = stringRedisTemplate.opsForHash().get(CONTENT_LIST, cid + "");
            System.out.println(o);
            String json = String.valueOf(o);
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list =contentMapper.selectByExampleWithBLOBs(example);
        System.out.println(list);
        //把结果添加到缓存
        try {
            stringRedisTemplate.opsForHash().put(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;


    }
}
