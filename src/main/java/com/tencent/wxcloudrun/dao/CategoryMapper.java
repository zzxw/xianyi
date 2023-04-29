package com.tencent.wxcloudrun.dao;





import com.tencent.wxcloudrun.model.PmsProductCategory;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface CategoryMapper {

    List<PmsProductCategory> getCategory();

    @MapKey("id")
    List<Map<String,String>> getCategoryInfo();

}
