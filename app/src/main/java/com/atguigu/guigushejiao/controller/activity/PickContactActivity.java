package com.atguigu.guigushejiao.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.controller.adapter.PickAdapter;
import com.atguigu.guigushejiao.modle.Modle;
import com.atguigu.guigushejiao.modle.bean.PickInfo;
import com.atguigu.guigushejiao.modle.bean.UserInfo;
import com.atguigu.guigushejiao.utils.ShowToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PickContactActivity extends AppCompatActivity {

    @Bind(R.id.tv_pick_save)
    TextView tvPickSave;
    @Bind(R.id.lv_pick)
    ListView lvPick;
    private PickAdapter pickAdapter;
    private List<PickInfo> pickInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        ButterKnife.bind(this);

        initView();

        initData();

        initListener();
    }

    private void initListener() {
        lvPick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取item里的checkbox
                CheckBox cbPick =
                        (CheckBox) view.findViewById(R.id.cb_item_pick_contacts);
                //对当前checkbox状态进行取反
                cbPick.setChecked(!cbPick.isChecked());
                PickInfo pickInfo = pickInfos.get(position);
                pickInfo.setIscheck(cbPick.isChecked());
                pickAdapter.refresh(pickInfos);
            }
        });
    }

    private void initData() {
    //获取联系人本地
        List<UserInfo> conatcts = Modle.getInstance().getDbManger()
                .getContactDao().getConatcts();
        if(conatcts ==null){
            return;
        }
        if(conatcts.size()==0){
            ShowToast.show(this,"您还没有好友");
        }

        //转化数据
        pickInfos = new ArrayList<>();
        for(UserInfo userInfo :conatcts){
            pickInfos.add(new PickInfo(userInfo,false));
        }
        pickAdapter.refresh(pickInfos);


    }

    private void initView() {
        pickAdapter = new PickAdapter(this);
        lvPick.setAdapter(pickAdapter);

    }
}