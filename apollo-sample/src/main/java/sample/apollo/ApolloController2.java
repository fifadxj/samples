package sample.apollo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class ApolloController2 {

    @Autowired
    private XmlDefinedBean xmlDefinedBean;

    public ApolloController2() {
        System.out.println();
    }


    @RequestMapping(value = "/apollo/getXmlConfigs", method = { RequestMethod.GET })
    @ResponseBody
    ApolloResp getXmlConfigs() throws Exception {
        ApolloResp resp = new ApolloResp();
        resp.setValue(xmlDefinedBean.getVersion());

        return resp;
    }
}

