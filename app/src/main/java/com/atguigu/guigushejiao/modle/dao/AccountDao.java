package com.atguigu.guigushejiao.modle.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.modle.db.AccountDb;
import com.atguigu.guigushejiao.modle.table.AccountTable;

/**
 * Created by lenovo on 2017/2/14.
 */

public class AccountDao  {

    private final AccountDb accountDb;
    public AccountDao(Context context){
        //创建数据库
        accountDb = new AccountDb(context);
    }

    public void addAccount(UserInfo user) {
        if(user ==null){
            return;
        }

        //获取数据库连接
        SQLiteDatabase readableDatabase = accountDb.getReadableDatabase();
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(AccountTable.COL_USER_HXID,user.getHxid());
        contentValues.put(AccountTable.COL_USER_NAME,user.getUsername());
        contentValues.put(AccountTable.COL_USER_NICK,user.getNick());
        contentValues.put(AccountTable.COL_USER_PHOTO,user.getPhoto());
        readableDatabase.replace(AccountTable.TABLE_NAME,null, contentValues);
    }

    // 根据环信id获取所有用户信息
    public UserInfo getAccountByHxId(String hxId){

        if (hxId == null || TextUtils.isEmpty(hxId)){
            return null;
        }

        //获取数据库连接
        SQLiteDatabase database = accountDb.getReadableDatabase();

        String sql = "select * from "+AccountTable.TABLE_NAME
                +" where "+AccountTable.COL_USER_HXID+"=?";
        //第二个参数是条件选择
        Cursor cursor = database.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if (cursor.moveToNext()){

            userInfo = new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_HXID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_PHOTO)));
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(AccountTable.COL_USER_NAME)));
        }

        //关闭游标
        cursor.close();

        return userInfo;
    }
}
