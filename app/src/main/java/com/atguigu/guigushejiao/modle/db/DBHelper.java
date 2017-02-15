package com.atguigu.guigushejiao.modle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.atguigu.guigushejiao.modle.table.ContactTable;
import com.atguigu.guigushejiao.modle.table.InvitationTable;

/**
 * Created by lenovo on 2017/2/15.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.CREATE_TABLE);//创建联系人表
        db.execSQL(InvitationTable.CREATE_TABLE);//创建邀请信息表

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
