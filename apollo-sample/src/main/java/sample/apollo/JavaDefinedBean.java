package sample.apollo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
@RefreshScope
@Component
public class JavaDefinedBean {
    //@Value("${name}")
    @Value("#{env['name']}")
    private String name;

    //@Value("${value}")
    @Value("#{env['value']}")
    private String value;

    //@Value("${version}")
    @Value("#{env['version']}")
    private String version;
}
