package com.e3mall.cart.service;

import com.e3mall.mapper.pojo.TbItem;
import com.e3mall.utils.E3Result;

import java.util.List;

public interface CartService {

	E3Result addCart(long userId, long itemId, int num);
	E3Result mergeCart(long userId, List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	E3Result updateCartNum(long userId, long itemId, int num);
	E3Result deleteCartItem(long userId, long itemId);
    E3Result clearCartItem(long userId);
}
