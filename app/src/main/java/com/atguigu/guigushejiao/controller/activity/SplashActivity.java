package com.atguigu.guigushejiao.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.atguigu.guigushejiao.MainActivity;
import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.Modle;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==0){
                //进入主界面火登录界面
                enterMainOrLogin();
            }
        }
    };

    private void enterMainOrLogin() {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器获取是否登录
                boolean loggedInBefore = EMClient.getInstance().isLoggedInBefore();
                if(loggedInBefore){
                    //登录成功后需要处理
                    //Modle.getInstance().login
                    //录过
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(0,2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
