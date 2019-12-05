package sample.springcloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "server", fallback = ServerFeignClientFallback.class)
@RequestMapping(value = "/server")
public interface ServerFeignClient {
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Resp add(@RequestBody Req req);
}
