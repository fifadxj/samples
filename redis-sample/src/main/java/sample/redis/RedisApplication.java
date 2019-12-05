package sample.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class RedisApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(RedisApplication.class, args);

    }

}
