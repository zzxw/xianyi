package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CartMapper {
    List<Cart> queryCart(@Param("userId") String userId);

    Cart queryCartByID(@Param("userId") String userId, @Param("goodsID") String goodsID);

    void createCart(Cart cart);

    void updateCart(Cart cart);

    void deleteCart(@Param("userId") String userId, @Param("goodsID") String goodsID);
    //void deleteCarts(@Param("userId") String userId, @Param("goodsIDs") String goodsIDs);
    void deleteCarts(@Param("info") Map<String, String[]> map);
}
