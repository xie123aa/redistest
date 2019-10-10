import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.io.Serializable;
/**
 * @Author: georgexie
 * @description: ����roocketmq
 * @Date: 2019/10/8 0008 16:10
 * @Version 1.0
 */
public class Producer {
    public static void main(String[] args) throws MQClientException {
        /**
         * һ��Ӧ�ô���һ��Producer����Ӧ����ά���˶��󣬿�������Ϊȫ�ֶ�����ߵ���
         * ע�⣺ProducerGroupName��Ҫ��Ӧ������֤Ψһ(�����test-group)
         * ProducerGroup����������ͨ����Ϣʱ�����ò��󣬵��Ƿ��ͷֲ�ʽ������Ϣʱ���ȽϹؼ���
         * ��Ϊ��������ز����Group�µ�����һ��Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer("test-group");
        //�ҵ�nameserve��ַ���൱��ע������ȡ��ַ
        producer.setNamesrvAddr("47.101.211.129:9876");
        producer.setInstanceName("rmq-instance");
        /**
         * Producer������ʹ��֮ǰ����Ҫ����start��ʼ������ʼ��һ�μ��� ע�⣺�мǲ�������ÿ�η�����Ϣʱ��������start����
         */
        producer.start();
        /**
         * ������δ������һ��Producer������Է��Ͷ��topic�����tag����Ϣ��
         * ע�⣺send������ͬ�����ã�ֻҪ�����쳣�ͱ�ʶ�ɹ������Ƿ��ͳɹ�Ҳ�ɻ��ж���״̬
         * ������Ϣд��Master�ɹ�������Slave���ɹ������������Ϣ���ڳɹ������Ƕ��ڸ���Ӧ���������Ϣ�ɿ���Ҫ�󼫸ߣ�
         * ��Ҫ������������������⣬��Ϣ���ܻ���ڷ���ʧ�ܵ������ʧ��������Ӧ��������
         */
        try {
            for (int i = 0; i < 100; i++) {
                User user = new User();
                user.setLoginName("abc" + i);
                user.setPwd(String.valueOf(i));
                //top
                Message message = new Message("log-topic", "user-tag", JSON.toJSONString(user).getBytes());
                System.out.println("�����߷�����Ϣ:" + JSON.toJSONString(user));
                producer.send(message);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Ӧ���˳�ʱ��Ҫ����shutdown��������Դ���ر��������ӣ���MetaQ��������ע���Լ�
         * ע�⣺���ǽ���Ӧ����JBOSS��Tomcat���������˳����������shutdown����
         */
        producer.shutdown();
    }

    /**
     * �����û���Ϣ
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
