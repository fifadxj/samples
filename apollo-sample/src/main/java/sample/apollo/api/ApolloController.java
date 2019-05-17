package sample.apollo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.apollo.api.resp.ApolloResp;

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
        ApolloResp resp = new ApolloResp();
        resp.setJavaDefinedBean(javaDefinedBean);
        resp.setXmlDefinedBean(xmlDefinedBean);

        return resp;
    }
}

