package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Feedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeedbackMapper {
    List<Feedback> queryFeedbackByUser(@Param("userId") String userId);

    Feedback queryFeedBackById(@Param("userId") String userId, @Param("id") int id);

    //void deleteAddress(@Param("userID") String userID, @Param("addressNo") int addressNo);

    void updateFeedback(Feedback feedback);

    //void updateDefaultAddress(String userID);

    void createFeedback(Feedback feedback);
}
