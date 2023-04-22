package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PayResult {
    private String appid;
    private String bankType;
    private int cashFee;
    private String feeType;
    private String isSubscribe;
    private String mchID;
    private String nonceStr;
    private String openid;
    private String outTradeNo;
    private String resultCode;
    private String returnCode;
    private String subAppid;
    private String subIsSubscribe;
    private String subMchID;
    private String subOpenid;
    private String timeEnd;
    private double totalFee;
    private String tradeType;
    private String transactionID;
    private UserInfo userInfo;

    @JsonProperty("appid")
    public String getAppid() { return appid; }
    @JsonProperty("appid")
    public void setAppid(String value) { this.appid = value; }

    @JsonProperty("bankType")
    public String getBankType() { return bankType; }
    @JsonProperty("bankType")
    public void setBankType(String value) { this.bankType = value; }

    @JsonProperty("cashFee")
    public int getCashFee() { return cashFee; }
    @JsonProperty("cashFee")
    public void setCashFee(int value) { this.cashFee = value; }

    @JsonProperty("feeType")
    public String getFeeType() { return feeType; }
    @JsonProperty("feeType")
    public void setFeeType(String value) { this.feeType = value; }

    @JsonProperty("isSubscribe")
    public String getIsSubscribe() { return isSubscribe; }
    @JsonProperty("isSubscribe")
    public void setIsSubscribe(String value) { this.isSubscribe = value; }

    @JsonProperty("mchId")
    public String getMchID() { return mchID; }
    @JsonProperty("mchId")
    public void setMchID(String value) { this.mchID = value; }

    @JsonProperty("nonceStr")
    public String getNonceStr() { return nonceStr; }
    @JsonProperty("nonceStr")
    public void setNonceStr(String value) { this.nonceStr = value; }

    @JsonProperty("openid")
    public String getOpenid() { return openid; }
    @JsonProperty("openid")
    public void setOpenid(String value) { this.openid = value; }

    @JsonProperty("outTradeNo")
    public String getOutTradeNo() { return outTradeNo; }
    @JsonProperty("outTradeNo")
    public void setOutTradeNo(String value) { this.outTradeNo = value; }

    @JsonProperty("resultCode")
    public String getResultCode() { return resultCode; }
    @JsonProperty("resultCode")
    public void setResultCode(String value) { this.resultCode = value; }

    @JsonProperty("returnCode")
    public String getReturnCode() { return returnCode; }
    @JsonProperty("returnCode")
    public void setReturnCode(String value) { this.returnCode = value; }

    @JsonProperty("subAppid")
    public String getSubAppid() { return subAppid; }
    @JsonProperty("subAppid")
    public void setSubAppid(String value) { this.subAppid = value; }

    @JsonProperty("subIsSubscribe")
    public String getSubIsSubscribe() { return subIsSubscribe; }
    @JsonProperty("subIsSubscribe")
    public void setSubIsSubscribe(String value) { this.subIsSubscribe = value; }

    @JsonProperty("subMchId")
    public String getSubMchID() { return subMchID; }
    @JsonProperty("subMchId")
    public void setSubMchID(String value) { this.subMchID = value; }

    @JsonProperty("subOpenid")
    public String getSubOpenid() { return subOpenid; }
    @JsonProperty("subOpenid")
    public void setSubOpenid(String value) { this.subOpenid = value; }

    @JsonProperty("timeEnd")
    public String getTimeEnd() { return timeEnd; }
    @JsonProperty("timeEnd")
    public void setTimeEnd(String value) { this.timeEnd = value; }

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

    @JsonProperty("userInfo")
    public UserInfo getUserInfo() { return userInfo; }
    @JsonProperty("userInfo")
    public void setUserInfo(UserInfo value) { this.userInfo = value; }
}
