package com.atguigu.guigushejiao.modle;

import android.content.Context;

import com.atguigu.guigushejiao.modle.dao.AccountDao;
import com.atguigu.guigushejiao.modle.db.DBManger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2017/2/14.
 */

public class Modle {

    private static  Modle modle =new Modle();
    private AccountDao accountDao;
    private DBManger dbManger;

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
        if (currentUser == null){
            return;
        }
        if(dbManger!=null){
            dbManger.close();
        }
        dbManger = new DBManger(context,currentUser+".db");

    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public DBManger getDbManger(){
        return dbManger;
    }
}
