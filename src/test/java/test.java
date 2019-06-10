import redis.clients.jedis.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: georgexie
 * @description: TODO
 * @Date: 2019/6/4 0004 9:58
 * @Version 1.0
 */
public class test {
    private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);//设置最大线程池
        config.setMaxIdle(50);//设置最小空闲线程
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("47.101.211.129", 6379);
        jedisShardInfo1.setPassword("********");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
//        String keys = "myname";
//        String vaule = jedis.set(keys, "georgexie");
//        System.out.println(vaule);
         System.out.println(jedis.get("myname"));


    }

}
