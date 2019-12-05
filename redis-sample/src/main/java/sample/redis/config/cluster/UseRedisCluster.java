package sample.redis.config.cluster;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:/application.properties")
public class UseRedisCluster {
    @Value("${redis.timeout}")
    private Integer timeout;
    @Value("${redis.maxRedirections}")
    private Integer maxRedirections;
    @Value("${redis.cluster.address}")
    private String address;
    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;
    @Value("${redis.maxTotal}")
    private int maxTotal;
    @Value("${redis.minIdle}")
    private int minIdle;
    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Bean
    public JedisCluster jedisCluster() {
        Set<HostAndPort> haps = this.parseHostAndPort();
        GenericObjectPoolConfig genericObjectPoolConfig = createGenericObjectPoolConfig();
        JedisCluster jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);

        return jedisCluster;
    }

    @Bean
    public RedisClusterCache redisCache() {
        RedisClusterCache cache = new RedisClusterCache();
        return cache;
    }

    private GenericObjectPoolConfig createGenericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);

        return genericObjectPoolConfig;
    }

    private Set<HostAndPort> parseHostAndPort() {
        Set<HostAndPort> haps = new HashSet<HostAndPort>();

        String[] address = this.address.split(",");
        for (int i = 0; i < address.length; i++) {
            String[] ipAndPort = address[i].split(":");
            HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            haps.add(hap);
        }

        return haps;
    }
}
