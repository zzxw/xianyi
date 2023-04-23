package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class PayResult {
    private String appid;
    private String bankType;
    private String mchID;
    private String openid;
    private String outTradeNo;
    private double payerTotal;
    private String attach;
    private String successTime;
    private double totalFee;
    private String tradeType;
    private String transactionID;
    private List<Promotion> promotionList;

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public double getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(double payerTotal) {
        this.payerTotal = payerTotal;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    @JsonProperty("appid")
    public String getAppid() { return appid; }
    @JsonProperty("appid")
    public void setAppid(String value) { this.appid = value; }

    @JsonProperty("bankType")
    public String getBankType() { return bankType; }
    @JsonProperty("bankType")
    public void setBankType(String value) { this.bankType = value; }


    @JsonProperty("mchId")
    public String getMchID() { return mchID; }
    @JsonProperty("mchId")
    public void setMchID(String value) { this.mchID = value; }


    @JsonProperty("openid")
    public String getOpenid() { return openid; }
    @JsonProperty("openid")
    public void setOpenid(String value) { this.openid = value; }

    @JsonProperty("outTradeNo")
    public String getOutTradeNo() { return outTradeNo; }
    @JsonProperty("outTradeNo")
    public void setOutTradeNo(String value) { this.outTradeNo = value; }

    @JsonProperty("totalFee")
    public double getTotalFee() { return totalFee; }
    @JsonProperty("totalFee")
    public void setTotalFee(double value) { this.totalFee = value; }

    @JsonProperty("tradeType")
    public String getTradeType() { return tradeType; }
    @JsonProperty("tradeType")
    public void setTradeType(String value) { this.tradeType = value; }

    @JsonProperty("transactionId")
    public String getTransactionID() { return transactionID; }
    @JsonProperty("transactionId")
    public void setTransactionID(String value) { this.transactionID = value; }

}
