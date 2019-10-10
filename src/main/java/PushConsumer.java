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
     * ��ǰ������PushConsumer�÷���ʹ�÷�ʽ���û��о�����Ϣ��RocketMQ�������Ƶ���Ӧ�ÿͻ��ˡ�
     * ����ʵ��PushConsumer�ڲ���ʹ�ó���ѯPull��ʽ��Broker����Ϣ��Ȼ���ٻص��û�Listener����
     */
    public static void main(String[] args) throws MQClientException {
        /**
         * һ��Ӧ�ô���һ��Consumer����Ӧ����ά���˶��󣬿�������Ϊȫ�ֶ�����ߵ���
         * ע�⣺ConsumerGroupName��Ҫ��Ӧ������֤Ψһ
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-group");
        consumer.setNamesrvAddr("47.101.211.129:9876");
        /**
         * ����Consumer��һ�������ǴӶ���ͷ����ʼ���ѻ��Ƕ���β����ʼ���� ����ǵ�һ����������ô�����ϴ����ѵ�λ�ü�������
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //�������⣨log-topic�������������Ϣ��*��ͨ���
        consumer.subscribe("log-topic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                /**
                 * Ĭ��msgs��ֻ��һ����Ϣ������ͨ������consumeMessageBatchMaxSize����������������Ϣ
                 */
                System.out.printf(Thread.currentThread().getName() + "Receive New Messages :" + msgs + "%n");
                //��õ���Ϣ��Ҫת�룬��Ȼ�޷���ʾ
                MessageExt messgae=msgs.get(0);
                String body=new String(messgae.getBody());
                System.out.println(body);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
