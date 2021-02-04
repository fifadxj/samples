package sample.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import sample.redis.config.standalone.v2.RedisCacheV2;
import sample.redis.config.standalone.v2.UseRedisV2;

@SpringBootApplication
@Slf4j
@Import(UseRedisV2.class)
public class RedisApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(RedisApplication.class, args);
    }

}
