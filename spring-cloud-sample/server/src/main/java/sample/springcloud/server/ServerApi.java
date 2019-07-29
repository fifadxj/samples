package sample.springcloud.server;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/server")
public class ServerApi {
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Resp add(@RequestBody Req req) {
        Resp resp = new Resp();
        int value = req.getA() + req.getB();
        resp.setValue(value + "");

        return resp;
    }
}
