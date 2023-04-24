package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefundResult {
    private String refundID;
    private String outRefundNo;
    private String transactionID;
    private String outTradeNo;
    private String channel;
    private String userReceivedAccount;
    private String successTime;
    private String createTime;
    private String status;
    private String fundsAccount;
    private Amount amount;
    private PromotionDetail[] promotionDetail;
    private double total;
    private double refund;
    private double payerTotal;
    private double payerRefund;
    private double settlementRefund;
    private double settlementTotal;
    private double discountRefund;
    private String currency;
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public double getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(double payerTotal) {
        this.payerTotal = payerTotal;
    }

    public double getPayerRefund() {
        return payerRefund;
    }

    public void setPayerRefund(double payerRefund) {
        this.payerRefund = payerRefund;
    }

    public double getSettlementRefund() {
        return settlementRefund;
    }

    public void setSettlementRefund(double settlementRefund) {
        this.settlementRefund = settlementRefund;
    }

    public double getSettlementTotal() {
        return settlementTotal;
    }

    public void setSettlementTotal(double settlementTotal) {
        this.settlementTotal = settlementTotal;
    }

    public double getDiscountRefund() {
        return discountRefund;
    }

    public void setDiscountRefund(double discountRefund) {
        this.discountRefund = discountRefund;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @JsonProperty("refund_id")
    public String getRefundID() { return refundID; }
    @JsonProperty("refund_id")
    public void setRefundID(String value) { this.refundID = value; }

    @JsonProperty("out_refund_no")
    public String getOutRefundNo() { return outRefundNo; }
    @JsonProperty("out_refund_no")
    public void setOutRefundNo(String value) { this.outRefundNo = value; }

    @JsonProperty("transaction_id")
    public String getTransactionID() { return transactionID; }
    @JsonProperty("transaction_id")
    public void setTransactionID(String value) { this.transactionID = value; }

    @JsonProperty("out_trade_no")
    public String getOutTradeNo() { return outTradeNo; }
    @JsonProperty("out_trade_no")
    public void setOutTradeNo(String value) { this.outTradeNo = value; }

    @JsonProperty("channel")
    public String getChannel() { return channel; }
    @JsonProperty("channel")
    public void setChannel(String value) { this.channel = value; }

    @JsonProperty("user_received_account")
    public String getUserReceivedAccount() { return userReceivedAccount; }
    @JsonProperty("user_received_account")
    public void setUserReceivedAccount(String value) { this.userReceivedAccount = value; }

    @JsonProperty("success_time")
    public String getSuccessTime() { return successTime; }
    @JsonProperty("success_time")
    public void setSuccessTime(String value) { this.successTime = value; }

    @JsonProperty("create_time")
    public String getCreateTime() { return createTime; }
    @JsonProperty("create_time")
    public void setCreateTime(String value) { this.createTime = value; }

    @JsonProperty("status")
    public String getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(String value) { this.status = value; }

    @JsonProperty("funds_account")
    public String getFundsAccount() { return fundsAccount; }
    @JsonProperty("funds_account")
    public void setFundsAccount(String value) { this.fundsAccount = value; }

    @JsonProperty("amount")
    public Amount getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(Amount value) { this.amount = value; }

    @JsonProperty("promotion_detail")
    public PromotionDetail[] getPromotionDetail() { return promotionDetail; }
    @JsonProperty("promotion_detail")
    public void setPromotionDetail(PromotionDetail[] value) { this.promotionDetail = value; }
}
