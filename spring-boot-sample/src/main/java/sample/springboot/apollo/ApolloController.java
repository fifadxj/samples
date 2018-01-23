package sample.springboot.apollo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class ApolloController {
    @Autowired
    private SystemConfigProps systemConfigProps;

    @Autowired
    private SystemConfigBean systemConfigBean;

    public ApolloController() {

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
        String name = systemConfigBean.getName();
        String value = systemConfigBean.getValue();
        String version = systemConfigBean.getVersion();
        ApolloResp resp = new ApolloResp();
        resp.setValue(name + " " + value + " " + version);

        return resp;
    }
}

