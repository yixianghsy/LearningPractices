package com.mall.cart.service.ipml;

import com.mall.cart.service.CartService;
import com.mall.utils.E3Result;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理服务
 * <p>Title: CartServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class CartServiceImpl implements CartService {

    @Override
    public E3Result addCart(Long userId, Long itemId, int num) {
        return null;
    }

    @Override
    public E3Result updateCartNum(Long userId, Long itemId, int num) {
        return null;
    }

    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {
        return null;
    }

    @Override
    public E3Result clearCartList(Long userId) {
        return null;
    }
}
