package com.mall.item.listener;


import com.mall.item.confg.DefaultConsumerConfig;
import com.mall.item.pojo.Item;
import com.mall.mansger.service.ItemService;
import com.mall.mapper.pojo.TbItem;
import com.mall.mapper.pojo.TbItemDesc;
import com.mall.pojo.SearchItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听商品添加消息，生成对应的静态页面
 * <p>Title: HtmlGenListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
public class HtmlGenListener   extends DefaultConsumerConfig implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${HTML_GEN_PATH}")
    private String HTML_GEN_PATH;


    private static final Logger log = LoggerFactory.getLogger(HtmlGenListener.class);
    @Override
    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
        msgs.forEach(msg -> {
            byte[] bytes = msg.getBody();
            try {
                String itemId = new String(bytes, "utf-8");
                log.info("新增id:",itemId);
            //根据商品id查询商品信息，商品基本信息和商品描述。
             TbItem tbItem = itemService.getItemById(Long.valueOf(itemId));
             Item item = new Item(tbItem);
             //取商品描述
            TbItemDesc itemDesc = itemService.getItemDescById(Long.valueOf(itemId));
            //创建一个数据集，把商品数据封装
            Map data = new HashMap<>();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //加载模板对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流，指定输出的目录及文件名。
            Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
            //生成静态页面。
            template.process(data, out);
            //关闭流
            out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }

//    @Override
//    public void onMessage(Message message) {
        //创建一个模板，参考jsp
        //从消息中取商品id
//        TextMessage textMessage = (TextMessage) message;
//        try {
//            String text = textMessage.getText();
//            Long itemId = new Long(text);
//            //等待事务提交
//            Thread.sleep(1000);
//            //根据商品id查询商品信息，商品基本信息和商品描述。
//            TbItem tbItem = itemService.getItemById(itemId);
//            Item item = new Item(tbItem);
//            //取商品描述
//            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
//            //创建一个数据集，把商品数据封装
//            Map data = new HashMap<>();
//            data.put("item", item);
//            data.put("itemDesc", itemDesc);
//            //加载模板对象
//            Configuration configuration = freeMarkerConfigurer.getConfiguration();
//            Template template = configuration.getTemplate("item.ftl");
//            //创建一个输出流，指定输出的目录及文件名。
//            Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html");
//            //生成静态页面。
//            template.process(data, out);
//            //关闭流
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
