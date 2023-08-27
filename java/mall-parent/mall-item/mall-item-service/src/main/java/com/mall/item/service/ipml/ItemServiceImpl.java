package com.mall.item.service.ipml;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.mall.item.mapper.TbItemDescMapper;
import com.mall.item.mapper.TbItemMapper;
import com.mall.item.mapper.TbOrderMapper;
import com.mall.item.service.ItemService;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemCat;
import com.mall.modules.Item.TbItemDesc;
import com.mall.modules.Item.TbItemExample;
import com.mall.modules.order.TbOrder;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.pojo.EasyUITreeNode;
import com.mall.utils.E3Result;
import com.mall.utils.IDUtils;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private TbItemDescMapper tbItemDescMapper;
    @Resource
    private TbItemMapper tbItemMapper;
    @Resource
    private DefaultMQProducer producer;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;
    @Value("${ITEM_INFO_BASE_KEY}")
    private String ITEM_INFO_BASE_KEY;
    @Value("${ITEM_INFO_DESC_KEY}")
    private String ITEM_INFO_DESC_KEY;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    @Override
    public TbItem getItemById(Long itemId) {
        // 查询缓存
        try {
            TbItem tbItem = (TbItem) redisTemplate.opsForValue().get(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY);
            if (tbItem != null) {
                System.out.println("read redis item base information...");
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 查询数据库
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        if (tbItem != null) {
            try {
                // 把数据保存到缓存
                redisTemplate.opsForValue().set(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY, tbItem);
                // 设置缓存的有效期
                redisTemplate.expire(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_BASE_KEY, ITEM_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis item base information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tbItem;
        }
        return null;
    }
    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        // 查询缓存
        try {
            TbItemDesc itemDesc = (TbItemDesc) redisTemplate.opsForValue().get(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY);
            if (itemDesc != null) {
                System.out.println("read redis item desc information...");
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 查询数据库
        TbItemDesc itemDesc = tbItemDescMapper.selectItemDescByPrimaryKey(itemId);
        if (itemDesc != null) {
            // 把数据保存到缓存
            try {
                redisTemplate.opsForValue().set(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY, itemDesc);
                redisTemplate.expire(ITEM_INFO_KEY + ":" + itemId + ":" + ITEM_INFO_DESC_KEY, ITEM_INFO_EXPIRE, TimeUnit.HOURS);
                System.out.println("write redis item desc information...");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemDesc;
        }
        return null;
    }
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        List<TbItem> list = tbItemMapper.getItemList();
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }
    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        tbItemMapper.insert(item);
        //创建一个商品描述表对应的pojo对象。
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据:无效.插入不代表事务已经提交,方法走完才提交事务
        int i = tbItemDescMapper.insert(itemDesc);
        //如果插入成功就发送
        System.out.println(longToBytes(itemId).toString());
        if(i>0){
            Message message = new Message("saveItem", "Tag1", "itemId", String.valueOf(itemId).getBytes());
            // 这里用到了这个mq的异步处理，类似ajax，可以得到发送到mq的情况，并做相应的处理
            //不过要注意的是这个是异步的
            send(message);

        }
        return E3Result.ok();
    }

    //以下代码是自己写得

    @Override
    public TbItem getByPrimaryKey(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public void deleteItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 3);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
    }
    @Override
    public void instockItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 2);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }

    }

    @Override
    public void reshelfItemList(Long[] ids) {
        TbItem tbItem= new TbItem();
        for (Long id:ids){
            tbItem.setId(id);
            //1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 1);
            //执行查询
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }

    }




    //这里异常应该是跑出去，调用的捕获
    private void send(Message message) {
        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("传输成功");
                    log.info(JSON.toJSONString(sendResult));
                }

                @Override
                public void onException(Throwable e) {
                    log.error("传输失败", e);
                }
            });
        } catch (Exception e) {
            log.error("传输失败", e);
        }
    }
    private static ByteBuffer buffer = ByteBuffer.allocate(8);
    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}

