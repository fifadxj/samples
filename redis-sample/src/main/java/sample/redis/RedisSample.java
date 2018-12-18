package sample.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sample.redis.single.UseRedis;

public class RedisSample {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(UseRedis.class);
        Cache cache = context.getBean(Cache.class);
        cache.put("a", "hello", 100);
        String a = cache.get("a");
        System.out.println(a);
    }
}
