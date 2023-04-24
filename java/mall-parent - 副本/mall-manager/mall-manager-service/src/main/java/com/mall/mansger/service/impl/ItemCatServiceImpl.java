package com.mall.mansger.service.impl;

import com.mall.mansger.service.ItemCatService;
import com.mall.mapper.TbItemCatMapper;
import com.mall.mapper.pojo.TbItemCat;
import com.mall.mapper.pojo.TbItemCatExample;
import com.mall.pojo.EasyUITreeNode;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Override
    public List<EasyUITreeNode> getItemCatlist(long parentId) {
        //根据parentId查询子节点列表
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list= null;
        //执行查询
        try {
            list = itemCatMapper.selectByExample(example);
        }catch (Exception e){
            System.out.println("数据库查询异常");
        }

        //创建返回结果List
        List<EasyUITreeNode> resultList = new ArrayList<>();
        //把列表转换成EasyUITreeNode列表
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            //设置属性
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加到结果列表
            resultList.add(node);
        }
        //返回结果
        return resultList;
    }


    @Override
    public String getListDome(int i) {
        return "这是测试数据";
    }
}