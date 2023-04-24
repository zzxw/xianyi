package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {
    private int total;
    private int refund;
    //private From[] from;
    private int payerTotal;
    private int payerRefund;
    private int settlementRefund;
    private int settlementTotal;
    private int discountRefund;
    private String currency;

    @JsonProperty("total")
    public int getTotal() { return total; }
    @JsonProperty("total")
    public void setTotal(int value) { this.total = value; }

    @JsonProperty("refund")
    public int getRefund() { return refund; }
    @JsonProperty("refund")
    public void setRefund(int value) { this.refund = value; }

//    @JsonProperty("from")
//    public From[] getFrom() { return from; }
//    @JsonProperty("from")
//    public void setFrom(From[] value) { this.from = value; }

    @JsonProperty("payer_total")
    public int getPayerTotal() { return payerTotal; }
    @JsonProperty("payer_total")
    public void setPayerTotal(int value) { this.payerTotal = value; }

    @JsonProperty("payer_refund")
    public int getPayerRefund() { return payerRefund; }
    @JsonProperty("payer_refund")
    public void setPayerRefund(int value) { this.payerRefund = value; }

    @JsonProperty("settlement_refund")
    public int getSettlementRefund() { return settlementRefund; }
    @JsonProperty("settlement_refund")
    public void setSettlementRefund(int value) { this.settlementRefund = value; }

    @JsonProperty("settlement_total")
    public int getSettlementTotal() { return settlementTotal; }
    @JsonProperty("settlement_total")
    public void setSettlementTotal(int value) { this.settlementTotal = value; }

    @JsonProperty("discount_refund")
    public int getDiscountRefund() { return discountRefund; }
    @JsonProperty("discount_refund")
    public void setDiscountRefund(int value) { this.discountRefund = value; }

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }
}
