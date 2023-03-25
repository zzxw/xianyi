package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.util.OrderUtil;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

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

  /**
   * 获取当前计数
   * @return API response json
   */
  @GetMapping(value = "/getUserInfo")
  ApiResponse getUserInfo(@RequestParam String code) {
    logger.info("/getUserInfo get request");
    //HttpCli
    //String userId =
    HttpClient httpClient = new HttpClient();
    //code = "093QyB0w3zl3b0320Q1w3HZ5Sg2QyB0j";
    System.out.println("code is " + code);
    PostMethod postMethod = new PostMethod("https://api.weixin.qq.com/sns/jscode2session?appid=wx7874f23b30f30672&secret=7c88c65b513292819127ef927921d708&js_code=" + code + "&grant_type=authorization_code");

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
          return ApiResponse.error(errCode, errMsg, json);
        }
        //data = json.getJSONObject("data");
        data = json;
        String openId = data.getString("openid");
        //String sessionKey = data.getString("session_key");

        if(!counterService.hasUser(openId)) {
          User user = new User();
          user.setUserId(openId);
          counterService.createUser(user);
        }

      }else{
        result = postMethod.getResponseBodyAsString();
        System.out.println("发送请求失败，错误码为:" + status);
        System.out.println(result);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    return ApiResponse.ok(data);
  }

  @GetMapping(value = "/getCart")
  ApiResponse getCart(@RequestParam String userId) {
    logger.info("/getCart get request");

    List<Cart> list = counterService.queryCart(userId);

    return ApiResponse.ok(list);
  }

  @PostMapping(value = "/newCart")
  //ApiResponse newCart(@RequestParam String userId, @RequestParam String goodsID, @RequestParam int num, @RequestParam double price) {
  ApiResponse newCart(@RequestBody Cart cart) {
    logger.info("/newCart post request");
    Cart oldCart = counterService.queryCartByID(cart.getUserId(), cart.getGoodsID());
//    Cart cart = new Cart();
//    cart.setUserId(userId);
//    cart.setGoodsID(goodsID);
//    cart.setPrice(price);
//    cart.setNum(num);
//    counterService.createCart(cart);
    if(oldCart != null) {
      cart.setNum(cart.getNum() + oldCart.getNum());
      counterService.updateCart(cart);
    }else{
      //cart.setNum(num);
      counterService.createCart(cart);
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

    counterService.updateCart(cart);

    return ApiResponse.ok(0);
  }

  @GetMapping(value = "/deleteCart")
  ApiResponse deleteCart(@RequestParam String userId, @RequestParam String goodsID) {
    logger.info("/deleteCart get request");

    int index = goodsID.indexOf(",");
    if(index < 0) {
      counterService.deleteCart(userId, goodsID);
    }else {
      String []goodsIDs = goodsID.split(",");
      Map<String, String[]> map = new HashMap<>();
      map.put("userId", new String[]{userId});
      map.put("goodsID", goodsIDs);
      counterService.deleteCarts(map);
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
  ApiResponse getOrderByUserId(@RequestParam String userId) {
    logger.info("/getOrderByUserId get request");

    List<Order> list = counterService.queryOrderByUserID(userId);
    for(Order order: list) {
      order.setList(counterService.getOrderDetails(order.getOrderID()));
    }
    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/getOrderByStatus")
  ApiResponse getOrderByStatus(@RequestParam String userId, @RequestParam int status) {
    logger.info("/getOrderByUserId get request");

    List<Order> list = counterService.queryOrderByStatus(userId,status);
    for(Order order: list) {
      order.setList(counterService.getOrderDetails(order.getOrderID()));
    }
    return ApiResponse.ok(list);
  }

  @GetMapping(value = "/getOrderById")
  ApiResponse getOrderById(@RequestParam String orderId) {
    logger.info("/getOrderById get request");

    Order order = counterService.queryOrderByID(orderId);
    List<OrderDetail> list = counterService.getOrderDetails(orderId);
    order.setList(list);

    return ApiResponse.ok(order);
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
    //counterService.payOrder(order);
    return ApiResponse.ok(orderID);
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
  ApiResponse updateOrderStatus(@RequestParam String orderId, @RequestParam int status) {
    logger.info("/updateOrder post request");

//    Order order = new Order();
//    order.setGoodsID(goodsID);
//    order.setUserID(userId);
//    order.setNum(num);
//    order.setPrice(price);
//    order.setStatus(status);
//    order.setOrderID(orderID);

    Order order = new Order();
    order.setStatus(status);
    order.setOrderID(orderId);
    counterService.updateOrderStatus(order);
    //counterService.updateOrder(order);

    return ApiResponse.ok(0);
  }
  @GetMapping(value = "/deleteOrder")
  ApiResponse deleteCart(@RequestParam String orderId) {
    logger.info("/deleteCart get request");

    counterService.deleteOrder(orderId);

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
    }
    //address = counterService.queryDefaultAddress(userId);

    return ApiResponse.ok(address);
  }

  @PostMapping(value = "/payNotify")
  ApiResponse notify(@RequestBody String notifyData) {
    logger.info("/payNotify get request");

    counterService.notify(notifyData);
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
}