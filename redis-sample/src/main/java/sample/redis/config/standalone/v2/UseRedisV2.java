package sample.redis.config.standalone.v2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import sample.redis.config.standalone.RedisCache;

@PropertySource("classpath:/application.properties")
public class UseRedisV2 {
    @Value("${redis.timeout}")
    private Integer timeout;
    @Value("${redis.address}")
    private String address;
    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;
    @Value("${redis.maxTotal}")
    private int maxTotal;
    @Value("${redis.minIdle}")
    private int minIdle;
    @Value("${redis.maxIdle}")
    private int maxIdle;

    private String password;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        String[] ipport = address.split(":");
        return new JedisPool(poolConfig, ipport[0], new Integer(ipport[1]), timeout, password);
    }

    @Bean
    public RedisCacheV2 redisCache() {
        RedisCacheV2 cache = new RedisCacheV2();
        return cache;
    }
}
