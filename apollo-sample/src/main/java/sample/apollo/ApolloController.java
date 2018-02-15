package sample.apollo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class ApolloController {
    @Autowired
    private JavaDefinedBean javaDefinedBean;
    @Autowired
    private XmlDefinedBean xmlDefinedBean;

    public ApolloController() {
    }

    @RequestMapping(value = "/apollo/getConfigs", method = { RequestMethod.GET })
    @ResponseBody
    ApolloResp getConfigs() throws Exception {
        String name = javaDefinedBean.getName();
        String value = javaDefinedBean.getValue();
        String version = xmlDefinedBean.getVersion();
        ApolloResp resp = new ApolloResp();
        resp.setValue(name + " " + value + " " + version + " ");

        return resp;
    }
}

