package sample.springboot.mongodb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String username;
    private String password;
}
