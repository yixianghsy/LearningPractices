package net.xdclass.xdclassmq.controller;

import net.xdclass.xdclassmq.jms.JmsConfig;
import net.xdclass.xdclassmq.jms.PayProducer;
import net.xdclass.xdclassmq.jms.TransactionProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class PayController {

    @Autowired
    private TransactionProducer transactionMQProducer;


    @RequestMapping("/api/v1/pay_cb")
    public Object callback( String tag, String otherParam ) throws Exception {

            Message message = new Message(JmsConfig.TOPIC, tag, tag+"_key",tag.getBytes());



            SendResult sendResult =  transactionMQProducer.getProducer().
                    sendMessageInTransaction(message, otherParam);


            System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());

            return new HashMap<>();
    }


}
