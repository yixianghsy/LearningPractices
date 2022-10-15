package com.e3mall.content.service.impl;

import com.e3mall.content.service.ContentCategoryService;

import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.mapper.pojo.TbContentCategory;
import com.e3mall.mapper.pojo.TbContentCategoryExample;
import com.e3mall.pojo.EasyUITreeNode;
import com.e3mall.utils.E3Result;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl  implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        // 根据parentid查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> catList = tbContentCategoryMapper.selectByExample(example);
        //转换成EasyUITreeNode的列表
        List<EasyUITreeNode> nodeList = new ArrayList<>();
        for (TbContentCategory tbContentCategory:catList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到列表
            nodeList.add(node);
        }
        return nodeList;

    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //创建一个tb_content_category表对应的pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //设置pojo的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //1(正常),2(删除)
        contentCategory.setStatus(1);
        //默认排序就是1
        contentCategory.setSortOrder(1);
        //新添加的节点一定是叶子节点
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据库
        tbContentCategoryMapper.insert(contentCategory);
        //判断父节点的isparent属性。如果不是true改为true
        //根据parentid查询父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            //更新到数数据库
            tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        //返回结果，返回E3Result，包含pojo
        return E3Result.ok(contentCategory);

    }
}
