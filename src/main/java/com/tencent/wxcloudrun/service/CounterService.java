package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CounterService {

  Optional<Counter> getCounter(Integer id);

  void upsertCount(Counter counter);

  void clearCount(Integer id);

  boolean hasUser(String userID);

  void createUser(User userInfo);

  void createCart(Cart cart);

  void updateCart(Cart cart);

  void deleteCart(String userID, String goodsID);
  //void deleteCarts(String userID, String goodsIDs);
  void deleteCarts(Map<String, String[]> map);

  List<Cart> queryCart(String userID);

  Cart queryCartByID(String userID, String goodsID);

  void createOrder(Order order);

  void updateOrder(Order order);

  void updateOrderStatus(Order order);

  void deleteOrder(String orderID);

  List<Order> queryOrderByUserID(String userID);

  List<Order> queryOrderByStatus(String userID, int status);

  Order queryOrderByID(String orderID);

  Address queryAddressById(String userID, int addressNo);

  Address queryDefaultAddress(String userID);

  List<Address> queryAddressByUser(String userID);

  void deleteAddress(String userID, int addressNo);

  void updateAddress(Address address);

  void updateDefaultAddress(String userId);

  void createAddress(Address address);

  Goods queryGoodsDetail(int id);

  List<Goods> queryGoods(int page, int pageSize);

  List<OrderDetail> getOrderDetails(String orderId);

  OrderDetail getOrderDetail(String id);

  void newOrderDetail(OrderDetail detail);

  void notify(String notifyData);

  void payOrder(Order order);
}
