package sample.mybatis.mapper2;

import sample.mybatis.vo.User;

import java.util.List;

public interface UserMapper2 {
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List selectByCondition(User condition);

    int updateByPrimaryKeySelective(User record);
}