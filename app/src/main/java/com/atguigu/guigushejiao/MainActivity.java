package com.atguigu.guigushejiao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atguigu.guigushejiao.fragment.ContactListFragment;
import com.atguigu.guigushejiao.fragment.ConversitionFragment;
import com.atguigu.guigushejiao.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rb_main_chat)
    RadioButton rbMainChat;
    @Bind(R.id.rb_main_contact)
    RadioButton rbMainContact;
    @Bind(R.id.rb_main_setting)
    RadioButton rbMainSetting;
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    private ConversitionFragment conversitionFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();

        setOnListener();

        //初始选中fragment
        switchFragment(R.id.rb_main_chat);
    }

    private void setOnListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchFragment(checkedId);
            }
        });

    }

    private void switchFragment(int checkedId) {
        Fragment fragment = new Fragment();
        switch (checkedId) {
            case R.id.rb_main_chat:
                fragment = conversitionFragment;
                break;
            case R.id.rb_main_contact:
                fragment = contactListFragment;
                break;
            case R.id.rb_main_setting:
                fragment = settingFragment;
                break;
        }
            getSupportFragmentManager()
                    .beginTransaction().replace(R.id.mian_f1, fragment).commit();

    }

    private void initFragment() {
        conversitionFragment = new ConversitionFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }
}
