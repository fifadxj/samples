package sample.dubbo.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import sample.dubbo.server.api.DubboApi;

@Configuration
@ComponentScan
public class DubboServerApplication {
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboServerApplication.class);
        context.start();
        Config config = context.getBean(Config.class);

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName(config.getApplicationName());

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol(config.getRegistryProtocol());
        registry.setAddress(config.getRegistryAddress());
        registry.setGroup(config.getRegistryGroup());

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName(config.getProtocolName());
        protocol.setPort(config.getProtocolPort());
        protocol.setThreads(config.getProtocolThreads());
        protocol.setThreadpool(config.getProtocolThreadpool());
        protocol.setAccesslog(config.getProtocolAccesslog());
        protocol.setDispatcher("direct");

        // 服务提供者暴露服务配置
        ServiceConfig<DubboApi> service = new ServiceConfig<DubboApi>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setProtocol(protocol); // 多个协议可以用setProtocols()
        service.setInterface(DubboApi.class);
        DubboApi dubboApi = new DubboApiImpl();
        service.setRef(dubboApi);
        service.setVersion("1");

        // 暴露及注册服务
        service.export();
        System.in.read();
    }
}
