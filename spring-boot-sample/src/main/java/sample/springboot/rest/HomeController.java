package sample.springboot.rest;

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
    @Autowired
    private KeyGenerator idGenerator;
    @Autowired
    private DataSource dataSource;

    public HomeController() {
    }

    @RequestMapping(value = "/", method = { RequestMethod.POST })
    @ResponseBody
    Resp home(@RequestBody Req req) throws Exception {
        //org.codehaus.jackson.map.util.StdDateFormat
        Resp resp = new Resp();
        resp.setValue(new SimpleDateFormat("yyyy MM dd : HH mm ss").format(req.getDate()));
        resp.setId(idGenerator.generateKey().longValue());
        resp.setDate(req.getDate());
        resp.setAmount(BigDecimal.ZERO.setScale(2));

        return resp;
    }
}

