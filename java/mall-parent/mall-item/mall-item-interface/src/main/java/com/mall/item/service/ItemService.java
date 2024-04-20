package com.mall.item.service;
public interface ItemService {

    //分页查询
    //以下代码自己加的
    //删除商品
    void deleteItemList(Long[] ids);
    //下架商品
    void instockItemList(Long[] ids);
    //上架商品
    void reshelfItemList(Long[] ids);



}
