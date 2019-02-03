package sample.springcloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "pii-xdy-user", url = "16287-tech-credit-cloud-pii-xdy-user.test.za-tech.net")
public interface UserApi extends io.zhongan.xdy4.user.api.UserApi {

}
