package com.atguigu.guigushejiao.modle;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2017/2/14.
 */

public class Modle {

    private static  Modle modle =new Modle();

    private Modle(){};

    public static Modle getInstance(){
        return modle;
    }

    private Context context;
    public void init(Context context){
        this.context =context;
    }

    private ExecutorService thread = Executors.newCachedThreadPool();

    public ExecutorService getGlobalThread(){
        return  thread;
    }
}
