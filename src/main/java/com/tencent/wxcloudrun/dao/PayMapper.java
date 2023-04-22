package com.tencent.wxcloudrun.dao;


import com.tencent.wxcloudrun.model.PayResult;
import org.apache.ibatis.annotations.Param;


public interface PayMapper {


    void createPayResult(PayResult pay);

    void updateResult(PayResult pay);

    PayResult getResult(@Param("id")String id);

}
