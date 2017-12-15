package sample.rest;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class HomeController {
    @Value("${version}")
    private String version;

    @Autowired
    private IdGenerator idGenerator;

    @RequestMapping("/")
    @ResponseBody
    Resp home(@RequestParam String key) throws Exception {
        Resp resp = new Resp();
        resp.setValue(key);
        resp.setVersion(version);
        resp.setId(idGenerator.generateId().longValue());

        return resp;
    }
}

@Getter
@Setter
class Resp {
    private String value;
    private String version;
    private long id;
}

