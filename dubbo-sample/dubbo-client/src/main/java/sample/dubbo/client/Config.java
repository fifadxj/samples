package sample.dubbo.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix = "dubbo")
@Getter
@Setter
@Service
public class Config {
    @Value("${dubbo.application.name}")
    private String applicationName;
    @Value("${dubbo.provider.timeout}")
    private String providerTimeout;

    @Value("${dubbo.registry.protocol}")
    private String registryProtocol;
    @Value("${dubbo.registry.address}")
    private String registryAddress;
    @Value("${dubbo.registry.group}")
    private String registryGroup;

    @Value("${dubbo.protocol.name}")
    private String protocolName;
    @Value("${dubbo.protocol.port}")
    private Integer protocolPort;
    @Value("${dubbo.protocol.accesslog}")
    private String protocolAccesslog;
    @Value("${dubbo.protocol.threads}")
    private Integer protocolThreads;
    @Value("${dubbo.protocol.threadpool}")
    private String protocolThreadpool;
}
