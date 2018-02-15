package sample.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class ConfigClient {
    private static Properties props;

    public static synchronized String getProperty(String key) {
        if (props == null) {
            ApolloPropertiesFactory factory = new ApolloPropertiesFactory();
            try {
                props = factory.create();
            } catch (Exception e) {
                throw new RuntimeException("failed to init Apollo config", e);
            }
        }
        String value = props.getProperty(key);
        return value;
    }

    public static String getFreshProperty(String key) {
        List<String> namespaces = ApolloPropertiesFactory.loadNamespaces();
        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);
            String value = config.getProperty(key, null);

            if (value != null) {
                return value;
            }
        }

        return null;
    }
}
