package com.atguigu.guigushejiao.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/2/14.
 */

public class ShowToast {


    public static void show(Context context, String name) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
    }

    public static void showUI(final Activity context, final String name) {
        if(context ==null){
            return;
        }

        //在主线程中运行
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(context,name);
            }
        });
    }
}
