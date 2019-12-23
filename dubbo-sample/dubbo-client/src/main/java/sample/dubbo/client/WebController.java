package sample.dubbo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sample.dubbo.server.api.DubboApi;
import sample.dubbo.server.api.DubboReq;
import sample.dubbo.server.api.DubboResp;

@RestController
public class WebController {
    @Autowired
    private DubboApi dubboApi;

    @RequestMapping(method = RequestMethod.GET, value = "/service")
    public String service(@RequestParam String arg) {
        DubboReq req = new DubboReq();
        req.setBody(arg);
        DubboResp resp = null;
        try {
            resp = dubboApi.service(req);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp.getBody();
    }
}
