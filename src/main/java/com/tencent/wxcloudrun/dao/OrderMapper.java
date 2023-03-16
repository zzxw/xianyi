package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    Order queryOrderById(@Param("orderID") String orderID);

    List<Order> queryOrderByUserId(@Param("userID") String userID);

    void createOrder(Order order);

    void updateOrder(Order order);

    void deleteOrder(@Param("orderID") String orderID);
}
