package sample.springcloud.client;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class ClientApplication {
	@Bean
	Logger.Level feignLoggerLevel() {
        //return Logger.Level.FULL;
		return Logger.Level.NONE;
	}

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

}
