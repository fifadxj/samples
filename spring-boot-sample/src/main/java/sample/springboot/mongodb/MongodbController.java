package sample.springboot.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by za-daixiaojun on 2017/12/13.
 */
@RestController
public class MongodbController {
    @Autowired
    private MongoDbFactory mongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/mongo")
    @ResponseBody
    Record home(@RequestParam String key) throws Exception {
        Record record = new Record();
        return record;
    }
}

@Getter
@Setter
class Record {
    private String value;
}


