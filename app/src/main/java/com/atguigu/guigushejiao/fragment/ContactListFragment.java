package com.atguigu.guigushejiao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.activity.AddContactActivity;
import com.atguigu.guigushejiao.controller.activity.InviteMessageActivity;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.utils.Constant;
import com.atguigu.guigushejiao.utils.SpUtils;
import com.hyphenate.chat.EMClient;
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
        initListener();
        //初始化小红点
        isShow();

        //注册广播
        manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(receiver,new IntentFilter(Constant.NEW_INVITE_CHANGE));

        initData();
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

                    //内存和网页
                    refreshContact();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshContact() {
        //从本地获取数据
        List<UserInfo> contacts = Modle.getInstance().getDbManger().getContactDao()
                .getConatcts();
        //校验
        if(contacts==null){
            return;
        }


        //转换数据
        Map<String,EaseUser> maps = new HashMap<>();
        for (UserInfo userInfo:contacts) {
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
                break;
        }
    }
}
