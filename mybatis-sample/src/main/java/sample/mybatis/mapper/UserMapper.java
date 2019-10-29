package sample.mybatis.mapper;

import java.util.List;
import sample.mybatis.vo.User;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    List selectByCondition(User condition);

    int updateByPrimaryKeySelective(User record);
}