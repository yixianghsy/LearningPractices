package com.mall.item.service;


import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;

public interface ItemService {
    TbItem getItemById(Long itemId);
    EasyUIDataGridResult getItemList(int page, int rows);
    E3Result addItem(TbItem item, String desc);
    TbItemDesc getItemDescById(Long itemId);
    //分页查询
    //以下代码自己加的
    //删除商品
    void deleteItemList(Long[] ids);
    //下架商品
    void instockItemList(Long[] ids);
    //上架商品
    void reshelfItemList(Long[] ids);

    TbItem getByPrimaryKey(Long itemId);

}
