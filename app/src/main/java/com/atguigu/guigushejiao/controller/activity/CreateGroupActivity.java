package com.atguigu.guigushejiao.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.utils.ShowToast;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivity extends AppCompatActivity {

    @Bind(R.id.et_newgroup_name)
    EditText etNewgroupName;
    @Bind(R.id.et_newgroup_desc)
    EditText etNewgroupDesc;
    @Bind(R.id.cb_newgroup_public)
    CheckBox cbNewgroupPublic;
    @Bind(R.id.cb_newgroup_invite)
    CheckBox cbNewgroupInvite;
    @Bind(R.id.bt_newgroup_create)
    Button btNewgroupCreate;
    private String desc;
    private String groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.bt_newgroup_create)
    public void onClick() {

        if (validate()){

            //跳转
            Intent intent = new Intent(CreateGroupActivity.this,PickContactActivity.class);
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            createGroup(data);
        }
    }

    private void createGroup(final Intent data) {
        Modle.getInstance().getGlobalThread().execute(new Runnable() {
            @Override
            public void run() {
                String[] members =data.getStringArrayExtra("members");
                //去环信服务器创建群
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers=200;

                if (cbNewgroupPublic.isChecked()){
                    if (cbNewgroupInvite.isChecked()){
                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else{
                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else{
                    if (cbNewgroupInvite.isChecked()){

                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

                    }else{

                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                try {
                    EMClient.getInstance().groupManager().createGroup(groupname,desc,members,"",option);
                    ShowToast.showUI(CreateGroupActivity.this,"创建群成功");
                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ShowToast.showUI(CreateGroupActivity.this,"创建群失败"+e.getMessage());
                }
            }
        });

    }

    private boolean validate() {

        desc = etNewgroupDesc.getText().toString().trim();
        groupname = etNewgroupName.getText().toString().trim();

        if (TextUtils.isEmpty(groupname)){
            ShowToast.show(this,"群名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(desc)){
            ShowToast.show(this,"群简介不能为空");
            return false;
        }
        return true;
    }
}
