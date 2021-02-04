package sample.redis.config.standalone.v2;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import sample.redis.config.Cache;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

/**
 * Created by za-daixiaojun on 2017/11/1.
 */
@Setter
@Slf4j
public class RedisCacheV2 implements Cache {
    private static final String SUCCESS_CODE = "OK";
    private static final String SCRIPT_SUCCESS = "1";
    private static final String NO_SCRIPT = "NOSCRIPT";
    private String lockScript;
    private String unlockScript;
    private String lockSHA;
    private String unlockSHA;

    @PostConstruct
    public void init() {
        URL lockFile = Resources.getResource("lock.lua");
        URL unlockFile = Resources.getResource("unlock.lua");
        try {
            lockScript = Resources.toString(lockFile, Charsets.UTF_8);
            unlockScript = Resources.toString(unlockFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            lockSHA = jedis.scriptLoad(lockScript);
            unlockSHA = jedis.scriptLoad(unlockScript);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

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
            String resp = (String) jedis.evalsha(lockSHA, Lists.newArrayList(key), Lists.newArrayList(seconds + "", value));

            return resp != null && resp.equals(SCRIPT_SUCCESS);
        } catch (JedisException e) {
            if (e.getMessage() != null && e.getMessage().startsWith(NO_SCRIPT)) {
                log.warn("lock script not loaded, loading script");
                init();
                return lock(key, value, seconds);
            }
            log.error("redis error", e);
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
            String resp = (String) jedis.evalsha(unlockSHA, Lists.newArrayList(key), Lists.newArrayList(value));

            return resp != null && resp.equals(SCRIPT_SUCCESS);
        } catch (JedisException e) {
            if (e.getMessage() != null && e.getMessage().startsWith(NO_SCRIPT)) {
                log.warn("lock script not loaded, loading script");
                init();
                return unlock(key, value);
            }
            log.error("redis error", e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
