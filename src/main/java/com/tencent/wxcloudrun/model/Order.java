package com.tencent.wxcloudrun.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String userID;

//    private String goodsID;
//
//    private int num;

    private int status;

    private String orderID;

    private Date createTime;

    private double price;

    int addressNo;

    private String deliveryId;

    private String waybillId;

    private String closeTime;

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    private List<OrderDetail> list;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

//    public String getGoodsID() {
//        return goodsID;
//    }
//
//    public void setGoodsID(String goodsID) {
//        this.goodsID = goodsID;
//    }
//
//    public int getNum() {
//        return num;
//    }
//
//    public void setNum(int num) {
//        this.num = num;
//    }

    public List<OrderDetail> getList() {
        return list;
    }

    public void setList(List<OrderDetail> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(int addressNo) {
        this.addressNo = addressNo;
    }
}
