package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.CounterService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CounterServiceImpl implements CounterService {

  final CountersMapper countersMapper;
  final UserMapper userMapper;
  final CartMapper cartMapper;
  final OrderMapper orderMapper;
  final AddressMapper addressMapper;
  final OrderDetailMapper orderDetailMapper;

  public CounterServiceImpl(@Autowired CountersMapper countersMapper, UserMapper userMapper, CartMapper cartMapper, OrderMapper orderMapper, AddressMapper addressMapper, OrderDetailMapper orderDetailMapper) {
    this.countersMapper = countersMapper;
    this.userMapper = userMapper;
    this.cartMapper = cartMapper;
    this.orderMapper = orderMapper;
    this.addressMapper = addressMapper;
    this.orderDetailMapper = orderDetailMapper;
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
  public void updateOrderStatus(Order ordre) {

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
  public Address queryDefaultAddress(String userID) {
    return null;
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

  @Override
  public List<OrderDetail> getOrderDetails(String orderId) {
    return orderDetailMapper.getOrderDetails(orderId);
  }

  @Override
  public OrderDetail getOrderDetail(String id) {
    return orderDetailMapper.getOrderDetail(id);
  }

  @Override
  public void newOrderDetail(OrderDetail detail) {
    orderDetailMapper.newOrderDetail(detail);
  }

  @Override
  public void notify(String notifyData) {
    System.out.println(notifyData);
    Document document = null;
    try {
      document = DocumentHelper.parseText(notifyData);
      Element root = document.getRootElement();
      Iterator<Element> iterator = root.elementIterator();
      Map<String, String> map = new HashMap<>();
      while(iterator.hasNext()) {
        Element element = iterator.next();
        String key = element.getName();
        String value = element.getText();
        map.put(key, value);
      }
      if("SUCCESS".equals(map.get("result_code"))) {
        String orderId = map.get("out_trade_no");
        Order order = queryOrderByID(orderId);
        order.setStatus(1);
        updateOrder(order);
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void payOrder(Order order)  {
    HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
    //HttpPost httpPost = new HttpPost("http://api.weixin.qq.com/_/pay/unifiedorder");
    httpPost.addHeader("Accept", "application/json");
    httpPost.addHeader("Content-type","application/json; charset=utf-8");
    CloseableHttpClient httpClient = HttpClients.createDefault();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectMapper objectMapper = new ObjectMapper();


    ObjectNode rootNode = objectMapper.createObjectNode();
    int newPrice = (int) (order.getPrice() * 100);
//    rootNode.put("sub_mch_id","1639911327")
//            .put("appid", "wx7874f23b30f30672")
//            .put("body", "test")
//            .put("notify_url", "https://www.weixin.qq.com/wxpay/pay.php")
//            .put("out_trade_no", order.getOrderID())
//            .put("openid", "wx7874f23b30f30672")
//            .put("spbill_create_ip","127.0.0.1")
//             .put("env_id","prod-6gvg13hsf13d23f3")
//            .put("total_fee", order.getPrice())
//            .put("callback_type",2);
//
//    rootNode.putObject("container")
//            .put("service", "springboot-v3w5")
//            .put("path", "/payNotify");
    rootNode.put("mchid","1639911327")
            .put("appid", "wx7874f23b30f30672")
            .put("description", "test")
            .put("notify_url", "https://springboot-v3w5-37027-5-1317305634.sh.run.tcloudbase.com/payNotify")
            .put("out_trade_no", order.getOrderID());
    rootNode.putObject("amount")
            .put("total", newPrice);
    rootNode.putObject("payer")
            .put("openid", order.getUserID());
    String bodyAsString = null;
    CloseableHttpResponse response = null;
    try {
      objectMapper.writeValue(bos, rootNode);
      httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
      response = httpClient.execute(httpPost);

      bodyAsString = EntityUtils.toString(response.getEntity());
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
      try {
        httpClient.close();
        if(response != null) {
          response.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println(bodyAsString);
  }
}
