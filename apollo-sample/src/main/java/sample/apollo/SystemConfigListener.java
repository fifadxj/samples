package sample.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
//@Component
public class SystemConfigListener {
    public SystemConfigListener() {
    }

    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener("application")
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshScope.refreshAll();
    }
}
