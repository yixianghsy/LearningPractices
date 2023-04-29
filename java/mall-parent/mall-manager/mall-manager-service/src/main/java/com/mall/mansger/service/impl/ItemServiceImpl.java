package com.mall.mansger.service.impl;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.mapper.TbItemDescMapper;
import com.mall.mapper.TbItemMapper;
import com.mall.mapper.pojo.TbItemExample;
import com.mall.pojo.EasyUIDataGridResult;
import com.mall.mansger.service.ItemService;
import com.mall.mapper.pojo.TbItem;
import com.mall.mapper.pojo.TbItemDesc;
import com.mall.utils.E3Result;
import com.mall.utils.IDUtils;
import com.mall.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.rocketmq.common.message.Message;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private DefaultMQProducer producer;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Value("${REDIS_ITEM_PRE2}")
    private String REDIS_ITEM_PRE ;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE ;
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);


    @Override
    public TbItem getItemById(long itemId) {
        /**
         * 在UserService的实现类中（业务层）进行缓存测试，注入RedisTemplate或StringRedisTemplate都可以。
         */

        //查询缓存
        try {
            String json = stringRedisTemplate.opsForValue().get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)){
                TbItem tbItem  = JsonUtils.jsonToPojo(json,TbItem.class);
                return  tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有，查询数据库
        //根据主键查询
//        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria =   example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);
        if (list !=null && list.size()>0){
            try {
                //查询结果写入缓存
                //插入缓存及过期时间
                stringRedisTemplate.opsForValue().set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)),ITEM_CACHE_EXPIRE , TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    @Override
    public EasyUIDataGridResult getItemListgetItemList(int page, int rows) {
        System.out.println("ItemServiceImpl.getItemListgetItemList");
        System.out.println("page = " + page + ", rows = " + rows);
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }
    @Override
    public TbItemDesc getItemDescById(long itemId) {
        try {
            String json = stringRedisTemplate.opsForValue().get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //把结果添加到缓存
        try {
            stringRedisTemplate.opsForValue().set(REDIS_ITEM_PRE + ":" + itemId + ":DESC" ,JsonUtils.objectToJson(tbItemDesc),ITEM_CACHE_EXPIRE , TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

        return tbItemDesc;
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


