package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.PromotionDetail;

import java.util.List;

public interface PromotionDetailMapper {
    List<PromotionDetail> getPromotionDetails(String refundId);

    PromotionDetail getPromotionDetail(String couponId);

    void newPromotionDetail(PromotionDetail promotionDetail);

}
