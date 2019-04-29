package sample.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ComponentScan({"sample.springboot"})
@ImportResource({"classpath*:spring/**/*.xml"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@Slf4j
public class JvmTuningApplication {
    public static void main(String[] args) throws Exception {
//        ClassLoader parent = Thread.currentThread().getContextClassLoader();
//        System.out.println(parent);
//        ClassLoader cl = new HotFixClassLoader(parent);
//        Thread.currentThread().setContextClassLoader(cl);

        SpringApplication.run(JvmTuningApplication.class, args);
    }
}
