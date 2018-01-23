package sample.springboot.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
@RefreshScope
@Component
public class SystemConfigBean {
    @Value("${name}")
    private String name;

    @Value("${value}")
    private String value;

    @Value("${version}")
    private String version;
}
