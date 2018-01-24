package sample.apollo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Getter
@Setter
public class XmlDefinedBean {
    public XmlDefinedBean() {
        System.out.println();
    }
    private String version;
    private String version2;
}
