package sample.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import sample.idgen.IdGen;


@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan({"sample.springboot"})
@ImportResource({"classpath*:spring/**/*.xml"})
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@Slf4j
public class SpringBootApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(SpringBootApplication.class, args);

        IdGen idGen = context.getBean(IdGen.class);
        System.out.println(idGen.generate());
    }
}
