package sample.redis.cluster;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;
import sample.redis.Cache;

import java.util.Collections;

/**
 * Created by za-daixiaojun on 2017/11/1.
 */
@Setter
public class RedisClusterCache implements Cache {
    private static final String SUCCESS_CODE = "OK";
    
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public Boolean put(String key, String value) {
        String resp = jedisCluster.set(key, value);

        return resp.equals(SUCCESS_CODE);
    }

    @Override
    public Boolean put(String key, String value, int seconds) {
        String resp = jedisCluster.setex(key, seconds, value);

        return resp.equals(SUCCESS_CODE);
    }

    @Override
    public String get(String key) {
        String resp = jedisCluster.get(key);
        return resp;
    }

    @Override
    public Boolean del(String key) {
        long resp = jedisCluster.del(key);
        return resp == 1;
    }
    
    @Override
    public Boolean lock(String key, String value, int seconds) {
        String resp = jedisCluster.set(key, value, "NX", "EX", seconds);

        return resp != null && resp.equals(SUCCESS_CODE);
    }
    
    @Override
    public Boolean unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedisCluster.eval(script, Collections.singletonList(key), Collections.singletonList(value));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
