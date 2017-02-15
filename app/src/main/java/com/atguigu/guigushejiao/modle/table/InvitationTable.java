package com.atguigu.guigushejiao.modle.table;

/**
 * Created by lenovo on 2017/2/15.
 */

public class InvitationTable {
    public static final String TABLE_NAME ="invitation";//表名
    public static final String COL_USER_NAME ="username";//联系人名
    public static final String COL_USER_HXID ="hxid";//联系人环信
    public static final String COL_GROUP_ID ="groupid";//群主id
    public static final String COL_GROUP_NAME ="groupname";//群主名称
    public static final String COL_REASON ="reason";//理由
    public static final String COL_STATUS ="status";//状态

    public static final String CREATE_TABLE = "create table "+TABLE_NAME + "("

            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_GROUP_ID + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " Integer);";


}