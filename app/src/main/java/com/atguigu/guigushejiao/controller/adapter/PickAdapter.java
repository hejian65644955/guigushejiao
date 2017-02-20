package com.atguigu.guigushejiao.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.bean.PickInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/2/20.
 */

public class PickAdapter extends BaseAdapter {

    private final Context context;
    List<PickInfo> pickInfos;

    public PickAdapter(Context context) {
        this.context = context;
        pickInfos = new ArrayList<>();
    }

    public void refresh(List<PickInfo> pickInfos) {
        if (pickInfos == null) {
            return;
        }

        this.pickInfos.clear();
        this.pickInfos.addAll(pickInfos);
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return pickInfos == null ? 0 : pickInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return pickInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_pick_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //初始化数据
        PickInfo pickInfo = pickInfos.get(position);
        viewHolder.cbItemPickContacts.setChecked(pickInfo.ischeck());
        viewHolder.tvItemPickContactsName.setText(pickInfo.getUserInfo().getUsername());
        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.cb_item_pick_contacts)
        CheckBox cbItemPickContacts;
        @Bind(R.id.tv_item_pick_contacts_name)
        TextView tvItemPickContactsName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
