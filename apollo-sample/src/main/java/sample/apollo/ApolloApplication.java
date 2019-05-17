package sample.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableApolloConfig(value = {"application", "app.common"})
@ImportResource("classpath:/spring.xml")
@Slf4j
public class ApolloApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ApolloApplication.class, args);
        log.info("*******************启动成功:{}****************", applicationContext.toString());
    }
}
