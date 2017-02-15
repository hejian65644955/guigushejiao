package com.atguigu.guigushejiao.modle;

import android.content.Context;

import com.atguigu.guigushejiao.modle.dao.AccountDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2017/2/14.
 */

public class Modle {

    private static  Modle modle =new Modle();
    AccountDao accountDao;

    private Modle(){};

    public static Modle getInstance(){
        return modle;
    }

    private Context context;
    public void init(Context context){
        this.context =context;
        accountDao = new AccountDao(context);

        //初始化全局监听
        new GlobalListener(context);
    }

    private ExecutorService thread = Executors.newCachedThreadPool();

    public ExecutorService getGlobalThread(){
        return  thread;
    }

    public void loginSuccess(String currentUser) {

    }

    public AccountDao getAccountDao() {
        return accountDao;
    }
}
