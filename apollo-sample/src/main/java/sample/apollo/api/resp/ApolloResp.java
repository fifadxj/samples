package sample.apollo.api.resp;

import lombok.Getter;
import lombok.Setter;
import sample.apollo.api.JavaDefinedBean;
import sample.apollo.api.XmlDefinedBean;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
public class ApolloResp {
    private JavaDefinedBean javaDefinedBean;
    private XmlDefinedBean xmlDefinedBean;
}
