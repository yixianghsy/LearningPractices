package net.xdclass.xdclassmq.jms;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class TransactionProducer {

    private String producerGroup = "trac_producer_group";

    //事务监听器
    private TransactionListener transactionListener = new TransactionListenerImpl();

    private TransactionMQProducer producer = null;


    //一般自定义线程池的时候，需要给线程加个名称

    private ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        }

    });

    public TransactionProducer(){

        producer = new TransactionMQProducer(producerGroup);

        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);

        producer.setTransactionListener(transactionListener);

        producer.setExecutorService(executorService);

        //指定NameServer地址，多个地址以 ; 隔开

        start();
    }

    public TransactionMQProducer getProducer(){
        return this.producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown(){
        this.producer.shutdown();
    }


}





class TransactionListenerImpl implements  TransactionListener{

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        System.out.println("====executeLocalTransaction=======");
        String body = new String(msg.getBody());
        String key = msg.getKeys();
        String transactionId = msg.getTransactionId();
        System.out.println("transactionId="+transactionId+", key="+key+", body="+body);
        // 执行本地事务begin TODO


        // 执行本地事务end TODO


        int status = Integer.parseInt(arg.toString());

        //二次确认消息，然后消费者可以消费
        if(status == 1){
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        //回滚消息，broker端会删除半消息
        if(status == 2){
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        //broker端会进行回查消息，再或者什么都不响应
        if(status == 3){
            return LocalTransactionState.UNKNOW;
        }



        return null;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {

        System.out.println("====checkLocalTransaction=======");
        String body = new String(msg.getBody());
        String key = msg.getKeys();
        String transactionId = msg.getTransactionId();
        System.out.println("transactionId="+transactionId+", key="+key+", body="+body);

        //要么commit 要么rollback

        //可以根据key去检查本地事务消息是否完成


        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
