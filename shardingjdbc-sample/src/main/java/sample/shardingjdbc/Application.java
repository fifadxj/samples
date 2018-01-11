package sample.shardingjdbc;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


@SpringBootApplication
@ServletComponentScan("sample.shardingjdbc")
//@ImportResource({"classpath:/spring.xml"})
//@PropertySource("classpath:/env/${env}.properties")
public class Application {
    public static void main(String[] args) throws Exception {
//        System.setProperty("env", "test");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }

    @Bean
    public DataSource shardingJdbcDataSource() throws SQLException {
        String jdbcurl = "jdbc:mysql://47.96.159.210:3306/";
        String username = "root";
        String password = "Helloworld@123";

        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        BasicDataSource dataSource1 = new BasicDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl(jdbcurl + "mycat_demo00");
        dataSource1.setUsername(username);
        dataSource1.setPassword(password);
        dataSourceMap.put("ds_0", dataSource1);

        // 配置第二个数据源
        BasicDataSource dataSource2 = new BasicDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl(jdbcurl + "mycat_demo01");
        dataSource2.setUsername(username);
        dataSource2.setPassword(password);
        dataSourceMap.put("ds_1", dataSource2);

        // 配置表规则
        TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration();
        userTableRuleConfig.setLogicTable("user");
        userTableRuleConfig.setActualDataNodes("ds_0.user_${0..1}, ds_1.user_${2..3}");
        userTableRuleConfig.setKeyGeneratorColumnName("id");
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("order");
        orderTableRuleConfig.setActualDataNodes("ds_0.order_${0..1}, ds_1.order_${2..3}");
        orderTableRuleConfig.setKeyGeneratorColumnName("id");

        // 配置分库策略
        userTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds_${(id % 4).intdiv(2)}"));
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds_${(user_id % 4).intdiv(2)}"));

        // 配置分表策略
        userTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "user_${id % 4}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "order_${user_id % 4}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultKeyGeneratorClass("sample.shardingjdbc.util.MyKeyGenerator");
        shardingRuleConfig.getTableRuleConfigs().add(userTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.getBindingTableGroups().add("user, order");

        // 获取数据源对象
        Properties properties = new Properties();
        properties.put("sql.show", "true");
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), properties);

        return dataSource;
    }
}