package sample.springboot.rest;

import cn.sccfc.config.client.ConfigClient;
import io.shardingjdbc.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
@RefreshScope
public class HomeController {
    @Autowired
    private KeyGenerator idGenerator;

    @Value("#{env['version']}")
    private String version;

    //@Value("#{env['version']}")
    private String version2;

    //@Value("#{env['version']}")
    private String version3;

    //@Value("#{env['amount']}")
    private int amount;

    //@Autowired
    private Settings settings;

    public HomeController() {
    }

    @RequestMapping(value = "/", method = { RequestMethod.POST })
    @ResponseBody
    Resp home(@RequestBody Req req) throws Exception {
        //org.codehaus.jackson.map.util.StdDateFormat
        Resp resp = new Resp();
        //resp.setValue(settings.getValue());
        resp.setVersion(version);
        resp.setVersion2(ConfigClient.getProperty("version"));
        resp.setId(idGenerator.generateKey().longValue());
        resp.setDate(req.getDate());
        resp.setAmount(amount);

        return resp;
    }
}

