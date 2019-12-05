package sample.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "sample.mybatis.mapper2", sqlSessionFactoryRef = "sqlSessionFactory2")
public class DBConfig {
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("Terry@2019");
        dataSource.setUrl("jdbc:mysql://47.96.159.210/test");

        return dataSource;
    }

    @Bean
    public DataSource dataSource2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("Terry@2019");
        dataSource.setUrl("jdbc:mysql://47.96.159.210/new_test");

        return dataSource;
    }
}
