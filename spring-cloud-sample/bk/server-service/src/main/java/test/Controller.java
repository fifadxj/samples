package test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class Controller {
    @Autowired
    private MongoDbFactory mongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/")
    @ResponseBody
    Resp home(@RequestParam String key) throws Exception {
        Resp resp = new Resp();

        List<Resp> resps = mongoTemplate.findAll(Resp.class, "r");

        for (Resp r : resps) {
            System.out.println(r.getValue());
        }

        return resp;
    }
}

@Getter
@Setter
class Resp {
    private String value;
}

