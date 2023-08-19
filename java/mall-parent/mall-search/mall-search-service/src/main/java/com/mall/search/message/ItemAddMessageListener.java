//package com.mall.search.message;
//
//import com.mall.pojo.SearchItem;
//import com.mall.search.Utit.EsUtil;
//import com.mall.search.config.DefaultConsumerConfig;
//import com.mall.search.mapper.ItemMapper;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
///**
// * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
// * <p>Title: ItemAddMessageListener</p>
// * <p>Description: </p>
// * <p>Company: www.itcast.cn</p>
// * @version 1.0
// */
//@Service
//public class ItemAddMessageListener   extends DefaultConsumerConfig implements ApplicationListener<ContextRefreshedEvent> {
//
//    private static final Logger log = LoggerFactory.getLogger(ItemAddMessageListener.class);
//
//    @Autowired
//    private ItemMapper itemMapper;
//
//    @Autowired
//    private EsUtil esUtil;
//
//    @Override
//    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
//        msgs.forEach(msg -> {
//            byte[] bytes = msg.getBody();
//            try {
//                String itemId = new String(bytes, "utf-8");
//                log.info("新增id:",itemId);
//                //根据商品id查询商品信息
//                SearchItem searchItem = itemMapper.getItemById(Long.valueOf(itemId));
//                XContentBuilder builder = XContentFactory.jsonBuilder()
//                        .startObject()
//                        .field("id",searchItem.getId())
//                        .field("item_title",searchItem.getTitle())
//                        .field("item_sell_point",searchItem.getSell_point())
//                        .field("item_price",searchItem.getPrice())
//                        .field("item_image",searchItem.getImage())
//                        .field("item_category_name",searchItem.getCategory_name())
//                        .endObject();
//                esUtil.addData(builder,"item_title",searchItem.getId());
//            } catch (UnsupportedEncodingException e) {
//                log.error("body转字符串失败");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        //返回消费成功信息
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        try {
//            super.consumer("saveItem", "Tag1");
//        } catch (MQClientException e) {
//            log.error("消费者监听器启动失败", e);
//        }
//    }
//
//}
