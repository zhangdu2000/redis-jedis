package com.example.redisjedis.config;/**
 * Created with IntelliJ IDEA.
 * User: 张渡
 * Date: 2020/3/11
 * Time: 17:38
 * Description: No Description
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @version 1.0
 * @author： 张渡
 * @date 2020/3/11 17:38
 * Modified By： 修改人姓名(如果有其他人修改时增加这三项)
 * Modified Date: 修改日期
 *
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private  Integer maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private  Integer minIdle;
    @Value("${spring.redis.port}")
    private  Integer port;
    @Value("${redis.maxTotal}")
    private  Integer maxTotal;
    @Value("${spring.redis.timeout}")
    private  Integer timeout;
    @Value("${redis.maxWait}")
    private  Integer maxWait;
    @Value("${spring.redis.host}")
    private  String host;

    /*注入连接池对象*/
    @Bean
    public JedisPool getjedisPool(){

        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        //连接池最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        //连接池最小空闲数
        jedisPoolConfig.setMinIdle(minIdle);
        //连接池阻塞最大等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //连接池最大连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        /*拿到一个连接池*/
        JedisPool jedisPool=new JedisPool(jedisPoolConfig,host,port,timeout);
        return jedisPool;
    }
}
