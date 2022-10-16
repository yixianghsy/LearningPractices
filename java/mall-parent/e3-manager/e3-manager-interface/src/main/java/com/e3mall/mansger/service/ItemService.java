package com.e3mall.mansger.service;


import com.e3mall.mapper.pojo.TbItem;
import com.e3mall.mapper.pojo.TbItemDesc;
import com.e3mall.pojo.EasyUIDataGridResult;
import com.e3mall.utils.E3Result;
public interface ItemService {

    TbItem getItemById(long itemId);
    //分页查询
    EasyUIDataGridResult getItemListgetItemList(int page  , int rows);

    TbItemDesc getItemDescById(long itemId);
    //添加商品
    E3Result addItem(TbItem item, String desc);
    //删除商品
    void deleteItemList(Long[] ids);
    //下架商品
    void instockItemList(Long[]ids);
    //上架商品
    void reshelfItemList(Long[] ids);
}