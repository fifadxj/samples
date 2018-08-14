package sample.shardingjdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigFileChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.google.common.base.Charsets;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import sample.shardingjdbc.util.algorithm.MyInlineShardingStrategyConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


//@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
//@EnableApolloConfig({"application"})
public class Application2 {
    public static void main(String[] args) throws Exception {
        //System.setProperty("env", "FAT");
        HttpServletRequest req;
        //System.setProperty("fat_meta", "http://10.156.41.18:8086");
        System.out.println(ConfigService.getConfig("share.config-client-xml").getProperty("server.0.ip", null));
        //System.out.println(ConfigService.getAppConfig().getProperty("1", null));
        //SpringApplication.run(Application2.class, args);
    }


}
