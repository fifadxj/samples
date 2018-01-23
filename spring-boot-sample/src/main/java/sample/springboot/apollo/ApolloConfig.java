package sample.springboot.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Configuration
@Import(RefreshAutoConfiguration.class)
@EnableApolloConfig(value = {"TEST2.cat", "application"})
public class ApolloConfig {
}
