package com.tencent.wxcloudrun.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.util.HttpClientSslUtils;
import com.tencent.wxcloudrun.util.Util;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class CounterServiceImpl implements CounterService {

  final static String APPID = "wx7874f23b30f30672";
  final String SECRET="7c88c65b513292819127ef927921d708";
  String token = "";
  final CountersMapper countersMapper;
  final UserMapper userMapper;
  final CartMapper cartMapper;
  final OrderMapper orderMapper;
  final AddressMapper addressMapper;
  final OrderDetailMapper orderDetailMapper;
  final GoodsMapper goodsMapper;
  final PayMapper payMapper;
  final FeedbackMapper feedbackMapper;
  final PromotionMapper promotionMapper;
  final PromotionDetailMapper promotionDetailMapper;
  final RefundResultMapper refundResultMapper;

  private static final String MERCHANTSERIALNUMBER ="4884D435779D77A97A92A12D8A0B4D1E6B3EFAD0";
  private static final String MERCHANTID ="1639911327";
  private static final String MERCHANTPRIVATEKEY ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDeDbOnafXVVMzj\n" +
          "/71xivDebiOHelu4W4pCMX3vfzuZDoL0Q/5zGeAgR1o0sRgaDCjpgMO5bpWBdPsm\n" +
          "wOOCtfYmB+K4mgNjSeiE9Q9UebwEy9uq3ZAkNwReJNZH1f+53FhSgvMy+1rQ4ntK\n" +
          "Ecziv0lA9EzcOzZpTvyF28VPBlGIenSmsVLRKzaSQlJaTH2Qas3nCKWddgDY1Cx2\n" +
          "2dm/Ad1DuMIomEsqfjCZgIQTvup3N83w/lSKbuHmWJ175ce+KFAfXBQ5EKeNK39O\n" +
          "i4kgP2115m+Crro3aDXe9PUVwkqBu7AfvRXEQ5tXt5YuFKiURKFX9nO/ah7AzGRe\n" +
          "XBIB4ig5AgMBAAECggEAG6vrcnZy6zXZHqSVEBw3bhCdntlxqqTFswAF6J2baLR1\n" +
          "P0ll4SQdWQhrRlu3XC+dvZONUINmYC6aybaJ45UXap/a8hRHTc09C6yaT3WoJ5Tb\n" +
          "+AwKVWkBw1Wl2mfhqWC7JPUqp3TJWXSP1qgnNy6NS2nmVh+O5Uqxj2DW0sU/zdjx\n" +
          "S+XkUYG4J9ONtVTPu0fdQXilBfCVL9CHW0yH9n5GY32OcCKkZCTmwEnBHF+tM4a9\n" +
          "VsVEj/wbaO88abOvlSkXIdrKeIlbVI+6bpweUx9ktp4QjNN7/HAlYxKSAqemkSbZ\n" +
          "vZUCknTr9CC7xg0zdE+SSrRkKs7mfkNWTAq39lTu8QKBgQD45yPgpHFYuMXB/sNR\n" +
          "xBrMZIe7ydso1QMqqYV1n2+Kcrub57LfzjaNQ3IGUrv2bsW2N9OBhSBABBoPJjQj\n" +
          "hMDh8VILAh+ALVUm/4urj6gd2QhwlsBn89sj9Yb12CjaS2MqWd9xzMVYSdHB8kgk\n" +
          "+XVLUnNPKcPX/IKWLSt27U92lQKBgQDkYpNNBIIlBLirCUzEGpBfgUC7uuq1ZZer\n" +
          "o2SGolaVFqQBLmZT26DJ0pvA7HaZyFB2hyblSY6YsrxFxYAzUm2Yp6mrUvhuH8PR\n" +
          "40dN5myfk0BCWXuiKpiEQQLCzJPaEqFHebwok1wVEZM0eLRMo8YiAqt1GvXijK+5\n" +
          "Vd0h1XI2FQKBgH6ZYmBCg/xyjvOrV0Fhk5fekkNr2nMcVW3/p4g6PguXa+FSqmK3\n" +
          "inuzkG2y6zPfB+U04/l+8vZcn7yQ2/gs78Z8bhR3UfpqFGOvmyT5/rKfz3Ek3FyD\n" +
          "ZjUWDz1AYxcVPS0vZT2Gv+G2OmCBkTxtPcHAADKFtb1IDEvCUdc9wSs5AoGAPfFa\n" +
          "gEYbwkyQhZslHf8Sb0TQONqOdBqU03Gifz2ifBdC7isWh+IGrxaXNfEsjbMd17f6\n" +
          "Xa/gpBu+IrJZfhH6NbArvZLoXH3zD4c0PLWlenZmtFguxyIEccJsLEduRnRNF+S1\n" +
          "ms+05uX4Zf/i7vJwd6L/u+hPDl4X/w2Bx35r1q0CgYEAgqKZwPrWHWyGpT9d4B5F\n" +
          "pj1U9xupk9u8WyeW0qUT533Twmcz52mNXOl7SFCg8MRPYNa/c2hs6gLrIXnOJJHd\n" +
          "vP7NUJtXqWdz1a0ka8dLjmOuf8mouUOb+U+8tB4Vm/0ETowC3dlQ6XTwqen2ruDK\n" +
          "vsOva8jS9gHW50EwoZ04Fww=";
//  private static final String merchantId ="4884D435779D77A97A92A12D8A0B4D1E6B3EFAD0";

  public CounterServiceImpl(@Autowired CountersMapper countersMapper, UserMapper userMapper, CartMapper cartMapper, OrderMapper orderMapper, AddressMapper addressMapper, OrderDetailMapper orderDetailMapper, GoodsMapper goodsMapper, PayMapper payMapper, FeedbackMapper feedbackMapper, PromotionMapper promotionMapper, PromotionDetailMapper promotionDetailMapper, RefundResultMapper refundResultMapper) {
    this.countersMapper = countersMapper;
    this.userMapper = userMapper;
    this.cartMapper = cartMapper;
    this.orderMapper = orderMapper;
    this.addressMapper = addressMapper;
    this.orderDetailMapper = orderDetailMapper;
    this.goodsMapper = goodsMapper;
    this.payMapper = payMapper;
    this.feedbackMapper = feedbackMapper;
    this.promotionMapper = promotionMapper;
    this.promotionDetailMapper = promotionDetailMapper;
    this.refundResultMapper = refundResultMapper;
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
  public User getUser(String userId) {
    User user = userMapper.getUser(userId);
    return user;
  }

  @Override
  public void createUser(User user) {
    userMapper.createUser(user);
  }

  @Override
  public void updateUser(User user) {
    userMapper.updateUser(user);
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
  public void deleteCart(String userID, String goodsID, int specId) {
    cartMapper.deleteCart(userID, goodsID, specId);
  }

  @Override
  public void deleteCarts(Map<String, String[]> map) {
    cartMapper.deleteCarts(map);
  }

  @Override
  public List<Cart> queryCart(String userID) {
    List<Cart> carts = cartMapper.queryCart(userID);
    return carts;
  }

  @Override
  public Cart queryCartByID(String userID, int goodsID, int specId) {
    return cartMapper.queryCartByID(userID, goodsID, specId);
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
    deleteOrderDetailByOrderId(orderID);
  }

  @Override
  public void deleteOrderDetailByOrderId(String orderId) {
    orderDetailMapper.deleteByOrderId(orderId);
  }

  @Override
  public List<Order> queryOrderByUserID(String userID, int page,int pageSize) {
    int startIndex = (page-1) * pageSize;
    return orderMapper.queryOrderByUserId(userID, startIndex, pageSize);
  }

  @Override
  public List<Order> queryOrderByStatus(String userID, int status,int page, int pageSize) {
    int startIndex = (page-1) * pageSize;
    return orderMapper.queryOrderByStatus(userID, status,startIndex, pageSize);
  }

  @Override
  public List<Order> queryOrdersByStatus(String userID, List<Integer> statusList, int page, int pageSize) {
    return orderMapper.queryOrdersByStatus(userID,statusList,page,pageSize);
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
  public List<Goods> queryGoodsByCondition(int page, int pageSize, String keyword) {
    int startIndex = (page-1) * pageSize;
    return goodsMapper.queryGoodsByCondition(startIndex, pageSize, keyword);
  }

  @Override
  public List<Goods> queryGoodsByType(int page, int pageSize, int category) {
    int startIndex = (page-1) * pageSize;
    return goodsMapper.queryGoodsByType(startIndex,pageSize,category);
  }

  @Override
  public List<OrderDetail> getOrderDetails(String orderId) {
    return orderDetailMapper.getOrderDetails(orderId);
  }

  @Override
  public Map<String, Object> getOrderInfo(List<Order> orders,int page, int pageSize) {
    Map<String, Object> result = new HashMap<>();
    result.put("pageNum",page);
    result.put("pageSize", pageSize);
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

        //int price = (int)(goods.getPrice() * 100);
        int totalPrice = (int)(detail.getTotalPrice() * 100);
        int specId = detail.getSpecId();
        String specStr = goods.getSpecList();
        JSONArray specArr = JSON.parseArray(specStr);
        JSONObject specObj = specArr.getJSONObject(specId);
        List<String> specList = new ArrayList<>();
        List<Map<String, String>> specificationList = new ArrayList<>();
        int price = (int) (specObj.getDoubleValue("specPrice") * 100);
        specList.add(specObj.getString("spec"));

        Map<String ,String> specMap = new HashMap<>();
        specMap.put("specTitle","规格");
        specMap.put("specValue",specObj.getString("spec"));
        specificationList.add(specMap);

        goodDetail.put("id", detail.getId());
        goodDetail.put("orderNo", null);
        goodDetail.put("spuId", goods.getId());
        goodDetail.put("skuId", "135696670");

        goodDetail.put("roomId", null);
        goodDetail.put("goodsMainType", 0);
        goodDetail.put("goodsViceType", 0);
        goodDetail.put("goodsName", goods.getTitle());

        goodDetail.put("specifications", specificationList);
        goodDetail.put("specs",specList);
        String path = goods.getPath().split(",")[0];

        goodDetail.put("goodsPictureUrl", path);
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

    Map<String, Object> confirmButton = new HashMap<>();
    confirmButton.put("primary",true);
    confirmButton.put("type",3);
    confirmButton.put("name","确认收货");

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
    }else if(status == 3) {
      orderStatus = 60;
      statusName = "已完成";
      remark = "";
    }else if(status == 4) {
      orderStatus = 80;
      statusName = "退款待审核";
      remark = "";
    }else if(status == 5) {
      orderStatus = 80;
      statusName = "退款被拒绝，请联系客服";
      remark = "";
    }else if(status == 6) {
      orderStatus = 80;
      statusName = "已退款";
      remark = "";
    }


    String cancelTime = Long.toString(LocalDateTime.ofInstant(order.getCreateTime().toInstant(), ZoneId.systemDefault()).plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli());
    Map<String, Object> map = new HashMap<>();
    map.put("saasId","8888888");
    map.put("storeId","1000");
    map.put("storeName","鲜一冻品");
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
    //物流信息节点
    map.put("trajectoryVos", null);
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
    JSONObject result = JSONObject.parseObject(notifyData);
    String state = result.getString("trade_state");
    if("SUCCESS".equals(state)) {
      PayResult payResult = JSONObject.parseObject(notifyData, PayResult.class);
      Order order = queryOrderByID(payResult.getOutTradeNo());
      order.setStatus(1);
      updateOrder(order);
      JSONObject payJson = result.getJSONObject("payer");
      String openId = payJson.getString("openid");
      JSONObject amountJson = result.getJSONObject("amount");
      int total = amountJson.getInteger("total");
      int payerTotal = amountJson.getInteger("payer_total");
      payResult.setOpenid(openId);
      payResult.setTotalFee(Util.getRealFee(total,100));
      payResult.setPayerTotal(Util.getRealFee(payerTotal,100));
      String time = payResult.getSuccessTime();
      payResult.setSuccessTime(Util.getTimeStamp(time));
      payMapper.createPayResult(payResult);

      List<Promotion> promotionList = payResult.getPromotionList();
      if(promotionList!= null){
        for (Promotion promotion: promotionList
             ) {
          promotionMapper.newPromotion(promotion);
        }
      }
//      String orderId = result.getString("out_trade_no");
//      Order order = queryOrderByID(orderId);
//      order.setStatus(1);
//      updateOrder(order);
//      JSONObject amountJson = result.getJSONObject("amount");
//      //double total = Util.getRealFee(amountJson.getInteger("payer_total"), 100);
//      int total = amountJson.getInteger("payer_total");
//      String bank = result.getString("bank_type");
//      JSONArray array = result.getJSONArray("promotion_detail");
//      if(array != null){
//        for(int i=0;i<array.size();i++) {
//          JSONObject promotion = array.getJSONObject(i);
//          //double amount = Util.getRealFee(promotion.getInteger("amount"), 100);
//          int amount = promotion.getInteger("amount");
//          String coupon_id = promotion.getString("coupon_id");
//          String name = promotion.getString("name");
//        }
//      }
//      String trade_type = result.getString("trade_type");
//      String transaction_id = result.getString("transaction_id");
//      String success_time = result.getString("success_time");
//      LocalDateTime localDateTime = LocalDateTime.parse(success_time, DateTimeFormatter.ISO_DATE_TIME);
//      JSONObject payerObj = result.getJSONObject("payer");
//      String useId = payerObj.getString("openid");

//      PayResult payResult = new PayResult(bank, total, orderId, trade_type, transaction_id,localDateTime, useId);
//
//      payMapper.createPayResult(payResult);
    }


    //如果通知数据是xml类型的使用这个
//    System.out.println(notifyData);
//    Document document = null;
//    try {
//      document = DocumentHelper.parseText(notifyData);
//      Element root = document.getRootElement();
//      Iterator<Element> iterator = root.elementIterator();
//      Map<String, String> map = new HashMap<>();
//      while(iterator.hasNext()) {
//        Element element = iterator.next();
//        String key = element.getName();
//        String value = element.getText();
//        map.put(key, value);
//      }
//      if("SUCCESS".equals(map.get("result_code"))) {
//        String orderId = map.get("out_trade_no");
//        Order order = queryOrderByID(orderId);
//        order.setStatus(1);
//        updateOrder(order);
//      }
//    } catch (DocumentException e) {
//      e.printStackTrace();
//    }
  }

  @Override
  public void refundNotify(String notifyData) {
    JSONObject result = JSONObject.parseObject(notifyData);
    String state = result.getString("status");
    if("SUCCESS".equals(state)) {
      //PayResult payResult = JSONObject.parseObject(notifyData, PayResult.class);
      RefundResult refundResult = JSONObject.parseObject(notifyData, RefundResult.class);
      Amount amount = refundResult.getAmount();
      refundResult.setTotal(Util.getDefaultRealFee(amount.getTotal()));
      refundResult.setRefund(Util.getDefaultRealFee(amount.getRefund()));
      refundResult.setPayerTotal(Util.getDefaultRealFee(amount.getPayerTotal()));
      refundResult.setPayerRefund(Util.getDefaultRealFee(amount.getPayerRefund()));
      refundResult.setSettlementRefund(Util.getDefaultRealFee(amount.getSettlementRefund()));
      refundResult.setSettlementTotal(Util.getDefaultRealFee(amount.getSettlementTotal()));
      refundResult.setDiscountRefund(Util.getDefaultRealFee(amount.getDiscountRefund()));
      refundResult.setCurrency(amount.getCurrency());
      refundResult.setCreateTime(Util.getTimeStamp(refundResult.getCreateTime()));
      refundResult.setSuccessTime(Util.getTimeStamp(refundResult.getSuccessTime()));
      String refundId = refundResult.getRefundID();
      refundResultMapper.createRefundPayResult(refundResult);
      PromotionDetail[] promotionDetails = refundResult.getPromotionDetail();
      if(promotionDetails != null) {
        for (PromotionDetail promotionDetail: promotionDetails
           ) {
          promotionDetail.setRefundId(refundId);
          promotionDetail.setAmount(Util.getDefaultRealFee((int)promotionDetail.getAmount()));
          promotionDetail.setRefundAmount(Util.getDefaultRealFee((int)promotionDetail.getRefundAmount()));
          promotionDetailMapper.newPromotionDetail(promotionDetail);
        }
      }
//      Order order = queryOrderByID(payResult.getOutTradeNo());
//      order.setStatus(1);
//      updateOrder(order);
//      JSONObject payJson = result.getJSONObject("payer");
//      String openId = payJson.getString("openid");
//      JSONObject amountJson = result.getJSONObject("amount");
//      int total = amountJson.getInteger("total");
//      int payerTotal = amountJson.getInteger("payer_total");
//      payResult.setOpenid(openId);
//      payResult.setTotalFee(Util.getRealFee(total, 100));
//      payResult.setPayerTotal(Util.getRealFee(payerTotal, 100));
//      String time = payResult.getSuccessTime();
//      ZonedDateTime dateTime = ZonedDateTime.parse(time);
//      payResult.setSuccessTime(String.valueOf(dateTime.toInstant().toEpochMilli()));
//      payMapper.createPayResult(payResult);
//
//      List<Promotion> promotionList = payResult.getPromotionList();
//      if (promotionList != null) {
//        for (Promotion promotion : promotionList
//        ) {
//          promotionMapper.newPromotion(promotion);
//        }
//      }
    }
  }

  public void getCerfification() {
    try{
      URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/certificates");
      HttpGet httpGet = new HttpGet(uriBuilder.build());
      httpGet.addHeader("Accept", "application/json");
      WechatPayHttpClientBuilder builder = getBuild();
      CloseableHttpClient httpClient = builder.build();

      CloseableHttpResponse response = httpClient.execute(httpGet);

      String bodyAsString = EntityUtils.toString(response.getEntity());
    }catch (Exception e) {
      e.printStackTrace();
    }

  }


  @Override
  public String payOrder(Order order)  {
    WechatPayHttpClientBuilder builder = getBuild();


    HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
    //HttpPost httpPost = new HttpPost("http://api.weixin.qq.com/_/pay/unifiedorder");
    httpPost.addHeader("Accept", "application/json");
    httpPost.addHeader("Content-type","application/json; charset=utf-8");
    //CloseableHttpClient httpClient = HttpClients.createDefault();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    CloseableHttpClient httpClient = builder.build();
    ObjectMapper objectMapper = new ObjectMapper();


    ObjectNode rootNode = objectMapper.createObjectNode();
    int newPrice = (int) (order.getPrice() * 100);
    //int newPrice = (int) order.getPrice();
    rootNode.put("mchid",MERCHANTID)
            .put("appid", APPID)
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
    JSONObject json = JSON.parseObject(bodyAsString);
    String prepayId = "prepay_id=" + json.getString("prepay_id");
    System.out.println(prepayId);
    return prepayId;
  }


  public void refundOrder(RefundOrder refundOrder)  {
    WechatPayHttpClientBuilder builder = getBuild();


    HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
    httpPost.addHeader("Accept", "application/json");
    httpPost.addHeader("Content-type","application/json; charset=utf-8");
    //CloseableHttpClient httpClient = HttpClients.createDefault();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    CloseableHttpClient httpClient = builder.build();
    ObjectMapper objectMapper = new ObjectMapper();


    ObjectNode rootNode = objectMapper.createObjectNode();
    //int newPrice = (int) order.getPrice();
    rootNode.put("transaction_id",refundOrder.getTransactionId())
            .put("out_refund_no", refundOrder.getId())
            .put("notify_url", "https://springboot-v3w5-37027-5-1317305634.sh.run.tcloudbase.com/payNotify");
    rootNode.putObject("amount")
            .put("refund", refundOrder.getRefundFee() * 100)
            .put("total", refundOrder.getTotalFee() * 100)
            .put("currency", "CNY");

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
    JSONObject json = JSON.parseObject(bodyAsString);
    System.out.println(json);
//    String prepayId = "prepay_id=" + json.getString("prepay_id");
//    System.out.println(prepayId);
//    return prepayId;
  }
  @Override
  public void closeOrder(String orderId) {
    WechatPayHttpClientBuilder builder = getBuild();
    String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/" + orderId + "/close";
    HttpPost httpPost = new HttpPost(url);
    httpPost.addHeader("Accept", "application/json");
    httpPost.addHeader("Content-type","application/json; charset=utf-8");

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectMapper objectMapper = new ObjectMapper();
    CloseableHttpClient httpClient = builder.build();
    CloseableHttpResponse response = null;
    String bodyAsString = "";
    try {
      ObjectNode rootNode = objectMapper.createObjectNode();
      rootNode.put("mchid",MERCHANTID);
      //rootNode.put("out_trade_no",orderId);
      objectMapper.writeValue(bos, rootNode);

      httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
      response= httpClient.execute(httpPost);


//      bodyAsString = EntityUtils.toString(response.getEntity());
//      System.out.println(bodyAsString);
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
  }



  @Override
  public void refundOrder() {

  }

  @Override
  public String sign(String prepareId, String timeStamp, String nonStr) {
    String str = APPID + "\n"
            + timeStamp + "\n"
            + nonStr + "\n"
            + prepareId + "\n";
    String signStr = "";
    try{
      Signature sign = Signature.getInstance("SHA256withRSA");
      //私钥，通过getPrivateKey来获取，这是个方法可以接调用 ，需要的是_key.pem文件的绝对路径配上文件名
      //sign.initSign(getPrivateKey("F:\\download\\WXCertUtil\\cert\\1639911327_20230329_cert\\apiclient_key.pem"));
      sign.initSign(getPrivateKey("apiclient_key.pem"));
      sign.update(str.getBytes("utf-8"));
      signStr = Base64.getEncoder().encodeToString(sign.sign());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return signStr;
  }
  public  PrivateKey getPrivateKey(String filename) throws IOException {

//    File file = ResourceUtils.getFile("classpath:" + filename);
//    String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);



//    ClassPathResource classPathResource = new ClassPathResource(filename);
//    String content = IOUtils.toString(classPathResource.getInputStream(),"utf-8");
//    String content = "-----BEGIN PRIVATE KEY-----\n" +
//            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDeDbOnafXVVMzj\n" +
//            "/71xivDebiOHelu4W4pCMX3vfzuZDoL0Q/5zGeAgR1o0sRgaDCjpgMO5bpWBdPsm\n" +
//            "wOOCtfYmB+K4mgNjSeiE9Q9UebwEy9uq3ZAkNwReJNZH1f+53FhSgvMy+1rQ4ntK\n" +
//            "Ecziv0lA9EzcOzZpTvyF28VPBlGIenSmsVLRKzaSQlJaTH2Qas3nCKWddgDY1Cx2\n" +
//            "2dm/Ad1DuMIomEsqfjCZgIQTvup3N83w/lSKbuHmWJ175ce+KFAfXBQ5EKeNK39O\n" +
//            "i4kgP2115m+Crro3aDXe9PUVwkqBu7AfvRXEQ5tXt5YuFKiURKFX9nO/ah7AzGRe\n" +
//            "XBIB4ig5AgMBAAECggEAG6vrcnZy6zXZHqSVEBw3bhCdntlxqqTFswAF6J2baLR1\n" +
//            "P0ll4SQdWQhrRlu3XC+dvZONUINmYC6aybaJ45UXap/a8hRHTc09C6yaT3WoJ5Tb\n" +
//            "+AwKVWkBw1Wl2mfhqWC7JPUqp3TJWXSP1qgnNy6NS2nmVh+O5Uqxj2DW0sU/zdjx\n" +
//            "S+XkUYG4J9ONtVTPu0fdQXilBfCVL9CHW0yH9n5GY32OcCKkZCTmwEnBHF+tM4a9\n" +
//            "VsVEj/wbaO88abOvlSkXIdrKeIlbVI+6bpweUx9ktp4QjNN7/HAlYxKSAqemkSbZ\n" +
//            "vZUCknTr9CC7xg0zdE+SSrRkKs7mfkNWTAq39lTu8QKBgQD45yPgpHFYuMXB/sNR\n" +
//            "xBrMZIe7ydso1QMqqYV1n2+Kcrub57LfzjaNQ3IGUrv2bsW2N9OBhSBABBoPJjQj\n" +
//            "hMDh8VILAh+ALVUm/4urj6gd2QhwlsBn89sj9Yb12CjaS2MqWd9xzMVYSdHB8kgk\n" +
//            "+XVLUnNPKcPX/IKWLSt27U92lQKBgQDkYpNNBIIlBLirCUzEGpBfgUC7uuq1ZZer\n" +
//            "o2SGolaVFqQBLmZT26DJ0pvA7HaZyFB2hyblSY6YsrxFxYAzUm2Yp6mrUvhuH8PR\n" +
//            "40dN5myfk0BCWXuiKpiEQQLCzJPaEqFHebwok1wVEZM0eLRMo8YiAqt1GvXijK+5\n" +
//            "Vd0h1XI2FQKBgH6ZYmBCg/xyjvOrV0Fhk5fekkNr2nMcVW3/p4g6PguXa+FSqmK3\n" +
//            "inuzkG2y6zPfB+U04/l+8vZcn7yQ2/gs78Z8bhR3UfpqFGOvmyT5/rKfz3Ek3FyD\n" +
//            "ZjUWDz1AYxcVPS0vZT2Gv+G2OmCBkTxtPcHAADKFtb1IDEvCUdc9wSs5AoGAPfFa\n" +
//            "gEYbwkyQhZslHf8Sb0TQONqOdBqU03Gifz2ifBdC7isWh+IGrxaXNfEsjbMd17f6\n" +
//            "Xa/gpBu+IrJZfhH6NbArvZLoXH3zD4c0PLWlenZmtFguxyIEccJsLEduRnRNF+S1\n" +
//            "ms+05uX4Zf/i7vJwd6L/u+hPDl4X/w2Bx35r1q0CgYEAgqKZwPrWHWyGpT9d4B5F\n" +
//            "pj1U9xupk9u8WyeW0qUT533Twmcz52mNXOl7SFCg8MRPYNa/c2hs6gLrIXnOJJHd\n" +
//            "vP7NUJtXqWdz1a0ka8dLjmOuf8mouUOb+U+8tB4Vm/0ETowC3dlQ6XTwqen2ruDK\n" +
//            "vsOva8jS9gHW50EwoZ04Fww=\n" +
//            "-----END PRIVATE KEY-----\n";

      //String content = FileCopyUtils.copyToString(new InputStreamReader(CounterServiceImpl.class.getClassLoader().getResourceAsStream(filename)));
    //content = FileCopyUtils.copyToString(new InputStreamReader(CounterServiceImpl.class.getClassLoader().getResourceAsStream(filename)));
    //String content = new String(Files.readAllBytes(Paths.get(filename)), "utf-8");

    try {
      String privateKey = MERCHANTPRIVATEKEY.replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");

      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePrivate(
              new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("当前Java环境不支持RSA", e);
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException("无效的密钥格式");
    }
  }


  @Override
  public String getSign(String prepareId, String timeStamp, String nonStr) {

    String str = APPID + "\n" + timeStamp + "\n" + nonStr + "\n" + prepareId  + "\n";
    //byte[] privateKeyBytes = Base64.getDecoder().decode(MERCHANTPRIVATEKEY);
    byte[] privateKeyBytes = "yZwjC7OH9DK3jQzOAf9D3RwHqwfoGmDz".getBytes(StandardCharsets.UTF_8);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory keyFactory = null;
    PrivateKey privateKey = null;
    Signature signature = null;
    byte[] signatureBytes = null;
    String sign = null;
    try{
      keyFactory= KeyFactory.getInstance("RSA");
      privateKey = keyFactory.generatePrivate(keySpec);
      signature= Signature.getInstance("SHA256withRSA");
      signature.initSign(privateKey);
      signature.update(str.getBytes("UTF-8"));
      signatureBytes = signature.sign();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (SignatureException e) {
      e.printStackTrace();
    }
    if(signatureBytes != null) {
      sign = Base64.getEncoder().encodeToString(signatureBytes);
    }
    return sign;
  }

  @Override
  public void newFeedback(Feedback feedback) {
    feedbackMapper.createFeedback(feedback);
  }

  @Override
  public List<Feedback> queryFeedbackByUser(String userId) {
    return feedbackMapper.queryFeedbackByUser(userId);
  }

  @Override
  public Feedback queryFeedbackById(String userId, int id) {
    return feedbackMapper.queryFeedBackById(userId, id);
  }

  @Override
  public PayResult getResult(String id) {
    return payMapper.getResult(id);
  }

  @Override
  public void insertResult(PayResult payResult) {
    payMapper.createPayResult(payResult);
  }

  @Override
  public Map<String, String> getPayment(Order order) {
    String prepay_id = payOrder(order);
    String nonStr = Util.generateRandomString(32);
    String timeStamp = Util.getTimeStamp();
    //String sign = counterService.getSign(prepay_id, timeStamp, nonStr);
    String sign = sign(prepay_id, timeStamp, nonStr);
    Map<String, String> map = new HashMap<>();
    map.put("appId", "wx7874f23b30f30672");
    map.put("nonceStr",nonStr);
    map.put("package",prepay_id);
    map.put("paySign",sign);
    map.put("signType","RSA");
    map.put("timeStamp",timeStamp);

    return map;
  }

  public WechatPayHttpClientBuilder getBuild() {
    PrivateKey merchantPrivateKey = null;
    CertificatesManager certificatesManager = null;
    Verifier verifier = null;
    try{
      merchantPrivateKey = PemUtil.loadPrivateKey(
              new ByteArrayInputStream(MERCHANTPRIVATEKEY.getBytes("utf-8")));
      certificatesManager = CertificatesManager.getInstance();
// 向证书管理器增加需要自动更新平台证书的商户信息
      certificatesManager.putMerchant(MERCHANTID, new WechatPay2Credentials(MERCHANTID,
              new PrivateKeySigner(MERCHANTSERIALNUMBER, merchantPrivateKey)), "yZwjC7OH9DK3jQzOAf9D3RwHqwfoGmDz".getBytes(StandardCharsets.UTF_8));
      verifier = certificatesManager.getVerifier(MERCHANTID);
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (HttpCodeException e) {
      e.printStackTrace();
    } catch (NotFoundException e) {
      e.printStackTrace();
    }

    WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
            .withMerchant(MERCHANTID, MERCHANTSERIALNUMBER, merchantPrivateKey)
            .withValidator(new WechatPay2Validator(verifier));
    return builder;
  }

  @Override
  public JSONObject getOpenId(String code) {
    String openId = "";
    HttpClient httpClient = new HttpClient();
    System.out.println("code is " + code);
    PostMethod postMethod = new PostMethod("https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID + "&secret=" + SECRET + "&js_code=" + code + "&grant_type=authorization_code");

    postMethod.addRequestHeader("accept", "*/*");
    //设置Content-Type，此处根据实际情况确定
    postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    String result = "";
    JSONObject data = new JSONObject();
    try {
      int status = httpClient.executeMethod(postMethod);
      if (status == 200){
        result = postMethod.getResponseBodyAsString();
        System.out.println("result:" + result);
        JSONObject json = JSON.parseObject(result);
        System.out.println(json);
        Integer errCode = json.getInteger("errcode");
        if(errCode != null) {
          String errMsg = json.getString("errmsg");
        }
        //data = json.getJSONObject("data");
        data = json;
        openId = data.getString("openid");
        //String sessionKey = data.getString("session_key");


      }else{
        result = postMethod.getResponseBodyAsString();
        System.out.println("发送请求失败，错误码为:" + status);
        System.out.println(result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public User getUserInfo(String code) {
    getPhoneNumber(code);

//    HttpClient httpClient = new HttpClient();
//    PostMethod postMethod = new PostMethod("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + getToken());
//
//    postMethod.addRequestHeader("accept", "*/*");
//    //设置Content-Type，此处根据实际情况确定
//    postMethod.addRequestHeader("Content-Type", "application/json");
//    String result = "";
//    JSONObject data = new JSONObject();
//    HashMap<String, String> map = new HashMap<>();
//
//    map.put("code",code);
//
//    try {
//      postMethod.setRequestBody(String.valueOf(new StringRequestEntity(JSON.toJSONString(map), "application/json", "UTF-8")));
//      //postMethod.setRequestBody(String.valueOf(new StringRequestEntity(JSON.toJSONString(map))));
//      int status = httpClient.executeMethod(postMethod);
//      if (status == 200){
//        result = postMethod.getResponseBodyAsString();
//        System.out.println("result:" + result);
//        JSONObject json = JSON.parseObject(result);
//        System.out.println(json);
//        Integer errCode = json.getInteger("errcode");
//        if(errCode != null) {
//          String errMsg = json.getString("errmsg");
//        }
//        //data = json.getJSONObject("data");
//        data = json.getJSONObject("data");
//        System.out.println(data);
//        //String sessionKey = data.getString("session_key");
//
//
//      }else{
//        result = postMethod.getResponseBodyAsString();
//        System.out.println("发送请求失败，错误码为:" + status);
//        System.out.println(result);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    return null;
  }

  public String getToken() {
    HttpClient httpClient = new HttpClient();
    GetMethod getMethod = new GetMethod("https://api.weixin.qq.com/cgi-bin/token?appid=" + APPID + "&secret=" + SECRET  + "&grant_type=client_credential");

    getMethod.addRequestHeader("accept", "*/*");
    //设置Content-Type，此处根据实际情况确定
    getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    String result = "";
    JSONObject data = new JSONObject();
    try {
      int status = httpClient.executeMethod(getMethod);
      if (status == 200){
        result = getMethod.getResponseBodyAsString();
        System.out.println("result:" + result);
        JSONObject json = JSON.parseObject(result);
        System.out.println(json);
        Integer errCode = json.getInteger("errcode");
        if(errCode != null) {
          String errMsg = json.getString("errmsg");
        }
        //data = json.getJSONObject("data");
        data = json;
        token = data.getString("access_token");
        //String sessionKey = data.getString("session_key");


      }else{
        result = getMethod.getResponseBodyAsString();
        System.out.println("发送请求失败，错误码为:" + status);
        System.out.println(result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return token;
  }

  public JSONObject getPhoneNumber(String code) {
    JSONObject phone = null;
    // 获取token
    //String token_url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", APPID, SECRET);
    try {

      //获取phone
      String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber"
              + "?access_token=" + getToken();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("code", code);
      String reqJsonStr = JSON.toJSONString(jsonObject);
      phone = JSON.parseObject(HttpClientSslUtils.doPost(url, reqJsonStr));

      if (phone == null) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return phone;
  }

  public JSONObject getTrace_waybill() {
    JSONObject phone = null;
    // 获取token
    //String token_url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", APPID, SECRET);
    try {

      //获取phone
      String url = "https://api.weixin.qq.com/cgi-bin/express/delivery/open_msg/trace_waybill"
              + "?access_token=" + getToken();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("openid", "");
      jsonObject.put("receiver_phone", "");
      jsonObject.put("waybill_id", "");
      jsonObject.put("goods_info", "");
      jsonObject.put("trans_id", "");
      String reqJsonStr = JSON.toJSONString(jsonObject);
      phone = JSON.parseObject(HttpClientSslUtils.doPost(url, reqJsonStr));

      if (phone == null) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return phone;
  }
}

