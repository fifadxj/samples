package sample.dubbo.client;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import sample.dubbo.server.api.DubboApi;

@SpringBootApplication
//@ImportResource({"classpath:/dubbo-client.xml"})
@EnableConfigurationProperties(Config.class)
public class DubboClientApplication {
    @Autowired
    private Config config;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DubboClientApplication.class, args);
    }

    @Bean
    public DubboApi dubboApi() {
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(config.getApplicationName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol(config.getRegistryProtocol());
        registry.setAddress(config.getRegistryAddress());
        registry.setGroup(config.getRegistryGroup());

        // 引用远程服务
        ReferenceConfig<DubboApi> reference = new ReferenceConfig<DubboApi>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(application);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(DubboApi.class);
        reference.setVersion("1");
        reference.setConnections(2);

        DubboApi dubboApi = reference.get();

        return dubboApi;
    }
}
