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
    //@Autowired
    private SystemConfigProps systemConfigProps;

    @Autowired
    private JavaDefinedBean javaDefinedBean;

    public ApolloController() {
        System.out.println();
    }

    @RequestMapping(value = "/apollo/getAppConfig", method = { RequestMethod.GET })
    @ResponseBody
    ApolloResp getAppConfig(@RequestParam String key) throws Exception {
        String value = systemConfigProps.getAppValue(key);
        ApolloResp resp = new ApolloResp();
        resp.setValue(value);

        return resp;
    }

    @RequestMapping(value = "/apollo/getConfigs", method = { RequestMethod.GET })
    @ResponseBody
    ApolloResp getConfigs() throws Exception {
        String name = javaDefinedBean.getName();
        String value = javaDefinedBean.getValue();
        String version = javaDefinedBean.getVersion();
        ApolloResp resp = new ApolloResp();
        resp.setValue(name + " " + value + " " + version + " ");

        return resp;
    }
}

