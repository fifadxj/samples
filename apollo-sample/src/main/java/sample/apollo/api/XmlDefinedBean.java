package sample.apollo.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Getter
@Setter
public class XmlDefinedBean {
    private String name;

    private String value;

    private String version;
}
