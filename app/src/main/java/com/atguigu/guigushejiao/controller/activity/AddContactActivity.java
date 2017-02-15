package com.atguigu.guigushejiao.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity {

    @Bind(R.id.invite_btn_search)
    Button inviteBtnsearch;
    @Bind(R.id.invite_et_search)
    EditText inviteEtSearch;
    @Bind(R.id.invite_tv_username)
    TextView inviteTvUsername;
    @Bind(R.id.invite_btn_add)
    Button inviteBtnAdd;
    @Bind(R.id.invite_ll_item)
    LinearLayout inviteLlItem;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.invite_btn_search, R.id.invite_btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_btn_search:
                //搜索
                //验证

                if(verify()){
                    //显示搜索结果
                    inviteLlItem.setVisibility(View.VISIBLE);
                    inviteTvUsername.setText(username);

                }else {
                    inviteLlItem.setVisibility(View.GONE);

                }
                break;
            case R.id.invite_btn_add:
                Modle.getInstance().getGlobalThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //去环信服务器添加好友
                        //参数为要添加好友的username和理由
                        try {
                            EMClient.getInstance().contactManager().addContact(username,"添加理由");
                            ShowToast.showUI(AddContactActivity.this,"添加好友成功");
                        } catch (HyphenateException e) {
                            ShowToast.showUI(AddContactActivity.this,"添加好友失败"+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


                break;
        }
    }

    private boolean verify() {
        username = inviteEtSearch.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            ShowToast.show(this,"输入的不能为空");
            return false;
        }

        //服务器认证

        return true;
    }
}
