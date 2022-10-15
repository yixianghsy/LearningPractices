package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

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
