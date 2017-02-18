package com.atguigu.guigushejiao;


import android.app.Application;
import android.content.Context;

import com.atguigu.guigushejiao.modle.Modle;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by lenovo on 2017/2/14.
 */

public class ImApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化环信SDK
        initHXSdk();

        //初始化Modle
        Modle.getInstance().init(this);

        mContext = this;

    }

    private void initHXSdk() {
        //配置文件
        EMOptions options = new EMOptions();
        //总是接受请求
        options.setAcceptInvitationAlways(false);
        //自动接受群邀请
        options.setAutoAcceptGroupInvitation(false);
        //初始化sdk
        EaseUI.getInstance().init(this, options);

    }

    // 获取全局上下文对象
    public static Context getGlobalApplication() {
        return mContext;
    }
}
