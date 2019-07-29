package sample.springcloud.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class ServerFeignClientFallback implements ServerFeignClient {
    @Override
    @RequestMapping(value = "/fallback/add")
    public Resp add(Req req) {
        Resp resp = new Resp();
        resp.setValue("no result");
        return resp;
    }
}
