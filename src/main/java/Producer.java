import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.io.Serializable;
/**
 * @Author: georgexie
 * @description: 测试roocketmq
 * @Date: 2019/10/8 0008 16:10
 * @Version 1.0
 */
public class Producer {
    public static void main(String[] args) throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例
         * 注意：ProducerGroupName需要由应用来保证唯一(下面的test-group)
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer("test-group");
        //找到nameserve地址，相当于注册中心取地址
        producer.setNamesrvAddr("47.101.211.129:9876");
        producer.setInstanceName("rmq-instance");
        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        /**
         * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
         * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态
         * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，
         * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
         */
        try {
            for (int i = 0; i < 100; i++) {
                User user = new User();
                user.setLoginName("abc" + i);
                user.setPwd(String.valueOf(i));
                //top
                Message message = new Message("log-topic", "user-tag", JSON.toJSONString(user).getBytes());
                System.out.println("生产者发送消息:" + JSON.toJSONString(user));
                producer.send(message);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        producer.shutdown();
    }

    /**
     * 发送用户消息
     */
    static class User implements Serializable {
        private String loginName;
        private String pwd;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
