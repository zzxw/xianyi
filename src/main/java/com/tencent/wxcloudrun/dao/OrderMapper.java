package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    Order queryOrderById(@Param("orderID") String orderID);

    List<Order> queryOrderByUserId(@Param("userID") String userID, @Param("startIndex")int startIndex, @Param("pageSize")int pageSize);

    List<Order> queryOrderByStatus(@Param("userID") String userID, @Param("userID") int status, @Param("startIndex")int startIndex, @Param("pageSize")int pageSize);

    void createOrder(Order order);

    void updateOrder(Order order);

    void updateOrderStatus(Order order);

    void deleteOrder(@Param("orderID") String orderID);

    int selectCountByStatus(@Param("userID") String userID, @Param("status")int status);
}
