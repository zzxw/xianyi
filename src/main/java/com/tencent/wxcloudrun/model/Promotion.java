package com.tencent.wxcloudrun.model;

public class Promotion {
    private String couponId;
    private String name;
    private String scope;
    private String type;
    private double amount;
    private String stockId;
    private double wechatPay;
    private double otherPay;
    private String transactionID;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public double getWechatPay() {
        return wechatPay;
    }

    public void setWechatPay(double wechatPay) {
        this.wechatPay = wechatPay;
    }

    public double getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(double otherPay) {
        this.otherPay = otherPay;
    }
}
