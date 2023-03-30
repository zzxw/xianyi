package com.tencent.wxcloudrun.model;

import java.time.LocalDateTime;

public class PayResult {
    private String bank;
    private int total;
    private String orderId;
    private String tradeType;
    private String id;
    private LocalDateTime time;
    private String userId;

    public PayResult(String bank, int total, String orderId, String tradeType, String id, LocalDateTime time, String userId) {
        this.bank = bank;
        this.total = total;
        this.orderId = orderId;
        this.tradeType = tradeType;
        this.id = id;
        this.time = time;
        this.userId = userId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
