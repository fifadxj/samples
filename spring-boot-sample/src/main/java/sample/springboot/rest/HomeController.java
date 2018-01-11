package sample.springboot.rest;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
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
    @Value("${version}")
    private String version;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    @ResponseBody
    Resp home(@RequestBody Req req) throws Exception {
        //org.codehaus.jackson.map.util.StdDateFormat
        Resp resp = new Resp();
        resp.setValue(new SimpleDateFormat("yyyy MM dd : HH mm ss").format(req.getDate()));
        resp.setVersion(version);
        resp.setId(idGenerator.generateId().longValue());
        resp.setDate(req.getDate());
        resp.setAmount(BigDecimal.ZERO.setScale(2));

        dataSource.getConnection();

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

