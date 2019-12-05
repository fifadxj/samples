package sample.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sample.mybatis.mapper.UserMapper;
import sample.mybatis.mapper2.UserMapper2;
import sample.mybatis.vo.User;

import javax.annotation.Resource;

@RestController
public class UserController {
    @Resource(name = "transactionTemplate")
    TransactionTemplate transactionTemplate;

    @Resource(name = "transactionTemplate2")
    TransactionTemplate transactionTemplate2;

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMapper2 userMapper2;

    @RequestMapping(value = "test", method = {RequestMethod.GET})
    public String test() throws InterruptedException {
        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                User user = new User();
                user.setName("terry11");
                user.setPwd("terry11");
                userMapper.insertSelective(user);
                return user.getId();
            }
        });

        transactionTemplate2.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                User user = new User();
                user.setName("terry22");
                user.setPwd("terry22");
                userMapper2.insertSelective(user);
                return user.getId();
            }
        });

        return "ok";
    }

}
