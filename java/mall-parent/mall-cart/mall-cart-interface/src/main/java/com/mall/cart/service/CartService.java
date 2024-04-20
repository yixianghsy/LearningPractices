package com.mall.cart.service;
import com.mall.utils.E3Result;

import java.util.List;

public interface CartService {

    E3Result addCart(Long userId, Long itemId, int num);
    E3Result updateCartNum(Long userId, Long itemId, int num);
    E3Result deleteCartItem(Long userId, Long itemId);
    E3Result clearCartList(Long userId);
}
