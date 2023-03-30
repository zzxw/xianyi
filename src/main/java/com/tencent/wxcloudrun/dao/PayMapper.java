package com.tencent.wxcloudrun.dao;


import com.tencent.wxcloudrun.model.PayResult;


public interface PayMapper {


    void createPayResult(PayResult pay);

    void updateResult(PayResult pay);

}
