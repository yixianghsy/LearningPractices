package com.mall.item.service.ipml;

import com.mall.item.mapper.TbItemCatMapper;
import com.mall.item.service.ItemCatService;
import com.mall.modules.Item.TbItemCat;
import com.mall.modules.Item.TbItemCatExample;
import com.mall.pojo.EasyUITreeNode;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.Service;
import javax.annotation.Resource;
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Resource
    private TbItemCatMapper itemCatMapper;


    @Override
    public List<EasyUITreeNode> getCatList(Long parentId) {
        // 1、根据parentId查询节点列表
        //设置查询条件
        List<TbItemCat> list = itemCatMapper.getItemCatByParentId(parentId);
        // 2、转换成EasyUITreeNode列表。
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            //添加到列表
            resultList.add(node);
        }
        // 3、返回。
        return resultList;
    }
}