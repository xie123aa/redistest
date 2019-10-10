import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Author: georgexie
 * @description: TODO
 * @Date: 2019/10/9 0009 17:16
 * @Version 1.0
 */
public class PushConsumer {
    /**
     * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。
     * 但是实际PushConsumer内部是使用长轮询Pull方式从Broker拉消息，然后再回调用户Listener方法
     */
    public static void main(String[] args) throws MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-group");
        consumer.setNamesrvAddr("47.101.211.129:9876");
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //订阅主题（log-topic）下面的所有信息，*是通配符
        consumer.subscribe("log-topic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                /**
                 * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                 */
                System.out.printf(Thread.currentThread().getName() + "Receive New Messages :" + msgs + "%n");
                //获得的消息需要转码，不然无法显示
                MessageExt messgae=msgs.get(0);
                String body=new String(messgae.getBody());
                System.out.println(body);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
