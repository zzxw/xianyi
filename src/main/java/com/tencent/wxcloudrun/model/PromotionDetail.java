package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PromotionDetail {
    private String promotionID;
    private String scope;
    private String type;
    private double amount;
    private double refundAmount;
    private String refundId;


    //private GoodsDetail[] goodsDetail;

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    @JsonProperty("promotion_id")
    public String getPromotionID() { return promotionID; }
    @JsonProperty("promotion_id")
    public void setPromotionID(String value) { this.promotionID = value; }

    @JsonProperty("scope")
    public String getScope() { return scope; }
    @JsonProperty("scope")
    public void setScope(String value) { this.scope = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("amount")
    public double getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(double value) { this.amount = value; }

    @JsonProperty("refund_amount")
    public double getRefundAmount() { return refundAmount; }
    @JsonProperty("refund_amount")
    public void setRefundAmount(double value) { this.refundAmount = value; }

//    @JsonProperty("goods_detail")
//    public GoodsDetail[] getGoodsDetail() { return goodsDetail; }
//    @JsonProperty("goods_detail")
//    public void setGoodsDetail(GoodsDetail[] value) { this.goodsDetail = value; }
}
