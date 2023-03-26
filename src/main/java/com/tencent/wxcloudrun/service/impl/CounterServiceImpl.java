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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class CounterServiceImpl implements CounterService {

  final CountersMapper countersMapper;
  final UserMapper userMapper;
  final CartMapper cartMapper;
  final OrderMapper orderMapper;
  final AddressMapper addressMapper;
  final OrderDetailMapper orderDetailMapper;
  final GoodsMapper goodsMapper;

  public CounterServiceImpl(@Autowired CountersMapper countersMapper, UserMapper userMapper, CartMapper cartMapper, OrderMapper orderMapper, AddressMapper addressMapper, OrderDetailMapper orderDetailMapper, GoodsMapper goodsMapper) {
    this.countersMapper = countersMapper;
    this.userMapper = userMapper;
    this.cartMapper = cartMapper;
    this.orderMapper = orderMapper;
    this.addressMapper = addressMapper;
    this.orderDetailMapper = orderDetailMapper;
    this.goodsMapper = goodsMapper;
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
  public Cart queryCartByID(String userID, int goodsID) {
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
  public void updateOrderStatus(Order order) {
    orderMapper.updateOrderStatus(order);
  }

  @Override
  public void deleteOrder(String orderID) {
    orderMapper.deleteOrder(orderID);
  }

  @Override
  public List<Order> queryOrderByUserID(String userID, int page,int pageSize) {
    int startIndex = (page-1) * pageSize;
    return orderMapper.queryOrderByUserId(userID, startIndex, pageSize);
  }

  @Override
  public List<Order> queryOrderByStatus(String userID, int status) {
    return orderMapper.queryOrderByStatus(userID, status);
  }

  @Override
  public int selectCountByStatus(String userID, int status) {
    return orderMapper.selectCountByStatus(userID, status);
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
  public Goods queryGoodsDetail(int id) {
    return goodsMapper.queryGoodsDetail(id);
  }

  @Override
  public List<Goods> queryGoods(int page, int pageSize) {
    int startIndex = (page-1) * pageSize;
    return goodsMapper.queryGoods(startIndex,pageSize);
  }

  @Override
  public List<OrderDetail> getOrderDetails(String orderId) {
    return orderDetailMapper.getOrderDetails(orderId);
  }

  @Override
  public Map<String, Object> getOrderInfo(List<Order> orders) {
    Map<String, Object> result = new HashMap<>();
    result.put("pageNum",1);
    result.put("pageSize",10);
    result.put("totalCount",orders.size());
    List<Map<String, Object>> orderList =  new ArrayList<>();
    for(Order order: orders) {
      Map<String, Object> map = getBasicValue(order);

      map.put("orderId", order.getOrderID() + "1");
      List<OrderDetail> list = orderDetailMapper.getOrderDetails(order.getOrderID());
      List<Map<String, Object>> goodsList = new ArrayList<>();

      map.put("orderItemVOs", goodsList);
      for(OrderDetail detail: list) {
        Goods goods = goodsMapper.queryGoodsDetail(detail.getGoodsId());
        Map<String, Object> goodDetail = new HashMap<>();

        int price = (int)(goods.getPrice() * 100);
        int totalPrice = (int)(detail.getTotalPrice() * 100);
        goodDetail.put("id", detail.getId());
        goodDetail.put("orderNo", null);
        goodDetail.put("spuId", goods.getId());
        goodDetail.put("skuId", "135696670");

        goodDetail.put("roomId", null);
        goodDetail.put("goodsMainType", 0);
        goodDetail.put("goodsViceType", 0);
        goodDetail.put("goodsName", goods.getTitle());

        goodDetail.put("specifications", new ArrayList<>());
        goodDetail.put("goodsPictureUrl", goods.getPath());
        goodDetail.put("originPrice", price);
        goodDetail.put("actualPrice", price);

        goodDetail.put("buyQuantity", detail.getNum());
        goodDetail.put("itemTotalAmount", totalPrice);
        goodDetail.put("itemDiscountAmount", 0);
        goodDetail.put("goodsPaymentPrice", 0);

        goodDetail.put("tagPrice", null);
        goodDetail.put("tagText", null);
        goodDetail.put("outCode", null);
        goodDetail.put("labelVOs", null);
        goodDetail.put("buttonVOs", null);
        goodsList.add(goodDetail);

      }
      Address address = addressMapper.queryAddressById(order.getUserID(), order.getAddressNo());
      Map<String, Object> logistics = new HashMap<>();
      logistics.put("logisticsType", 1);
      logistics.put("logisticsNo", "");
      logistics.put("logisticsStatus", null);
      logistics.put("logisticsCompanyCode", "");
      logistics.put("receiverAddressId", address.getAddressNo());

      logistics.put("provinceCode", address.getProvinceCode());
      logistics.put("countryCode", address.getCityCode());
      logistics.put("receiverProvince", address.getProvinceName());
      logistics.put("receiverCity", address.getCityName());
      logistics.put("receiverCountry", address.getDistrictName());

      logistics.put("receiverArea", "");
      logistics.put("receiverAddress", address.getAddress());
      logistics.put("receiverPostCode", "");
      logistics.put("receiverLongitude", "113.829127");
      logistics.put("receiverLatitude", "22.713649");

      logistics.put("receiverIdentity", "88888888205468");
      logistics.put("receiverPhone", address.getPhoneNumber());
      logistics.put("receiverName", address.getName());
      logistics.put("expectArrivalTime", null);
      logistics.put("senderName", "");

      logistics.put("senderPhone", "");
      logistics.put("senderAddress", "");
      logistics.put("sendTime", null);
      logistics.put("arrivalTime", null);

      map.put("logisticsVO", logistics);

      map.put("paymentVO", getPayInfo(order));
      orderList.add(map);
    }
    result.put("orders", orderList);
    return result;
  }

  public Map<String, Object> getPayInfo(Order order) {
    Map<String, Object> map = new HashMap<>();

    map.put("payStatus", 1);
    map.put("amount", String.valueOf(order.getPrice() * 100));
    map.put("currency", null);
    map.put("payType", null);
    map.put("payWay", null);
    map.put("payWayName", null);
    map.put("interactId", null);
    map.put("traceNo", null);
    map.put("channelTrxNo", null);
    map.put("period", null);
    map.put("payTime", null);
    map.put("paySuccessTime", null);
    return map;
  }

  public List<Map<String, Object>> getButtonInfo(int status) {
    List<Map<String, Object>>buttonInfo = new ArrayList<>();
    Map<String, Object> cancelButton = new HashMap<>();
    cancelButton.put("primary",false);
    cancelButton.put("type",2);
    cancelButton.put("name","取消订单");

    Map<String, Object> againButton = new HashMap<>();
    againButton.put("primary",true);
    againButton.put("type",9);
    againButton.put("name","再次购买");

    Map<String, Object> pagButton = new HashMap<>();
    pagButton.put("primary",true);
    pagButton.put("type",1);
    pagButton.put("name","付款");

    if(status == 5){
      buttonInfo.add(cancelButton);
      buttonInfo.add(pagButton);
    }else if(status == 10) {
      buttonInfo.add(cancelButton);
    }
    return buttonInfo;
  }

  public Map<String, Object> getBasicValue(Order order) {
    int totalPrice = (int)(order.getPrice() * 100);
    int status = order.getStatus();
    String statusName = "待付款";
    String remark = "需支付 ￥" + order.getPrice();
    int orderStatus = 5;

    if(status == 1) {
      orderStatus= 10;
      statusName = "待发货";
      remark = "";

    }else if(status == 2){
      orderStatus = 40;
      statusName = "待收货";
      remark = "";
    }

    String cancelTime = Long.toString(LocalDateTime.ofInstant(order.getCreateTime().toInstant(), ZoneId.systemDefault()).plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli());
    Map<String, Object> map = new HashMap<>();
    map.put("saasId","8888888");
    map.put("storeId","1000");
    map.put("storeName","云Mall深圳旗舰店");
    map.put("uid","8888888");
    map.put("parentOrderNo",order.getOrderID());
    map.put("orderNo",order.getOrderID());
    map.put("orderType",0);
    map.put("orderSubType",0);
    map.put("orderStatus", orderStatus);
    map.put("orderSubStatus",null);
    map.put("totalAmount", totalPrice);
    map.put("goodsAmount", totalPrice);

    map.put("paymentAmount",0);
    map.put("freightFee",0);
    map.put("packageFee",0);
    map.put("discountAmount",0);
    map.put("channelType", 0);
    map.put("channelSource","");
    map.put("channelIdentity", "");
    map.put("remark", "");

    map.put("cancelType",null);
    map.put("cancelReasonType",null);
    map.put("cancelReason", null);
    map.put("rightsType", null);
    map.put("createTime", order.getCreateTime().getTime());
    map.put("labelVOs",null);
    map.put("invoiceVO", null);
    map.put("couponAmount", null);

    map.put("autoCancelTime", cancelTime);
    map.put("orderStatusName", statusName);
    map.put("orderSatusRemark", remark);
    map.put("logisticsLogVO", null);
    map.put("invoiceStatus",null);
    map.put("invoiceDesc", null);
    map.put("invoiceUrl", null);

    map.put("buttonVOs", getButtonInfo(orderStatus));
    return map;
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

