package sample.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ctrip.framework.apollo.tracer.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Configuration
@Import(RefreshAutoConfiguration.class)
@ImportResource("classpath:/spring.xml")
@Slf4j
//@EnableApolloConfig(value = {"application"})
@ComponentScan
@EnableWebMvc
public class ApolloConfig {
    @Autowired
    private RefreshScope refreshScope;

    @Bean
    @org.springframework.cloud.context.config.annotation.RefreshScope
    public Properties env() {
        ApolloPropertiesFactory factory = new ApolloPropertiesFactory();
        factory.setRefreshScope(refreshScope);
        Properties props = factory.create();

        return props;
    }
}
