package com.mall.content.service.ipml;

import com.mall.content.mapper.TbContentCategoryMapper;
import com.mall.content.service.ContentCategoryService;
import com.mall.modules.content.TbContentCategory;
import com.mall.modules.content.TbContentCategoryExample;
import com.mall.pojo.EasyUITreeNode;
import com.mall.utils.E3Result;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
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
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        // 1、取查询参数id，parentId
        // 2、根据parentId查询tb_content_category，查询子节点列表。
        // 3、得到List<TbContentCategory>
        List<TbContentCategory> list = contentCategoryMapper.selectTbContentCatsByParentId(parentId);
        // 4、把列表转换成List<EasyUITreeNode>ub
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getIsParent()?"closed":"open");
            // 添加到列表
            resultList.add(node);
        }
        return resultList;

    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        // 1、接收两个参数：parentId、name
        // 2、向tb_content_category表中插入数据。
        // a)创建一个TbContentCategory对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        // b)补全TbContentCategory对象的属性
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        tbContentCategory.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        // c)向tb_content_category表中插入数据
        contentCategoryMapper.insertCategory(tbContentCategory);
        // 3、判断父节点的isparent是否为true，不是true需要改为true。
        TbContentCategory parentNode = contentCategoryMapper.selectTbContentCatById(parentId);
        if (!parentNode.getIsParent()) {
            parentNode.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateContentCategoryById(parentNode);
        }
        // 4、需要主键返回。
        // 5、返回E3Result，其中包装TbContentCategory对象
        return E3Result.ok(tbContentCategory);

    }
}
