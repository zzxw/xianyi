package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);

  User getUser(String userID);

  void createUser(User userInfo);

  void createCart(Cart cart);

  void updateCart(Cart cart);

  void deleteCart(String userID, String goodsID, int specId);
  //void deleteCarts(String userID, String goodsIDs);
  void deleteCarts(Map<String, String[]> map);

  List<Cart> queryCart(String userID);

  Cart queryCartByID(String userID, int goodsID, int specId);

  void createOrder(Order order);

  void updateOrder(Order order);

  void updateOrderStatus(Order order);

  void deleteOrder(String orderID);

  void deleteOrderDetailByOrderId(String orderId);

  List<Order> queryOrderByUserID(String userID,int page, int pageSize);

  List<Order> queryOrderByStatus(String userID, int status,int page, int pageSize);

  int selectCountByStatus(String userID, int status);

  Order queryOrderByID(String orderID);

  Address queryAddressById(String userID, int addressNo);

  Address queryDefaultAddress(String userID);

  List<Address> queryAddressByUser(String userID);

  //void deleteAddress(String userID, int addressNo);

  void deleteAddress(String userID, int addressNo);

  void updateAddress(Address address);

  void updateDefaultAddress(String userId);

  void createAddress(Address address);

  Goods queryGoodsDetail(int id);

  List<Goods> queryGoods(int page, int pageSize);

  List<Goods> queryGoodsByType(int page, int pageSize, int category);

  List<OrderDetail> getOrderDetails(String orderId);

  Map<String, Object> getOrderInfo(List<Order> orders,int page, int pageSize);

  OrderDetail getOrderDetail(String id);

  void newOrderDetail(OrderDetail detail);

  void notify(String notifyData);

  String payOrder(Order order);

  JSONObject getOpenId(String code);

  User getUserInfo(String openId);

  JSONObject getPhoneNumber(String code);

  void closeOrder(String orderId);

  void refundOrder();

  String sign(String prepareId, String timeStamp, String nonStr);

  String getSign(String prepareId, String timeStamp, String nonStr);

  void newFeedback(Feedback feedback);

  List<Feedback> queryFeedbackByUser(String userId);

  Feedback queryFeedbackById(String userId, int id);

  PayResult getResult(String id);

  void insertResult(PayResult payResult);
}
