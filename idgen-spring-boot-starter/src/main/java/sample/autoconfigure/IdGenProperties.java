package sample.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("idgen")
@Getter
@Setter
public class IdGenProperties {
    private String type;
}
