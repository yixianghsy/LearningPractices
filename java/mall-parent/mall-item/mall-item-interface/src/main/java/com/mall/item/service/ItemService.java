package com.mall.item.service;


import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.utils.E3Result;

public interface ItemService {
    TbItem getItemById(long itemId);
    //分页查询
    EasyUIDataGridResult getItemListgetItemList(int page, int rows);

    TbItemDesc getItemDescById(long itemId);
    //添加商品
    E3Result addItem(TbItem item, String desc);
    //删除商品
    void deleteItemList(Long[] ids);
    //下架商品
    void instockItemList(Long[] ids);
    //上架商品
    void reshelfItemList(Long[] ids);
    void  testOrderId(String orderId);
}
