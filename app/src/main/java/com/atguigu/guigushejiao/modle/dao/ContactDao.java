package com.atguigu.guigushejiao.modle.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.modle.db.DBHelper;
import com.atguigu.guigushejiao.modle.table.ContactTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/15.
 */

public class ContactDao {

    private final DBHelper dbHelper;

    public ContactDao(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //获取所有联系人
    public List<UserInfo> getConatcts() {
        //获取连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //查询
        String sql = "select * from" + ContactTable.TABLE_NAME
                + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = database.rawQuery(sql, null);
        List<UserInfo> userInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
            userInfos.add(userInfo);

        }
        //关闭游标
        cursor.close();
        return userInfos;
    }

    //通过环信id获取联系人单人信息
    public UserInfo getContactByHx(String HxId) {
        if (TextUtils.isEmpty(HxId)) {
            return null;
        }

        //获取连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String sql = "select * from " + ContactTable.TABLE_NAME
                + " where " + ContactTable.COL_USER_HXID + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{HxId});

        UserInfo userInfo =null;
        if(cursor.moveToNext()){
            userInfo = new UserInfo();
            userInfo.setUsername(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NAME)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_PHOTO)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_NICK)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_USER_HXID)));
        }
        //关闭游标
        cursor.close();

        return userInfo;
    }

    //通过环信id用户联系人信息
    public List<UserInfo> getContactsByHx(List<String> hxids){
        if(hxids==null || hxids.size()==0){
            return null;
        }

        //封装数据
        List<UserInfo> userInfos =new ArrayList<>();
        for(String hxid :hxids){
            UserInfo userInfo = getContactByHx(hxid);
            if(userInfo!=null){
                userInfos.add(userInfo);
            }
        }

        return userInfos;
    }

    //保存单个联系人
    public void saveContact(UserInfo user,boolean isMyContact){
        if(user == null){
            return;
        }
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactTable.COL_IS_CONTACT,isMyContact?1:0);
        contentValues.put(ContactTable.COL_USER_HXID,user.getHxid());
        contentValues.put(ContactTable.COL_USER_NAME,user.getUsername());
        contentValues.put(ContactTable.COL_USER_NICK,user.getNick());
        contentValues.put(ContactTable.COL_USER_PHOTO,user.getPhoto());
        database.replace(ContactTable.TABLE_NAME,null, contentValues);
    }

    //保存联系人信息
    public  void saveContacts(List<UserInfo> contacts,boolean isMyContact){
        if(contacts ==null ||contacts.size()==0){
            return;
        }

        for(UserInfo userInfo:contacts){
            saveContact(userInfo,isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId){
        if (TextUtils.isEmpty(hxId)){
            return;
        }

        //获取连接
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.delete(ContactTable.TABLE_NAME,ContactTable.COL_USER_HXID+"=?",new String[]{hxId});
    }

}
