package sample.springboot.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MongoController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", method = { RequestMethod.POST })
    @ResponseBody
    CreateUserResp createUser(@RequestBody CreateUserReq req) {
        CreateUserResp resp = new CreateUserResp();

        User user = new User();
        user.setUsername("terry");
        user.setPassword("password");
        mongoTemplate.insert(user);
        resp.setId(user.getId());

        return resp;
    }
}

