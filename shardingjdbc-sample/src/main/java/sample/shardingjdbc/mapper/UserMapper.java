package sample.shardingjdbc.mapper;

import sample.shardingjdbc.model.User;
import sample.shardingjdbc.req.SqlReq;

import java.util.List;

public interface UserMapper {
    long insertSelective(User record);

    User selectByPrimaryKey(Long id);

    long updateByPrimaryKeySelective(User record);

    long updateByPrimaryKey(User record);

    List<User> selectBySql(SqlReq username);
}