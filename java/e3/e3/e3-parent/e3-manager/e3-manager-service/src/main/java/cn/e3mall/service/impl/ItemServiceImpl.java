package cn.e3mall.service.impl;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_ITEM_PRE2}")
    private String REDIS_ITEM_PRE ;
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE ;
    @Override
    public TbItem getItemById(long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有，查询数据库
        //根据主键查询
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        Criteria criteria =   example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);
        if (list !=null && list.size()>0){
            //把结果添加到缓存
            try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  list.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemListgetItemList(int page, int rows) {
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
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
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
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC" ,JsonUtils.objectToJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return tbItemDesc;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId =IDUtils.genItemId();
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
        if(i>0){
            //发送商品添加消息
            jmsTemplate.send(
                    //内部类
                    topicDestination, new MessageCreator(){
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            //itemId + ""转String
                            TextMessage textMessage = session.createTextMessage(itemId + "");
                            return textMessage;
                        }
                    }

            );
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
}
