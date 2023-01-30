package net.xdclass.xdclassmq.jms;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class PayConsumer {


    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "pay_consumer_group";

    public  PayConsumer() throws MQClientException {

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //默认是集群方式，可以更改为广播，但是广播方式不支持重试
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(JmsConfig.TOPIC,"*");
        consumer.registerMessageListener( new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                String key = msg.getKeys();

                try {

                System.out.printf("%s 2 Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

            } catch (Exception e) {
                System.out.println("消费异常");
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            }
        });

        consumer.start();
        System.out.println("consumer start ...");
    }

}
