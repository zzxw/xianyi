package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    Goods queryGoodsDetail(@Param("id") int id);

    List<Goods> queryGoods(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

}