package sample.springcloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "pii-xdy-trans", url = "http://16287-tech-credit-cloud-pii-xdy-trans.test.za-tech.net")
public interface TransApi extends io.zhongan.xdy4.trans.api.TransApi {

}
