package com.tencent.wxcloudrun.dao;



import com.tencent.wxcloudrun.model.RefundResult;
import org.apache.ibatis.annotations.Param;


public interface RefundResultMapper {


    void createRefundPayResult(RefundResult result);

    void updateRefundPay(RefundResult result);

    RefundResult getRefundResult(@Param("outRefundNo")String outRefundNo);

}
