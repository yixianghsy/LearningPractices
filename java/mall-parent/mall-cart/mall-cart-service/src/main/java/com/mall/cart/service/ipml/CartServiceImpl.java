package com.mall.cart.service.ipml;

import com.mall.cart.service.CartService;
import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.utils.E3Result;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Reference
    private ItemService itemService;

    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //向redis中添加购物车。
        //数据类型是hash key：用户id field：商品id value：商品信息
        //判断商品是否存在
        Boolean hexists = stringRedisTemplate.opsForHash().hasKey(REDIS_CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            String json = (String) stringRedisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");
            //把json转换成TbItem
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(item.getNum() + num);
            //写回redis
            //添加到购物车列表
            stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
            return E3Result.ok();
        }else {
            //redis 没有情况下调用查询后在从redis 查询
            itemService.getItemDescById(itemId);
            this.addCart(userId, itemId, num);
        }
        //如果不存在，根据商品id取商品信息
//        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        //设置购物车数据量
//        item.setNum(num);
//        //取一张图片
//        String image = item.getImage();
//        if (StringUtils.isNotBlank(image)) {
//            item.setImage(image.split(",")[0]);
//        }
        //添加到购物车列表
//        stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
//        return E3Result.ok();
        return  null;
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemList) {
        //遍历商品列表
        //把列表添加到购物车。
        //判断购物车中是否有此商品
        //如果有，数量相加
        //如果没有添加新的商品
        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        //返回成功
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询购车列表
        List<Object> jsonList = stringRedisTemplate.opsForHash().values(REDIS_CART_PRE + ":" + userId);
//        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (Object string : jsonList) {
            //创建一个TbItem对象
            TbItem item = JsonUtils.jsonToPojo((String) string, TbItem.class);
//            //添加到列表
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
//        //从redis中取商品信息
        String json = (String) stringRedisTemplate.opsForHash().get(REDIS_CART_PRE + ":" + userId, itemId + "");

        //更新商品数量
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        //写入redis

        stringRedisTemplate.opsForHash().put(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        // 删除购物车商品
        stringRedisTemplate.opsForHash().delete(REDIS_CART_PRE + ":" + userId, itemId + "");
//        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clearCartItem(long userId) {
        //删除购物车信息
        stringRedisTemplate.delete(REDIS_CART_PRE + ":" + userId);
//        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }


}
