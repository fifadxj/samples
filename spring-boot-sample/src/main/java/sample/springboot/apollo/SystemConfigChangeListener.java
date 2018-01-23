package sample.springboot.apollo;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * Created by za-daixiaojun on 2018/1/23.
 */
@Component
public class SystemConfigChangeListener {
    public SystemConfigChangeListener() {
        System.out.println();
    }

    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener("TEST2.cat")
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshScope.refreshAll();
    }
}
