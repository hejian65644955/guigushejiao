package com.atguigu.guigushejiao.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.adapter.InviteMessageAdapter;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.modle.bean.InvitationInfo;
import com.atguigu.guigushejiao.utils.Constant;
import com.atguigu.guigushejiao.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InviteMessageActivity extends AppCompatActivity {

    @Bind(R.id.invite_mes_lv)
    ListView inviteMesLv;
    private InviteMessageAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };
    private LocalBroadcastManager manger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    private void initData() {
        manger = LocalBroadcastManager.getInstance(this);
        manger.registerReceiver(receiver,new IntentFilter(Constant.NEW_INVITE_CHANGE));
    }

    private void initView() {
        adapter = new InviteMessageAdapter(this, new InviteMessageAdapter.OnInviteChangeListener() {
            @Override
            public void onAccept(final InvitationInfo info) {
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager()
                                    .acceptInvitation(info.getUserInfo().getHxid());
                            //本地
                            Modle.getInstance().getDbManger()
                                    .getInvitationDao()
                                    .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT,
                                            info.getUserInfo().getHxid());

                            //内存和网页
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh();
                                    ShowToast.show(InviteMessageActivity.this,"接受成功");
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ShowToast.showUI(InviteMessageActivity.this,"接受失败"+e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onReject(final InvitationInfo info) { Modle.getInstance().getGlobalThread().execute(new Runnable() {
                @Override
                public void run() {
                    //网络通知环信
                    try {
                        EMClient.getInstance().contactManager()
                                .declineInvitation(info.getUserInfo().getHxid());
                        //本地
                        Modle.getInstance().getDbManger().getInvitationDao()
                                .removeInvitation(info.getUserInfo().getHxid());
                        Modle.getInstance().getDbManger().getContactDao()
                                .deleteContactByHxId(info.getUserInfo().getHxid());
                        //内存和网页
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                                ShowToast.show(InviteMessageActivity.this,"拒绝成功");
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        ShowToast.showUI(InviteMessageActivity.this,"拒绝失败"+e.getMessage());
                    }
                }
            });
            }
        });
        inviteMesLv.setAdapter(adapter);

        refresh();
    }

    private void refresh() {
        //获取数据
        List<InvitationInfo> invitations = Modle.getInstance()
                .getDbManger().getInvitationDao().getInvitations();

        //刷新数据
        if(invitations ==null){
            return;
        }
        adapter.refresh(invitations);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manger.unregisterReceiver(receiver);
    }
}
