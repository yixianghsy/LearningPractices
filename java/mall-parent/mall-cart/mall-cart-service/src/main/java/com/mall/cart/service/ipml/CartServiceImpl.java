package com.mall.cart.service.ipml;

import com.mall.cart.service.CartService;
import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Reference
    private ItemService itemService;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Override
    public E3Result addCart(Long userId, Long itemId, int num) {
        //向redis中添加购物车。
        //数据类型是hash key：用户id field：商品id value：商品信息
        //判断商品是否存在
        Boolean hexists = redisTemplate.opsForHash().hasKey(REDIS_CART_PRE + ":" + userId, itemId+"");
        //如果存在数量相加
        if (hexists) {
            // 商品存在，数量相加
            TbItem tbItem = (TbItem) redisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId+"");
            tbItem.setNum(tbItem.getNum() + num);
            redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId+"", tbItem);
            return E3Result.ok();
        }else {
            // 商品不存在，查询数据库添加商品
            TbItem item = itemService.getByPrimaryKey(itemId);
            item.setNum(num);
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                item.setImage(image.split(",")[0]);
            }
            redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId+"", item);
            return E3Result.ok();
        }

    }

    @Override
    public E3Result mergeCart(Long userId, List<TbItem> cookieItemList) {
        for (TbItem tbItem : cookieItemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        List<Object> results = redisTemplate.opsForHash().values(REDIS_CART_PRE + ":" + userId);
        List<TbItem> tbItems = new ArrayList<>();
        for (Object result : results) {
            tbItems.add((TbItem) result);
        }
        return tbItems;
    }

    @Override
    public E3Result updateCartNum(Long userId, Long itemId, int num) {
        TbItem tbItem = (TbItem) redisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId+"");
        tbItem.setNum(tbItem.getNum() + num);
        redisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId+"", tbItem);
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {
        redisTemplate.opsForHash().delete(REDIS_CART_PRE + ":" + userId, itemId+"");
        return E3Result.ok();
    }

    /**
     * 清除购物车
     * @param userId
     * @return
     */
    @Override
    public E3Result clearCartList(Long userId) {
        redisTemplate.delete(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }
}
