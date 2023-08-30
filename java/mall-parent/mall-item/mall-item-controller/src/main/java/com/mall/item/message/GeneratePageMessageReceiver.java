package com.mall.item.message;


import com.mall.item.confg.DefaultConsumerConfig;
import com.mall.item.service.ItemService;
import com.mall.item.vo.Item;
import com.mall.modules.Item.TbItem;
import com.mall.modules.Item.TbItemDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接收后台添加商品消息，生成商品详情页面
 */
@Component
public class GeneratePageMessageReceiver  extends DefaultConsumerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Reference
    private ItemService itemService;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;
    @Value("${TEMPLATE_NAME}")
    private String TEMPLATE_NAME;
    @Value("${TEMPLATE_FILEPATH}")
    private String TEMPLATE_FILEPATH;
    private static final Logger log = LoggerFactory.getLogger(GeneratePageMessageReceiver.class);
    @Override
    public ConsumeConcurrentlyStatus dealBody(List<MessageExt> msgs) {
        msgs.forEach(msg -> {
            byte[] bytes = msg.getBody();
            try {
                Long itemId = Long.valueOf(new String(bytes, "utf-8"));
                log.info("新增id:",itemId);
                TbItem tbItem = itemService.getItemById(itemId);
                TbItemDesc itemDesc = itemService.getItemDescById(itemId);
                Item item = new Item(tbItem);
                // 2、构造上下文(Model)
                Context context = new Context();
                context.setVariable("item", item);
                context.setVariable("itemDesc", itemDesc);
                // 3、生成页面
                FileWriter writer = new FileWriter(TEMPLATE_FILEPATH + itemId + ".html");
                springTemplateEngine.process(TEMPLATE_NAME, context, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return null;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }
}
