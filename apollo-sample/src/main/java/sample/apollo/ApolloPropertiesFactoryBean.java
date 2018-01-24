//package sample.apollo;
//
//import com.ctrip.framework.apollo.Config;
//import com.ctrip.framework.apollo.ConfigChangeListener;
//import com.ctrip.framework.apollo.ConfigService;
//import com.ctrip.framework.apollo.enums.PropertyChangeType;
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
//import lombok.Setter;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.context.scope.refresh.RefreshScope;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.support.PropertiesLoaderSupport;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * Created by za-daixiaojun on 2018/1/23.
// */
//
//@Setter
//public class ApolloPropertiesFactoryBean extends PropertiesLoaderSupport implements FactoryBean<Properties> {
//    private Properties props = new Properties();
//
//    private List<String> namespaces;
//
//    private RefreshScope refreshScope;
//
//    @Override
//    public Properties getObject() throws Exception {
//        List<Config> apolloConfigs = new ArrayList<Config>();
//        for (String namespace : namespaces) {
//            Config config = ConfigService.getConfig(namespace);
//
//            for (String key : config.getPropertyNames()) {
//                props.put(key, config.getProperty(key, null));
//            }
//            apolloConfigs.add(config);
//        }
//        configureListeners(apolloConfigs);
//
//        super.loadProperties(props);
//
//        return props;
//    }
//
//    private void configureListeners(List<Config> configs) {
//        for (Config config : configs) {
///*            config.addChangeListener(new ConfigChangeListener() {
//                @Override
//                public void onChange(ConfigChangeEvent changeEvent) {
//                    System.out.println("Changes for namespace " + changeEvent.getNamespace());
//                    for (String key : changeEvent.changedKeys()) {
//                        ConfigChange change = changeEvent.getChange(key);
//                        System.out.println(String.format("Found change - key: %s, propertyName: %s, oldValue: %s, newValue: %s, changeType: %s", key, change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
//                        if (PropertyChangeType.ADDED == change.getChangeType() || PropertyChangeType.MODIFIED == change.getChangeType()) {
//                            props.put(key, change.getNewValue());
//                        } else if (PropertyChangeType.DELETED.equals(change.getChangeType())) {
//                            props.remove(key);
//                        }
//                    }
//                }
//            });*/
//
//            config.addChangeListener(new ConfigChangeListener() {
//                @Override
//                public void onChange(ConfigChangeEvent changeEvent) {
//                    refreshScope.refreshAll();
//                }
//            });
//        }
//    }
//
//    @Override
//    public Class<?> getObjectType() {
//        return Properties.class;
//    }
//
//    @Override
//    public boolean isSingleton() {
//        return true;
//    }
//}
