package com.mall.mansger.service;
import com.mall.utils.E3Result;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.mapper.pojo.TbItem;
import com.mall.mapper.pojo.TbItemDesc;

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
