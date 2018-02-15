package sample.springboot.rest;

import io.shardingjdbc.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class HomeController {
    @Autowired
    private KeyGenerator idGenerator;

    public HomeController() {
    }

    @RequestMapping(value = "/", method = { RequestMethod.POST })
    @ResponseBody
    Resp home(@RequestBody Req req) throws Exception {
        //org.codehaus.jackson.map.util.StdDateFormat
        Resp resp = new Resp();
        resp.setId(idGenerator.generateKey().longValue());
        resp.setDate(req.getDate());

        return resp;
    }
}

