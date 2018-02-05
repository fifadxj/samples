package sample.shardingjdbc;

import cn.sccfc.cat.springaop.CatAopService;
import cn.sccfc.cat.springboot.CatFilterConfigure;
import cn.sccfc.cat.springmvcurl.CatPageURIRewriteAspect;
import com.alibaba.druid.pool.DruidDataSource;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import sample.shardingjdbc.util.algorithm.MyInlineShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


@SpringBootApplication
@ServletComponentScan("sample.shardingjdbc")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@Import({ CatFilterConfigure.class, CatPageURIRewriteAspect.class, CatAopService.class })
@ImportResource({"classpath:/spring.xml"})
//@PropertySource("classpath:/application.properties")
public class Application {
    public static void main(String[] args) throws Exception {
//        System.setProperty("env", "test");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(shardingJdbcDataSource());

        return transactionManager;
    }

    @Bean
    public static MapperScannerConfigurer init() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("sample.shardingjdbc");

        return configurer;
    }

    @Bean
    public DataSource physicalDataSource1() throws SQLException {
        String jdbcurl = "jdbc:mysql://47.96.159.210:3306/";
        String username = "root";
        String password = "Helloworld@123";

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(jdbcurl + "mycat_demo00");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setFilters("mergeStat,config,cat");

        return dataSource;
    }

    @Bean
    public DataSource physicalDataSource2() throws SQLException {
        String jdbcurl = "jdbc:mysql://47.96.159.210:3306/";
        String username = "root";
        String password = "Helloworld@123";

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(jdbcurl + "mycat_demo01");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setFilters("mergeStat,config,cat");

        return dataSource;
    }

    @Bean
    public DataSource shardingJdbcDataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        DataSource ds1 = physicalDataSource1();
        dataSourceMap.put("ds_0", ds1);

        // 配置第二个数据源
        DataSource ds2 = physicalDataSource2();
        dataSourceMap.put("ds_1", ds2);

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
        userTableRuleConfig.setDatabaseShardingStrategyConfig(new MyInlineShardingStrategyConfiguration("id", "ds_${(id % 4).intdiv(2)}"));
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new MyInlineShardingStrategyConfiguration("user_id", "ds_${(user_id % 4).intdiv(2)}"));

        // 配置分表策略
        userTableRuleConfig.setTableShardingStrategyConfig(new MyInlineShardingStrategyConfiguration("id", "user_${id % 4}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new MyInlineShardingStrategyConfiguration("user_id", "order_${user_id % 4}"));

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