package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    Goods queryGoodsDetail(@Param("id") int id);

    List<Goods> queryGoods(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    List<Goods> queryGoodsByCondition(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize, @Param("keyword") String keyword);

    List<Goods> queryGoodsByType(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize, @Param("category") int category);

}
