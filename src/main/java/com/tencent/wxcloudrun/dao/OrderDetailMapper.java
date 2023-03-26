package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OrderDetail;

import java.util.List;

public interface OrderDetailMapper {
    List<OrderDetail> getOrderDetails(String orderId);

    OrderDetail getOrderDetail(String id);

    void newOrderDetail(OrderDetail detail);

    void deleteByOrderId(String orderId);
}
