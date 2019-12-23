package sample.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sample.mybatis.mapper.UserMapper;
import sample.mybatis.mapper2.UserMapper2;
import sample.mybatis.vo.User;

import javax.annotation.Resource;


@SpringBootApplication
@ComponentScan({"sample.mybatis"})
@ImportResource({"classpath*:spring/**/*.xml"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(PropConfig.class)
@Slf4j
@MapperScan(value = "sample.mybatis.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(MybatisApplication.class, args);
    }
}
