package sample.springboot.controller;

import org.springframework.stereotype.Service;
import sample.springboot.dto.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDao {
    public List<User> queryUsers(User condition) {
        List<User> users = new ArrayList<>();
        for (long i = 0; i < 1000 * 1000; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("username");
            user.setPassword("password");
            user.setIcon(new byte[1024 * 1024]);
            users.add(user);
        }

        return users;
    }

    public List<User> queryUsersByPage(long pageSize) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<User> users = new ArrayList<>();
        for (long i = 0; i < pageSize; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("username");
            user.setPassword("password");
            user.setIcon(new byte[1024 * 1024]);
            users.add(user);
        }

        return users;
    }
}
