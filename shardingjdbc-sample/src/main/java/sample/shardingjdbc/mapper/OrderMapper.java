package sample.shardingjdbc.mapper;

import sample.shardingjdbc.model.Order;

public interface OrderMapper {
    long insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    long updateByPrimaryKeySelective(Order record);

    long updateByPrimaryKey(Order record);
}