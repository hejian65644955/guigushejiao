package com.atguigu.guigushejiao.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.activity.AddContactActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;

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

    @Override
    protected void initView() {
        super.initView();
        View view = View.inflate(getActivity(), R.layout.fragment_contact_list_head, null);
        ButterKnife.bind(this, view);
        titleBar.setRightImageResource(R.mipmap.em_add);
        listView.addHeaderView(view);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        initListener();

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
    }

    @OnClick({R.id.ll_new_friends, R.id.ll_groups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friends:
                break;
            case R.id.ll_groups:
                break;
        }
    }
}
