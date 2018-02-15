package sample.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.core.io.support.PropertiesLoaderSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */

@Setter
@Slf4j
public class ApolloPropertiesFactory extends PropertiesLoaderSupport {
    private RefreshScope refreshScope;

    public Properties create() {
        Properties props = new Properties();

        //本地配置
        //super.loadProperties(props);

        //环境变量
        Map<String,String> env = System.getenv();
        props.putAll(env);

        //远程配置
        List<Config> apolloConfigs = new ArrayList<Config>();
        List<String> namespaces = loadNamespaces();
        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);

            for (String key : config.getPropertyNames()) {
                props.put(key, config.getProperty(key, null));
            }
            apolloConfigs.add(config);
        }
        if (refreshScope != null) {
            configureListeners(apolloConfigs);
        }

        return props;
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

    protected static List<String> loadNamespaces() {
        InputStream in = ApolloPropertiesFactory.class.getResourceAsStream(DefaultApplicationProvider.APP_PROPERTIES_CLASSPATH);
        Properties properties = null;

        if (in != null) {
            properties = new Properties();

            try {
                properties.load(in);
            } catch (IOException ex) {
                Tracer.logError(ex);
                log.error("Load namespaces failed", ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }

        List<String> namespaces = new ArrayList<>();
        if (properties != null) {
            String namespacesValue = (String) properties.get("namespaces");
            String[] namespacesArray = namespacesValue.trim().split(",");
            namespaces = Arrays.asList(namespacesArray);
        }

        return namespaces;
    }
}
