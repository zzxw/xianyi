package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.util.OrderUtil;
import com.tencent.wxcloudrun.util.Util;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CounterRequest;
import com.tencent.wxcloudrun.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * counter控制器
 */
@RestController

public class CounterController {

  final CounterService counterService;
  final Logger logger;

  public CounterController(@Autowired CounterService counterService) {
    this.counterService = counterService;
    this.logger = LoggerFactory.getLogger(CounterController.class);
  }


  /**
   * 获取当前计数
   * @return API response json
   */
  @GetMapping(value = "/api/count")
  ApiResponse get() {
    logger.info("/api/count get request");
    Optional<Counter> counter = counterService.getCounter(1);
    Integer count = 0;
    if (counter.isPresent()) {
      count = counter.get().getCount();
    }

    return ApiResponse.ok(count);
  }

  @GetMapping(value = "/getUserByCode")
  ApiResponse createUser(@RequestParam String code) {
    logger.info("/getUser get request");

    //String openId = request.getHeader("x-wx-openid");
//    if(!counterService.hasUser(openId)) {
//      User user = new User();
//      user.setUserId(openId);
//      user.setPhoneNumber(phoneNumber);
//      counterService.createUser(user);
//    }
    counterService.getUserInfo(code);
    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/getUser")
  ApiResponse getUser(@RequestParam String code) {
    logger.info("/getUser get request");


    JSONObject data = counterService.getOpenId(code);

    String openId = data.getString("openid");
    User user = counterService.getUser(openId);
    if(user != null) {
      String phoneNumber = user.getPhoneNumber();
      data.put("phoneNumber",  phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7));
//      User user = new User();
//      user.setUserId(openId);
//      counterService.createUser(user);
    }
    return ApiResponse.ok(data);

  }

  @GetMapping(value = "/getUserInfo")
  ApiResponse getUserInfo(@RequestParam String code, @RequestParam String phoneCode) {
    logger.info("/getUserInfo get request");


    JSONObject data = counterService.getOpenId(code);
    JSONObject phoneInfo = counterService.getPhoneNumber(phoneCode);
    String phoneNumber = phoneInfo.getJSONObject("phone_info").getString("phoneNumber");

    String openId = data.getString("openid");
    //if(!counterService.hasUser(openId)) {
    User user = new User();
    user.setUserId(openId);
    user.setPhoneNumber(phoneNumber);
    counterService.createUser(user);
    //}

    data.put("phoneNumber",  phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7));
    return ApiResponse.ok(data);
//    HttpClient httpClient = new HttpClient();
//    System.out.println("code is " + code);
//    PostMethod postMethod = new PostMethod("https://api.weixin.qq.com/sns/jscode2session?appid=wx7874f23b30f30672&secret=7c88c65b513292819127ef927921d708&js_code=" + code + "&grant_type=authorization_code");
//
//    postMethod.addRequestHeader("accept", "*/*");
//    //设置Content-Type，此处根据实际情况确定
//    postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//    String result = "";
//    JSONObject data = new JSONObject();
//    try {
//      int status = httpClient.executeMethod(postMethod);
//      if (status == 200){
//        result = postMethod.getResponseBodyAsString();
//        System.out.println("result:" + result);
//        JSONObject json = JSON.parseObject(result);
//        System.out.println(json);
//        Integer errCode = json.getInteger("errcode");
//        if(errCode != null) {
//          String errMsg = json.getString("errmsg");
//          return ApiResponse.error(errCode, errMsg, json);
//        }
//        //data = json.getJSONObject("data");
//        data = json;
//        String openId = data.getString("openid");
//        //String sessionKey = data.getString("session_key");
//
//        if(!counterService.hasUser(openId)) {
//          User user = new User();
//          user.setUserId(openId);
//          counterService.createUser(user);
//        }
//
//      }else{
//        result = postMethod.getResponseBodyAsString();
//        System.out.println("发送请求失败，错误码为:" + status);
//        System.out.println(result);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

  }

  @GetMapping(value = "/getCart")
  ApiResponse getCart(@RequestParam String userId) {
    logger.info("/getCart get request");

    List<Cart> list = counterService.queryCart(userId);
    HashMap<String,Object>result = new HashMap<>();
    for (Cart cart:list
         ) {
      int goodsId = cart.getGoodsID();
      Goods goods = counterService.queryGoodsDetail(goodsId);

      result.put(String.valueOf(goodsId), goods);
    }
    result.put("cartList", list);
    return ApiResponse.ok(result);
  }

  @PostMapping(value = "/newCart")
  //ApiResponse newCart(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price) {
  ApiResponse newCart(@RequestBody Cart cart) {
    logger.info("/newCart post request");

//    Cart cart = new Cart();
//    cart.setUserId(userId);
//    cart.setGoodsID(goodsID);
//    cart.setPrice(price);
//    cart.setNum(num);
//    counterService.createCart(cart);
    List<Spec> list = cart.getSpecList();
    for (Spec spec: list
    ) {
      Cart item = new Cart();
      item.setSpecId(spec.getSpecId());
      item.setPrice(spec.getPrice());
      item.setNum(spec.getBuyNum());
      item.setUserId(cart.getUserId());
      item.setGoodsID(cart.getGoodsID());
      Cart oldCart = counterService.queryCartByID(cart.getUserId(), item.getGoodsID(), item.getSpecId());
      if(oldCart != null) {
        item.setNum(item.getNum() + oldCart.getNum());
        counterService.updateCart(item);
      }else{
        //cart.setNum(num);
        counterService.createCart(item);
      }
    }

    return ApiResponse.ok(0);
  }

  @PostMapping(value = "/updateCart")
  ApiResponse updateCart(@RequestBody Cart cart) {
    logger.info("/updateCart post request");

//    Cart cart = new Cart();
//    cart.setUserId(userId);
//    cart.setGoodsID(goodsID);
//    cart.setPrice(price);
//    cart.setNum(num);

    List<Spec> list = cart.getSpecList();
    if(list!= null) {
      for (Spec spec: list
      ) {
        Cart item = new Cart();
        item.setSpecId(spec.getSpecId());
        item.setPrice(spec.getPrice());
        item.setNum(spec.getBuyNum());
        item.setUserId(cart.getUserId());
        item.setGoodsID(cart.getGoodsID());
        counterService.updateCart(item);
      }
    }else {
      counterService.updateCart(cart);
    }

    //counterService.updateCart(cart);

    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/deleteCart")
  ApiResponse deleteCart(@RequestParam String userId, @RequestParam String goodsID, @RequestParam String specId) {
    logger.info("/deleteCart get request");

    int index = goodsID.indexOf(",");
    if(index < 0) {
      counterService.deleteCart(userId, goodsID, Integer.valueOf(specId));
    }else {
      String []goodsIDs = goodsID.split(",");
      String []specIds = specId.split(",");
      for(int i=0;i< goodsIDs.length;i++) {
        counterService.deleteCart(userId, goodsIDs[i], Integer.valueOf(specIds[i]));
      }
//      Map<String, String[]> map = new HashMap<>();
//      map.put("userId", new String[]{userId});
//      map.put("goodsID", goodsIDs);
//      map.put("specIds", specIds);
//      counterService.deleteCarts(map);
    }

    return ApiResponse.ok(0);
  }

  //使用deleteCart替代该方法
  @GetMapping(value = "/deleteCarts")
  ApiResponse deleteCarts(@RequestParam String userId, @RequestParam String goodsIDs) {
    logger.info("/deleteCarts get request");

    //counterService.deleteCarts(userId, goodsIDs);

    return ApiResponse.ok(0);
  }


  @GetMapping(value = "/getOrderByUserId")
  ApiResponse getOrderByUserId(@RequestParam String userId, @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
    logger.info("/getOrderByUserId get request");

    List<Order> list = counterService.queryOrderByUserID(userId,page, pageSize);
    for(Order order: list) {
      order.setList(counterService.getOrderDetails(order.getOrderID()));
    }
    Map<String, Object> map = counterService.getOrderInfo(list, page, pageSize);
    return ApiResponse.ok(map);
  }

  @GetMapping(value = "/getOrderByStatus")
  ApiResponse getOrderByStatus(@RequestParam String userId, @RequestParam int status, @RequestParam(name = "page", defaultValue = "1") int page,
                               @RequestParam(name = "pageSize", defaultValue = "10")int pageSize) {
    logger.info("/getOrderByUserId get request");

    List<Order> list = counterService.queryOrderByStatus(userId,status,page, pageSize);
    for(Order order: list) {
      order.setList(counterService.getOrderDetails(order.getOrderID()));
    }
    Map<String, Object> map = counterService.getOrderInfo(list, page, pageSize);
    return ApiResponse.ok(map);
  }

  @GetMapping(value = "/getOrderCount")
  ApiResponse getOrderCount(@RequestParam String userId) {
    logger.info("/getOrderCount get request");

    List<Map<String, Integer>> list = new ArrayList<>();
    int preCount = counterService.selectCountByStatus(userId, 0);
    int waitCount = counterService.selectCountByStatus(userId, 1);
    int ingCount = counterService.selectCountByStatus(userId, 2);
    int finshCount = counterService.selectCountByStatus(userId, 3);
    int tuiCount = counterService.selectCountByStatus(userId, 4);
    Map<String, Integer> preMap = new HashMap<>();
    preMap.put("orderNum",preCount);
    preMap.put("tabType",5);
    list.add(preMap);

    Map<String, Integer> waitMap = new HashMap<>();
    waitMap.put("orderNum",waitCount);
    waitMap.put("tabType",10);
    list.add(waitMap);

    Map<String, Integer> ingMap = new HashMap<>();
    ingMap.put("orderNum",ingCount);
    ingMap.put("tabType",40);
    list.add(ingMap);

    Map<String, Integer> finshMap = new HashMap<>();
    finshMap.put("orderNum",finshCount);
    finshMap.put("tabType", 60);
    list.add(finshMap);

    Map<String, Integer> tuiMap = new HashMap<>();
    tuiMap.put("orderNum",tuiCount);
    tuiMap.put("tabType", 80);
    list.add(tuiMap);
    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/getOrderById")
  ApiResponse getOrderById(@RequestParam String orderId) {
    logger.info("/getOrderById get request");

    Order order = counterService.queryOrderByID(orderId);
    List<OrderDetail> list = counterService.getOrderDetails(orderId);
    order.setList(list);
    List<Order> orderList = new ArrayList<>();
    orderList.add(order);
    Map<String, Object> map = counterService.getOrderInfo(orderList, 1, 10);
    return ApiResponse.ok(map);
  }

  @PostMapping(value = "/newOrder")
  //ApiResponse newOrder(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price, @RequestParam int status) {
  ApiResponse newOrder(@RequestBody Order order) {
    logger.info("/newOrder post request");

//    Order order = new Order();
//    order.setGoodsID(goodsID);
//    order.setUserID(userId);
//    order.setNum(num);
//    order.setPrice(price);
//    order.setStatus(status);

    String orderID = OrderUtil.getIDByTime();
    order.setOrderID(orderID);

    counterService.createOrder(order);
    List<OrderDetail> list = order.getList();
    int i=0;
    for(OrderDetail detail:list) {
      if(i<10){
        detail.setId(orderID + "0" + i);
      }else {
        detail.setId(orderID + i);
      }
      i++;
      detail.setOrderId(orderID);
      counterService.newOrderDetail(detail);
    }
    String prepay_id = counterService.payOrder(order);
    String nonStr = Util.generateRandomString(32);
    String timeStamp = Util.getTimeStamp();
    //String sign = counterService.getSign(prepay_id, timeStamp, nonStr);
    String sign = counterService.sign(prepay_id, timeStamp, nonStr);
    Map<String, String> map = new HashMap<>();
    map.put("appId", "wx7874f23b30f30672");
    map.put("nonceStr",nonStr);
    map.put("package",prepay_id);
    map.put("paySign",sign);
    map.put("signType","RSA");
    map.put("timeStamp",timeStamp);
    map.put("orderId", orderID);
    return ApiResponse.ok(map);
  }

  @GetMapping(value = "/closeOrderPay")
    //ApiResponse newOrder(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price, @RequestParam int status) {
  ApiResponse closeOrder(@RequestParam String orderId) {
    logger.info("/closeOrderPay post request");

    counterService.closeOrder(orderId);
//    Order order = new Order();
//    order.setGoodsID(goodsID);
//    order.setUserID(userId);
//    order.setNum(num);
//    order.setPrice(price);
//    order.setStatus(status);


    return ApiResponse.ok(0);
  }

  @PostMapping(value = "/updateOrder")
 // ApiResponse updateOrder(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price, @RequestParam int status, @RequestParam String orderID) {
  ApiResponse updateOrder(@RequestBody Order order) {
    logger.info("/updateOrder post request");

//    Order order = new Order();
//    order.setGoodsID(goodsID);
//    order.setUserID(userId);
//    order.setNum(num);
//    order.setPrice(price);
//    order.setStatus(status);
//    order.setOrderID(orderID);


    counterService.updateOrder(order);

    return ApiResponse.ok(0);
  }

  @PostMapping(value = "/updateOrderStatus")
    // ApiResponse updateOrder(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price, @RequestParam int status, @RequestParam String orderID) {
  ApiResponse updateOrderStatus(@RequestBody Order order) {
    logger.info("/updateOrder post request");

//    Order order = new Order();
//    order.setGoodsID(goodsID);
//    order.setUserID(userId);
//    order.setNum(num);
//    order.setPrice(price);
//    order.setStatus(status);
//    order.setOrderID(orderID);

    counterService.updateOrderStatus(order);
    //counterService.updateOrder(order);

    return ApiResponse.ok(0);
  }
  @GetMapping(value = "/deleteOrder")
  ApiResponse deleteOrder(@RequestParam String orderId) {
    logger.info("/deleteOrder get request");

    //counterService.deleteOrder(orderId);
    Order order = new Order();
    order.setOrderID(orderId);
    order.setStatus(-1);
    counterService.updateOrderStatus(order);

    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/getAddressByUserId")
  ApiResponse getAddressByUserId(@RequestParam String userId) {
    logger.info("/getAddressByUserId get request");

    List<Address> list = counterService.queryAddressByUser(userId);

    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/getAddress")
  ApiResponse getAddress(@RequestParam String userId, @RequestParam int addressNo) {
    logger.info("/getAddress get request");

    Address address = counterService.queryAddressById(userId, addressNo);

    return ApiResponse.ok(address);
  }

  @GetMapping(value = "/getDefaultAddress")
  ApiResponse getDefaultAddress(@RequestParam String userId) {
    logger.info("/getAddress get request");

    List<Address> list = counterService.queryAddressByUser(userId);
    Address address = null;
    if(list.size() == 1) {
      address = list.get(0);
    }else  {
      for(Address address1 : list) {
        if(address1.getIsDefault() == 1) {
          address = address1;
          break;
        }
      }
      if(address == null && list.size() > 0) {
        address = list.get(0);
      }
    }
    //address = counterService.queryDefaultAddress(userId);

    return ApiResponse.ok(address);
  }

  @PostMapping(value = "/payNotify")
  ApiResponse notify(@RequestBody String notifyData) {
    logger.info("/payNotify get request");
    logger.info("回调通知数据为:");
    logger.info(notifyData);
    String decodeData = Util.decode(notifyData);
    counterService.notify(decodeData);
    return ApiResponse.ok(0);
  }


  @GetMapping(value = "/deleteAddress")
  ApiResponse deleteAddress(@RequestParam String userId, @RequestParam int addressNo) {
    logger.info("/deleteAddress get request");

    counterService.deleteAddress(userId, addressNo);

    return ApiResponse.ok(0);
  }

  @PostMapping(value = "/updateAddress")
//  ApiResponse updateAddress(@RequestParam String province, @RequestParam String city, @RequestParam String detail,
//                            @RequestParam int addressNo, @RequestParam String area, @RequestParam int isDefault,
//                            @RequestParam String phoneNumber, @RequestParam String userName, @RequestParam String userId) {
  ApiResponse updateAddress(@RequestBody Address address) {
    logger.info("/updateAddress post request");
//    Address address = new Address();
//    address.setAddressNo(addressNo);
//    address.setArea(area);
//    address.setCity(city);
//    address.setDetail(detail);
//    address.setProvince(province);
//    address.setIsDefault(isDefault);
//    address.setUserID(userId);
//    address.setPhoneNumber(phoneNumber);
//    address.setUserName(userName);

    if(address.getIsDefault() == 1) {
      counterService.updateDefaultAddress(address.getUserID());
    }
    counterService.updateAddress(address);
    return ApiResponse.ok(0);
  }

  @PostMapping(value = "/newAddress")
//  ApiResponse newAddress(@RequestParam String province, @RequestParam String city, @RequestParam String detail,
//                             @RequestParam String area, @RequestParam int isDefault,
//                            @RequestParam String phoneNumber, @RequestParam String userName, @RequestParam String userId) {
  ApiResponse newAddress(@RequestBody Address address) {
    logger.info("/newAddress post request");
    //Address address = new Address();
    //address.setAddressNo(addressNo);
//    address.setArea(area);
//    address.setCity(city);
//    address.setDetail(detail);
//    address.setProvince(province);
//    address.setIsDefault(isDefault);
//    address.setUserID(userId);
//    address.setPhoneNumber(phoneNumber);
//    address.setUserName(userName);

    if(address.getIsDefault() == 1) {
      counterService.updateDefaultAddress(address.getUserID());
    }
    counterService.createAddress(address);
    return ApiResponse.ok(0);
  }

  /**
   * 更新计数，自增或者清零
   * @param request {@link CounterRequest}
   * @return API response json
   */
  @PostMapping(value = "/api/count")
  ApiResponse create(@RequestBody CounterRequest request) {
    logger.info("/api/count post request, action: {}", request.getAction());

    Optional<Counter> curCounter = counterService.getCounter(1);
    if (request.getAction().equals("inc")) {
      Integer count = 1;
      if (curCounter.isPresent()) {
        count += curCounter.get().getCount();
      }
      Counter counter = new Counter();
      counter.setId(1);
      counter.setCount(count);
      counterService.upsertCount(counter);
      return ApiResponse.ok(count);
    } else if (request.getAction().equals("clear")) {
      if (!curCounter.isPresent()) {
        return ApiResponse.ok(0);
      }
      counterService.clearCount(1);
      return ApiResponse.ok(0);
    } else {
      return ApiResponse.error("参数action错误");
    }
  }

  @GetMapping(value = "/queryGoodsInfo")
  ApiResponse queryGoodsDetail(@RequestParam int id) {
    logger.info("/queryGoodsInfo get request");

    Goods goods = counterService.queryGoodsDetail(id);
    return ApiResponse.ok(goods);
  }

  @GetMapping(value = "/queryGoods")
  ApiResponse queryGoods(@RequestParam int page, @RequestParam int pageSize) {
    logger.info("/queryGoods get request");

    List<Goods> list = counterService.queryGoods(page, pageSize);
    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/queryGoodsByType")
  ApiResponse queryGoodsByType(@RequestParam int page, @RequestParam int pageSize, @RequestParam int type) {
    logger.info("/queryGoodsByType get request");

    List<Goods> list = counterService.queryGoodsByType(page, pageSize, type);
    return ApiResponse.ok(list);
  }
  @GetMapping(value = "/deleteGoods")
  ApiResponse deleteGood(@RequestParam String id) {
    logger.info("/deleteGoods get request");
    //Address address = counterService.queryAddressById(userId, addressNo);
    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/newGoods")
  ApiResponse newGoods(@RequestBody Goods goods) {
    logger.info("/newGoods get request");
    //Address address = counterService.queryAddressById(userId, addressNo);
    return ApiResponse.ok(0);
  }

  @PostMapping (value = "/newFeedback")
  ApiResponse newFeedback(@RequestBody Feedback feedback) {
    logger.info("/newFeedback get request");
    counterService.newFeedback(feedback);
    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/getFeedbackByUser")
  ApiResponse getFeedbackByUser(@RequestParam String userId) {
    logger.info("/getFeedbackByUser get request");
    List<Feedback> list = counterService.queryFeedbackByUser(userId);
    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/getFeedbackById")
  ApiResponse getFeedbackById(@RequestParam String userId, @RequestParam int id) {
    logger.info("/getFeedbackById get request");
    Feedback feedback = counterService.queryFeedbackById(userId, id);
    return ApiResponse.ok(feedback);
  }
}