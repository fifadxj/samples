package sample.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.tracer.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by za-daixiaojun on 2018/1/24.
 */
@Slf4j
public class ConfigPropertiesBuilder {
    private List<String> namespaces = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    private RefreshScope refreshScope;

    private final String NAMESPACE_COMMON = "TEST2.CAT";

    public ConfigPropertiesBuilder() {
        namespaces.add(ConfigConsts.NAMESPACE_APPLICATION);
        namespaces.add(NAMESPACE_COMMON);
    }

    public ConfigPropertiesBuilder refreshScope(RefreshScope refreshScope) {
        this.refreshScope = refreshScope;
        return this;
    }

    public Properties build() {
        Properties remoteProps = new Properties();
        List<Config> apolloConfigs = new ArrayList<Config>();
        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);

            for (String key : config.getPropertyNames()) {
                remoteProps.put(key, config.getProperty(key, null));
            }
            apolloConfigs.add(config);
        }
        if (refreshScope != null) {
            configureListeners(apolloConfigs);
        }

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

    public ConfigPropertiesBuilder addNamespaces(String ... namespaces) {
        for (String namespace : namespaces) {
            this.namespaces.add(namespace);
        }

        return this;
    }

    public ConfigPropertiesBuilder addLocations(String ... locations) {
        for (String location : locations) {
            this.locations.add(location);
        }

        return this;
    }
}
