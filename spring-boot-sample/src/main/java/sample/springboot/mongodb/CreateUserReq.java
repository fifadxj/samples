package sample.springboot.mongodb;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by za-daixiaojun on 2018/1/22.
 */
@Getter
@Setter
public class CreateUserReq {
    private String username;
    private String password;
}
