package sample.springcloud.client;


import io.zhongan.xdy4.product.enums.InterestWayEnum;
import io.zhongan.xdy4.trans.api.req.LoanGenerateReq;
import io.zhongan.xdy4.trans.api.req.TransLoanTrialReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        TransApi transApi = applicationContext.getBean(TransApi.class);
        LoanGenerateReq req = new LoanGenerateReq();
        Object resp = transApi.loanGenerate(req);
        System.out.println(resp);
    }
}
