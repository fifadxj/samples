package sample.smscode;

import com.google.common.primitives.Longs;
import org.apache.commons.lang.RandomStringUtils;
import redis.clients.jedis.JedisCluster;

//java
public class SmsCodeService {
    private static final String KEY_LAST_SEND_TIME = "LAST_SEND_TIME";
    private static final String KEY_LAST_GEN_CODE = "LAST_GEN_CODE";
    private static final String KEY_VALIDATE_TIME_LIST = "VALIDATE_TIME_LIST";
    private static final String KEY_LOCK = "LOCK";

    private static final int TTL_LAST_SEND_TIME = 30 * 1000; //发送短信最小时间间隔,30秒
    private static final int TTL_LAST_GEN_CODE = 5 * 60 * 1000; //生成验证码最小时间间隔,5分钟
    private static final int VALIDATION_LIMIT = 5; //x时间段内允许最多y次验证错误, y=5次
    private static final long TTL_VALIDATE_TIME_LIST = 60 * 60 * 1000L; //x时间段内允许最多y次验证错误， x=1小时

    private JedisCluster jedis;

    public void sendSmsCode(String opsType, String phone) {
        if (jedis.get(lastSendTimeKey(opsType, phone)) != null) {
            throw new RuntimeException("send sms too frequently");
        } else {
            String code = jedis.get(lastGenCodeKey(opsType, phone));
            if (code == null) {
                code = RandomStringUtils.randomNumeric(6);
                jedis.psetex(lastGenCodeKey(opsType, phone), TTL_LAST_GEN_CODE, code);
            }
            if (doSendSms(opsType, code, phone)) {
                jedis.psetex(lastSendTimeKey(opsType, phone), TTL_LAST_SEND_TIME, "");
            } else {
                throw new RuntimeException("fail to send sms");
            }
        }
    }

    /**
     validateTimeListKey()对应的value是一个list
     list超时时间是x时间段内允许最多y次验证错误中的x
     list格式: [第1次验证失败时间戳, ...第y次验证失败时间戳]
     每次验证失败时, list头出队列，list尾追加当前时间戳
     每次验证时, 如果List长度大于y, 且当前时间 - list头的时间戳  < x, 则拒绝验证请求
     */
    public void validateSmsCode(String opsType, String phone, String code) {
        boolean lock = false;
        try {
            String lockresult = jedis.set(lockKey(opsType, phone), "", "nx", "px", 1000);
            if (! "OK".equals(lockresult)) {
                throw new RuntimeException("sms lock failed");
            }
            lock = true;

            if (jedis.llen(validateTimeListKey(opsType, phone)) >= VALIDATION_LIMIT) {
                String first = jedis.lindex(validateTimeListKey(opsType, phone), 0);
                if (System.currentTimeMillis() - Longs.tryParse(first.split(":")[0]) <= TTL_VALIDATE_TIME_LIST) {
                    throw new RuntimeException("verify code failed too many times");
                }
            }
            String expectedCode = jedis.get(lastGenCodeKey(opsType, phone));
            if (expectedCode == null) {
                throw new RuntimeException("code expired");
            }
            if (!expectedCode.equals(code)) {
                jedis.rpush(validateTimeListKey(opsType, phone), System.currentTimeMillis() + ":" + code);
                jedis.ltrim(validateTimeListKey(opsType, phone), -1 * VALIDATION_LIMIT, -1);
                jedis.pexpire(validateTimeListKey(opsType, phone), TTL_VALIDATE_TIME_LIST);
                throw new RuntimeException("verify code failed: incorrect code");
            }

            //success
            jedis.del(validateTimeListKey(opsType, phone));
            jedis.del(lastGenCodeKey(opsType, phone));
        } finally {
            if (lock) {
                jedis.del(lockKey(opsType, phone));
            }
        }
    }

    private boolean doSendSms(String opsType, String code, String phone) {
        //TODO: send sms
        return true;
    }

    private String lastSendTimeKey(String opsType, String phone) {
        return KEY_LAST_SEND_TIME + ":" + opsType + ":" + phone;
    }

    private String lastGenCodeKey(String opsType, String phone) {
        return KEY_LAST_GEN_CODE + ":" + opsType + ":" + phone;
    }

    private String validateTimeListKey(String opsType, String phone) {
        return KEY_VALIDATE_TIME_LIST + ":" + opsType + ":" + phone;
    }

    private String lockKey(String opsType, String phone) {
        return KEY_LOCK + ":" + opsType + ":" + phone;
    }
}
