package sample.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
@Slf4j
public class ZipkinApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ZipkinApplication.class, args);
        log.info("*******************启动成功:{}****************", applicationContext.toString());
    }
}
