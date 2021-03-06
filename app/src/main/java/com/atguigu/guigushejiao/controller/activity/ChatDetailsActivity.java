package com.atguigu.guigushejiao.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.atguigu.guigushejiao.ImApplication;
import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.adapter.GroupDetailAdapter;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.utils.Constant;
import com.atguigu.guigushejiao.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatDetailsActivity extends AppCompatActivity {

    @Bind(R.id.gv_group_detail)
    GridView gvGroupDetail;
    @Bind(R.id.bt_group_detail)
    Button btGroupDetail;
    private String groupid;
    private EMGroup group;
    private String owner;
    private GroupDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        ButterKnife.bind(this);

        getData();

        initData();

        //获取群成员
        getGroupMembers();

        //监听事件
        initListener();
    }

    private void initListener() {
        gvGroupDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //获取当前gridview中的deleteModle
                        boolean modle = adapter.getDeleteModle();
                        //是删除模式下管用
                        if(modle){
                            adapter.setDeleteModle(false);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void getGroupMembers() {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //获取群主
                    EMGroup emGroup = EMClient.getInstance().
                            groupManager().getGroupFromServer(groupid);
                    //获取群成员
                    List<String> members = emGroup.getMembers();

                    //转类型
                    final List<UserInfo> userinfos = new ArrayList<UserInfo>();

                    for (String hxid : members) {
                        userinfos.add(new UserInfo(hxid));
                    }

                    //内存和网页
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.refresh(userinfos);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getData() {
        //获取群id
        groupid = getIntent().getStringExtra("groupid");
        if (TextUtils.isEmpty(groupid)) {
            return;
        }
    }

    private void initData() {

        group = EMClient.getInstance().groupManager().getGroup(groupid);
        //获取群主
        owner = group.getOwner();
        if (EMClient.getInstance().getCurrentUser().equals(owner)) {
            //是群主
            btGroupDetail.setText("解散群");
            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Modle.getInstance().getGlobalThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //去环信服务器解散群
                                EMClient.getInstance().groupManager().destroyGroup(groupid);
                                exitGroup();
                                //结束当前页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        ShowToast.show(ChatDetailsActivity.this, "解散群成功");
                                    }
                                });
                            } catch (HyphenateException e) {
                                ShowToast.showUI(ChatDetailsActivity.this, "解散群失败" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } else {
            btGroupDetail.setText("退群");
            btGroupDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Modle.getInstance().getGlobalThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            //告诉环信退群
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(groupid);
                                exitGroup();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                        ShowToast.show(ChatDetailsActivity.this, "退群成功");
                                    }
                                });
                            } catch (HyphenateException e) {
                                ShowToast.show(ChatDetailsActivity.this, "退群成失败" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
        //判断是否有邀请的权限
        boolean isModify = EMClient.getInstance().getCurrentUser().equals(owner) || group.isPublic();
        adapter = new GroupDetailAdapter(this, isModify,new GroupDetailAdapter.onMembersChangeListener(){

            @Override
            public void onRemoveGroupMember(final UserInfo userInfo) {
                //ShowToast.show(ChatDetailsActivity.this,"删除成功");
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //网络删除成员
                        try {
                            EMClient.getInstance().groupManager().
                                    removeUserFromGroup(group.getGroupId(),userInfo.getHxid());
                            ShowToast.showUI(ChatDetailsActivity.this,"删除成功");
                        } catch (HyphenateException e) {
                            ShowToast.showUI(ChatDetailsActivity.this,"删除失败");
                            e.printStackTrace();
                        }
                    }
                });


            }
            @Override
            public void onAddGroupMember(UserInfo userInfo) {
                ShowToast.show(ChatDetailsActivity.this,"添加成功");
                //跳转到选择好友界面
                Intent intent = new Intent(ChatDetailsActivity.this, PickContactActivity.class);
                intent.putExtra("groupid",group.getGroupId());
                startActivityForResult(intent,2);
            }
        });
        gvGroupDetail.setAdapter(adapter);
    }

    private void exitGroup() {
        //注意上下文
        LocalBroadcastManager manager = LocalBroadcastManager.
                getInstance(ImApplication.getGlobalApplication());
        Intent intent = new Intent(Constant.DESTORY_GROUP);
        intent.putExtra("groupid", groupid);
        manager.sendBroadcast(intent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2){
            addMembers(data);
        }
    }

    private void addMembers(Intent data) {
        final String[] memberses = data.getStringArrayExtra("members");
        if(memberses==null && memberses.length>=0){
            return;
        }

        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().
                            addUsersToGroup(group.getGroupId(),memberses);
                    ShowToast.showUI(ChatDetailsActivity.this,"添加群成员成功");
                } catch (HyphenateException e) {
                    ShowToast.showUI(ChatDetailsActivity.this,"添加群成员失败");
                    e.printStackTrace();
                }
            }
        });
    }
}
