package sample.springboot.rest;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import io.shardingjdbc.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class HomeController {
    //@Value("${version}")
    private String version;

    @Autowired
    private KeyGenerator idGenerator;
    @Autowired
    private DataSource dataSource;

    private Config appConfig;

    private Config catConfig;

    public HomeController() {
        appConfig = ConfigService.getAppConfig();
        catConfig = ConfigService.getConfig("TEST2.cat");

        appConfig.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                System.out.println("Changes for namespace " + changeEvent.getNamespace());
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    System.out.println(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
                }
            }
        });
    }

    @RequestMapping(value = "/", method = { RequestMethod.POST })
    @ResponseBody
    Resp home(@RequestBody Req req) throws Exception {
        //org.codehaus.jackson.map.util.StdDateFormat
        Resp resp = new Resp();
        resp.setValue(new SimpleDateFormat("yyyy MM dd : HH mm ss").format(req.getDate()));
        resp.setVersion(version);
        resp.setId(idGenerator.generateKey().longValue());
        resp.setDate(req.getDate());
        resp.setAmount(BigDecimal.ZERO.setScale(2));

        return resp;
    }
}

