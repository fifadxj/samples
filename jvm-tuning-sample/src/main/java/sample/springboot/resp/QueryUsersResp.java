package sample.springboot.resp;

import lombok.Getter;
import lombok.Setter;
import sample.springboot.dto.User;

import java.util.List;

@Getter
@Setter
public class QueryUsersResp {
    private List<User> users;
}
