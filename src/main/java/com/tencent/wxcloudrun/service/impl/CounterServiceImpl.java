package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CounterServiceImpl implements CounterService {

  final CountersMapper countersMapper;
  final UserMapper userMapper;
  final CartMapper cartMapper;
  final OrderMapper orderMapper;
  final AddressMapper addressMapper;

  public CounterServiceImpl(@Autowired CountersMapper countersMapper, UserMapper userMapper, CartMapper cartMapper, OrderMapper orderMapper, AddressMapper addressMapper) {
    this.countersMapper = countersMapper;
    this.userMapper = userMapper;
    this.cartMapper = cartMapper;
    this.orderMapper = orderMapper;
    this.addressMapper = addressMapper;
  }

  @Override
  public Optional<Counter> getCounter(Integer id) {
    return Optional.ofNullable(countersMapper.getCounter(id));
  }

  @Override
  public void upsertCount(Counter counter) {
    countersMapper.upsertCount(counter);
  }

  @Override
  public void clearCount(Integer id) {
    countersMapper.clearCount(id);
  }

  @Override
  public boolean hasUser(String userId) {
    User user = userMapper.getUser(userId);
    if(user != null) {
      return true;
    }
    return user == null ? false : true;
  }

  @Override
  public void createUser(User user) {
    userMapper.createUser(user);
  }

  @Override
  public void createCart(Cart cart) {
    cartMapper.createCart(cart);
  }

  @Override
  public void updateCart(Cart cart) {
    cartMapper.updateCart(cart);
  }

  @Override
  public void deleteCart(String userID, String goodsID) {
    cartMapper.deleteCart(userID, goodsID);
  }

  @Override
  public void deleteCarts(Map<String, String[]> map) {
    //cartMapper.deleteCarts(userID, goodsIDs);
    cartMapper.deleteCarts(map);
  }

  @Override
  public List<Cart> queryCart(String userID) {
    List<Cart> carts = cartMapper.queryCart(userID);
    return carts;
  }

  @Override
  public Cart queryCartByID(String userID, String goodsID) {
    return cartMapper.queryCartByID(userID, goodsID);
  }

  @Override
  public void createOrder(Order order) {
    orderMapper.createOrder(order);
  }

  @Override
  public void updateOrder(Order order) {
    orderMapper.updateOrder(order);
  }

  @Override
  public void deleteOrder(String orderID) {
    orderMapper.deleteOrder(orderID);
  }

  @Override
  public List<Order> queryOrderByUserID(String userID) {
    return orderMapper.queryOrderByUserId(userID);
  }

  @Override
  public Order queryOrderByID(String orderID) {
    return orderMapper.queryOrderById(orderID);
  }

  @Override
  public Address queryAddressById(String userID, int addressNo) {
    return addressMapper.queryAddressById(userID, addressNo);
  }

  @Override
  public List<Address> queryAddressByUser(String userID) {
    return addressMapper.queryAddressByUser(userID);
  }

  @Override
  public void deleteAddress(String userID, int addressNo) {
    addressMapper.deleteAddress(userID, addressNo);
  }

  @Override
  public void updateAddress(Address address) {
    addressMapper.updateAddress(address);
  }

  @Override
  public void updateDefaultAddress(String userId) {
    addressMapper.updateDefaultAddress(userId);
  }

  @Override
  public void createAddress(Address address) {
    addressMapper.createAddress(address);
  }


}
