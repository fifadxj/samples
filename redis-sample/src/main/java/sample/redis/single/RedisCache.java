package sample.redis.single;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import sample.redis.Cache;

import java.util.Collections;

/**
 * Created by za-daixiaojun on 2017/11/1.
 */
@Setter
@Slf4j
public class RedisCache implements Cache {
    private static final String SUCCESS_CODE = "OK";
    
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Boolean put(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String resp = jedis.set(key, value);

            return resp.equals(SUCCESS_CODE);
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Boolean put(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String resp = jedis.setex(key, seconds, value);
            return resp.equals(SUCCESS_CODE);
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String resp = jedis.get(key);
            return resp;
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Boolean del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long resp = jedis.del(key);
            return resp == 1;
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    @Override
    public Boolean lock(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String resp = jedis.set(key, value, "NX", "EX", seconds);

            return resp != null && resp.equals(SUCCESS_CODE);
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    @Override
    public Boolean unlock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            return RELEASE_SUCCESS.equals(result);
        } catch (JedisConnectionException e) {
            log.error("redis connection error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
