package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User getUser(@Param("userId") String userId);

    void createUser(User user);
}
