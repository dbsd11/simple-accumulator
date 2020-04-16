package group.bison.test.accumulator.tools;

import group.bison.test.accumulator.common.AccumulatorConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Created by diaobisong on 2020/3/29.
 */
@Component
public class PersistTool implements InitializingBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static PersistTool instance;

    public static String getVal(String key, String defaultVal) {
        String redisKey = getRedisKey(key);
        String val = instance.redisTemplate.opsForValue().get(redisKey);
        return val == null ? defaultVal : val;
    }

    public static Object getObj(String key, Object defaultObj) {
        Object result = defaultObj;

        ObjectInputStream ois = null;
        try {
            String val = getVal(key, null);
            if (val != null) {
                ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(val.getBytes()))) {
                };
                result = ois.readObject();
            }
        } catch (Exception e) {
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    public static void setVal(String key, String val) {
        instance.redisTemplate.opsForValue().set(getRedisKey(key), val, AccumulatorConstants.REDIS_KEY_EXPIRE, TimeUnit.SECONDS);
    }

    public static void setValByIncr(String key, String val, String incr) {
        if (StringUtils.isEmpty(incr)) {
            setVal(key, val);
        } else if (incr.contains(".")) {
            instance.redisTemplate.opsForValue().increment(getRedisKey(key), Double.valueOf(incr));
        } else {
            instance.redisTemplate.opsForValue().increment(getRedisKey(key), Long.valueOf(incr));
        }
    }

    public static void setObj(String key, Object val) {
        ObjectOutputStream objOutStream = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            objOutStream = new ObjectOutputStream(baos) {
            };
            objOutStream.writeObject(val);
            String valStr = Base64.getEncoder().encodeToString(baos.toByteArray());
            setVal(key, valStr);
        } catch (Exception e) {
        } finally {
            try {
                objOutStream.close();
            } catch (IOException e) {
            }
        }
    }

    static String getRedisKey(String key) {
        return String.join("", AccumulatorConstants.REDIS_KEY_PREFIX, "#", key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
