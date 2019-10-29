package sample.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import sample.mybatis.mapper.UserMapper;
import sample.mybatis.vo.User;


@SpringBootApplication
@ComponentScan({"sample.mybatis"})
@ImportResource({"classpath*:spring/**/*.xml"})
@EnableAutoConfiguration
@Slf4j
@MapperScan("sample.mybatis.mapper")
public class MybatisApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(MybatisApplication.class, args);
        System.out.println("===");
        UserMapper userMapper = context.getBean(UserMapper.class);
        User user = new User();
        user.setName("terry");
        user.setPwd("terry");
        userMapper.insertSelective(user);
        System.out.println();
    }
}
