package com.mall.cart.controller;
import com.mall.cart.service.CartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 购物车处理Controller
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Controller
@RequestMapping("/cart")
public class CartController  {
    @Reference
    CartService cartService;

}

