package com.atguigu.guigushejiao.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.atguigu.guigushejiao.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InviteMessageActivity extends AppCompatActivity {

    @Bind(R.id.invite_mes_lv)
    ListView inviteMesLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_message);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //inviteMesLv.setAdapter();
    }
}
