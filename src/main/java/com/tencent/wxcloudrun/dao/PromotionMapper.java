package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.OrderDetail;
import com.tencent.wxcloudrun.model.Promotion;

import java.util.List;

public interface PromotionMapper {
    List<Promotion> getPromotions(String transactionID);

    Promotion getPromotion(String couponId);

    void newPromotion(Promotion promotion);

}
