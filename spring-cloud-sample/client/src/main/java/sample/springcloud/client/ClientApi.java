package sample.springcloud.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/client")
@Slf4j
public class ClientApi {
    @Autowired
    private ServerFeignClient serverFeignClient;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Resp add(@RequestBody Req req) {
        log.info("--> serverFeignClient.add, req:{}", req);
        Resp resp = serverFeignClient.add(req);
        log.info("<-- serverFeignClient.add, resp:{}", resp);

        resp.setValue("result is: " + resp.getValue());

        return resp;
    }
}
