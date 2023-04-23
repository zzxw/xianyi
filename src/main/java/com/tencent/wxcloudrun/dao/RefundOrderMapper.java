package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.RefundOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefundOrderMapper {
    RefundOrder queryRefundOrderById(@Param("id") String id);

    List<RefundOrder> queryRefundOrderByUserId(@Param("userID") String userID, @Param("startIndex")int startIndex, @Param("pageSize")int pageSize);

    List<RefundOrder> queryRefundOrderByStatus(@Param("userID") String userID, @Param("status") int status, @Param("startIndex")int startIndex, @Param("pageSize")int pageSize);

    void createRefundOrder(RefundOrder order);

    void updateRefundOrder(RefundOrder order);

    void updateRefundOrderStatus(RefundOrder order);

    void deleteRefundOrder(@Param("orderID") String orderID);

    int selectCountByStatus(@Param("userID") String userID, @Param("status")int status);
}
