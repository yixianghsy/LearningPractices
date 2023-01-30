package com.xdclass.shop.util;

import com.xdclass.shop.model.Product;

/**
 * 购物车关联项
 *
 * @author Daniel
 * @version 2020/6/16
 */
public class CartItem {
    private Product product;//商品
    private Integer total;//数量

    public CartItem(Product product, Integer total) {
        this.product = product;
        this.total = total;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
