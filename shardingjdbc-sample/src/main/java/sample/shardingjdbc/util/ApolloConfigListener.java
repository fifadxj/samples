package sample.shardingjdbc.util;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class ApolloConfigListener {
    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener({"application"})
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshScope.refreshAll();
    }
}
