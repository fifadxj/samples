package sample.springboot.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
@Component
public class SystemConfigProps {
    private Map<String, String> appProps = new HashMap<String, String>();

    private Map<String, String> catProps = new HashMap<String, String>();

    private Config appConfig;

    private Config catConfig;

    @Bean
    public SystemConfigBean systemConfigBean() {
        return new SystemConfigBean();
    }

    public SystemConfigProps() {
        appConfig = ConfigService.getAppConfig();
        catConfig = ConfigService.getConfig("TEST2.cat");

        appConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                System.out.println("Changes for namespace " + changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    System.out.println(String.format("Found change - key: %s, propertyName: %s, oldValue: %s, newValue: %s, changeType: %s", key, change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                    if (PropertyChangeType.ADDED == change.getChangeType() || PropertyChangeType.MODIFIED == change.getChangeType()) {
                        appProps.put(key, change.getNewValue());
                    } else if (PropertyChangeType.DELETED.equals(change.getChangeType())) {
                        appProps.remove(key);
                    }

                }
                System.out.println();
            }
        });

        catConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                System.out.println("Changes for namespace " + changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    System.out.println(String.format("Found change - key: %s, propertyName: %s, oldValue: %s, newValue: %s, changeType: %s", key, change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                    catProps.put(key, change.getNewValue());
                }
                System.out.println();
            }
        });
    }

    public String getAppValue(String key) {
        String value = null;
        if (!appProps.containsKey(key)) {
            value = appConfig.getProperty(key, null);
            if (value != null) {
                appProps.put(key, value);
            }
        } else {
            value = appProps.get(key);
        }

        return value;
    }
}
