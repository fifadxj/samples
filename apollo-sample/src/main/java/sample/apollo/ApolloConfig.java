package sample.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.tracer.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Configuration
@Import(RefreshAutoConfiguration.class)
@ImportResource("classpath:/spring.xml")
@Slf4j
//@PropertySource({"classpath:/env/test.properties", "classpath:/env/test2.properties"})
//@EnableApolloConfig(value = {"TEST2.cat", "application"})
@ComponentScan
@EnableWebMvc
public class ApolloConfig {
    @Autowired
    private RefreshScope refreshScope;

/*    @Bean
    @Lazy(false)
    @org.springframework.cloud.context.config.annotation.RefreshScope
    public ApolloPropertiesFactoryBean env() {
        ApolloPropertiesFactoryBean factory = new ApolloPropertiesFactoryBean();
        List<String> namespaces = new ArrayList<>();
        namespaces.add("application");
        namespaces.add("TEST2.cat");
        factory.setNamespaces(namespaces);
        factory.setLocations(new ClassPathResource("/env/test.properties"));
        factory.setRefreshScope(refreshScope);
        return factory;
    }*/

    @Bean
    @Lazy(false)
    @org.springframework.cloud.context.config.annotation.RefreshScope
    public Properties env() {
        Properties remoteProps = new Properties();
        List<Config> apolloConfigs = new ArrayList<Config>();

        List<String> namespaces = new ArrayList<>();
        namespaces.add("application");
        namespaces.add("TEST2.cat");

        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);

            for (String key : config.getPropertyNames()) {
                remoteProps.put(key, config.getProperty(key, null));
            }
            apolloConfigs.add(config);
        }
        configureListeners(apolloConfigs);

        List<String> locations = new ArrayList<>();
        locations.add("/env/test.properties");
        locations.add("/env/test2.properties");
        Properties localProps = new Properties();
        for (String location : locations) {
            Properties local = loadFromResource(location);
            localProps.putAll(local);
        }

        localProps.putAll(remoteProps);

        return localProps;
    }

    private void configureListeners(List<Config> configs) {
        for (Config config : configs) {
/*            config.addChangeListener(new ConfigChangeListener() {
                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    System.out.println("Changes for namespace " + changeEvent.getNamespace());
                    for (String key : changeEvent.changedKeys()) {
                        ConfigChange change = changeEvent.getChange(key);
                        System.out.println(String.format("Found change - key: %s, propertyName: %s, oldValue: %s, newValue: %s, changeType: %s", key, change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                        if (PropertyChangeType.ADDED == change.getChangeType() || PropertyChangeType.MODIFIED == change.getChangeType()) {
                            props.put(key, change.getNewValue());
                        } else if (PropertyChangeType.DELETED.equals(change.getChangeType())) {
                            props.remove(key);
                        }
                    }
                }
            });*/

            config.addChangeListener(new ConfigChangeListener() {
                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    refreshScope.refreshAll();
                }
            });
        }
    }

    private Properties loadFromResource(String namespace) {
        InputStream in = ClassLoaderUtil.getLoader().getResourceAsStream(namespace);
        Properties properties = null;

        if (in != null) {
            properties = new Properties();

            try {
                properties.load(in);
            } catch (IOException ex) {
                Tracer.logError(ex);
                log.error("Load resource config for namespace {} failed", namespace, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }

        return properties;
    }
}
