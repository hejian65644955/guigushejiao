package com.atguigu.guigushejiao.modle.bean;

/**
 * Created by lenovo on 2017/2/20.
 */

public class PickInfo {
    private UserInfo userInfo;
    private boolean ischeck;

    public PickInfo() {
    }

    public PickInfo(UserInfo userInfo, boolean ischeck) {
        this.userInfo = userInfo;
        this.ischeck = ischeck;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}
