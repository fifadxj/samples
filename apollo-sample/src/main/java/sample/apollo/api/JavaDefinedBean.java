package sample.apollo.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
@Component
public class JavaDefinedBean {
    @Value("${name}")
    private String name;

    @Value("${value}")
    private String value;

    @Value("${version}")
    private String version;
}
