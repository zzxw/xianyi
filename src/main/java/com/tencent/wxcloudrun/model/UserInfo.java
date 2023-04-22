package com.tencent.wxcloudrun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    private String appID;
    private String openID;

    @JsonProperty("appId")
    public String getAppID() { return appID; }
    @JsonProperty("appId")
    public void setAppID(String value) { this.appID = value; }

    @JsonProperty("openId")
    public String getOpenID() { return openID; }
    @JsonProperty("openId")
    public void setOpenID(String value) { this.openID = value; }
}
