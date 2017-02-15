package com.atguigu.guigushejiao.modle.db;

import android.content.Context;

import com.atguigu.guigushejiao.modle.dao.ContactDao;
import com.atguigu.guigushejiao.modle.dao.InvitationDao;

/**
 * Created by lenovo on 2017/2/15.
 */

public class DBManger {

    private final ContactDao contactDao;
    private final InvitationDao invitationDao;
    private final DBHelper dbHelper;

    public DBManger(Context context, String name){
        dbHelper = new DBHelper(context, name);
        contactDao = new ContactDao(dbHelper);
        invitationDao = new InvitationDao(dbHelper);
    }

    public ContactDao getContactDao() {
        return contactDao;
    }

    public InvitationDao getInvitationDao() {
        return invitationDao;
    }
    public void close(){
        dbHelper.close();

    }
}
