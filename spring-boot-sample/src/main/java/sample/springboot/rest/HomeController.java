package sample.springboot.rest;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import io.shardingjdbc.core.keygen.KeyGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @RequestMapping(value = "/apollo/getAppConfig", method = { RequestMethod.GET })
    @ResponseBody
    Resp getAppConfig(@RequestParam String key) throws Exception {
        String value = appConfig.getProperty(key, null);
        Resp resp = new Resp();
        resp.setValue(value);

        return resp;
    }

    @RequestMapping(value = "/apollo/getCatConfig", method = { RequestMethod.GET })
    @ResponseBody
    Resp getCatConfig(@RequestParam String key) throws Exception {
        String value = catConfig.getProperty(key, null);
        Resp resp = new Resp();
        resp.setValue(value);

        return resp;
    }
}

@Getter
@Setter
class Resp {
    private String value;
    private String version;
    private long id;
    private Date date;
    private BigDecimal amount;
}

@Getter
@Setter
class Req {
    private Date date;
}

