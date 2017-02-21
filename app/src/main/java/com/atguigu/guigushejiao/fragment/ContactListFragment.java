package com.atguigu.guigushejiao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.activity.AddContactActivity;
import com.atguigu.guigushejiao.controller.activity.ChatActivity;
import com.atguigu.guigushejiao.controller.activity.GroupListActivity;
import com.atguigu.guigushejiao.controller.activity.InviteMessageActivity;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.utils.Constant;
import com.atguigu.guigushejiao.utils.ShowToast;
import com.atguigu.guigushejiao.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/2/15.
 */
public class ContactListFragment extends EaseContactListFragment {

    @Bind(R.id.contanct_iv_invite)
    ImageView contanctIvInvite;
    @Bind(R.id.ll_new_friends)
    LinearLayout llNewFriends;
    @Bind(R.id.ll_groups)
    LinearLayout llGroups;
    private LocalBroadcastManager lm;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShow();
        }
    };
    private LocalBroadcastManager manager;
    private BroadcastReceiver contactreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContact();
        }
    };
    private List<UserInfo> contacts;
    private BroadcastReceiver groupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isShow();
        }
    };

    private void isShow() {
        boolean isShow = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        contanctIvInvite.setVisibility(isShow?View.VISIBLE:View.GONE);
        
    }

    @Override
    protected void initView() {
        super.initView();
        View view = View.inflate(getActivity(), R.layout.fragment_contact_list_head, null);
        ButterKnife.bind(this, view);
        titleBar.setRightImageResource(R.mipmap.em_add);
        listView.addHeaderView(view);
        initData();

        initListener();
        //初始化小红点
        isShow();

        //注册广播
        manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(receiver,new IntentFilter(Constant.NEW_INVITE_CHANGE));
        manager.registerReceiver(contactreceiver,new IntentFilter(Constant.CONTACT_CHANGE));
        manager.registerReceiver(groupReceiver,new IntentFilter(Constant.GROUP_INVITE_CHAGE));



        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    return false;
                }

                showDialog(position);
                return true;
            }
        });

    }

    private void showDialog(final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage("确定要删除吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteContact(position);
                    }
                })
                .create()
                .show();
    }

    private void deleteContact(final int position) {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //获取到这个用户的环信ID
                    final UserInfo userInfo = contacts.get(position - 1);
                    //网络删除
                    EMClient.getInstance().contactManager().deleteContact(userInfo.getHxid());
                    //本地删除 删除联系人
                    Modle.getInstance().getDbManger().getContactDao()
                            .deleteContactByHxId(userInfo.getHxid());
                    //删除邀请信息
                    Modle.getInstance().getDbManger().getInvitationDao()
                            .removeInvitation(userInfo.getHxid());

                    if(getActivity()==null){
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //刷新
                            refreshContact();
                        }
                    });


                    ShowToast.showUI(getActivity(),"删除成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(getActivity(),"删除失败"+e.getMessage());
                }
            }
        });

    }

    private void initData() {
        //获取联系人
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                //从服务器获取联系人
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().
                            contactManager().getAllContactsFromServer();
                    //保存数据库
                    //转化数据
                    List<UserInfo> userInfos = new ArrayList<UserInfo>();
                    for(int i=0;i<allContactsFromServer.size();i++){
                        userInfos.add(new UserInfo(allContactsFromServer.get(i)));
                    }

                    Modle.getInstance().getDbManger().
                            getContactDao().saveContacts(userInfos,true);

                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //内存和网页

                            refreshContact();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshContact() {
        //从本地获取数据
        contacts = Modle.getInstance().getDbManger().getContactDao()
                .getConatcts();

        //校验
        if(contacts ==null){
            return;
        }
        //转换数据
        Map<String,EaseUser> maps = new HashMap<>();
        for (UserInfo userInfo: contacts) {
            EaseUser user = new EaseUser(userInfo.getHxid());
            maps.put(userInfo.getHxid(),user);
        }
        setContactsMap(maps);
        refresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshContact();
    }

    @Override
    protected void setUpView() {
        super.setUpView();

    }

    private void initListener() {
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowToast.showUI(getActivity(),"aaaaa");
                //跳转到邀请页面
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        //解除注册
        manager.unregisterReceiver(receiver);
        manager.unregisterReceiver(contactreceiver);
        manager.unregisterReceiver(groupReceiver);

    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                //隐藏小红点
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,false);
                isShow();
                //跳转
                Intent intent = new Intent(getActivity(), InviteMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_groups:
                //跳转到群列表
                Intent groupIntent = new Intent(getActivity(),GroupListActivity.class);
                startActivity(groupIntent);
                break;
        }
    }
}
