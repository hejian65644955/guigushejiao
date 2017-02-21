package com.atguigu.guigushejiao.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/2/21.
 */

public class GroupDetailAdapter extends BaseAdapter {
    private Context context;
    private boolean isModify;
    private List<UserInfo> userInfos;
    private boolean isDeleteModle = false;

    public GroupDetailAdapter(Context context, boolean isModify,onMembersChangeListener onMembersChangeListener) {
        this.onMembersChangeListener = onMembersChangeListener;
        this.context = context;
        this.isModify = isModify;
        userInfos = new ArrayList<>();
    }

    public void refresh(List<UserInfo> userInfos) {
        if (userInfos == null || userInfos.size() == 0) {
            return;
        }
        //清除原来的数据
        this.userInfos.clear();
        //添加加减号
        initUser();
        //添加群成员
        this.userInfos.addAll(0, userInfos);
        notifyDataSetChanged();
    }

    private void initUser() {
        this.userInfos.add(new UserInfo("remove"));
        this.userInfos.add(0, new UserInfo("add"));

    }

    @Override
    public int getCount() {
        return userInfos == null ? 0 : userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_group_member, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(isModify){
            //群主
            if(position ==userInfos.size()-1){
                if(isDeleteModle){//是删除模式
                    convertView.setVisibility(View.GONE);

                }else{
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.ivMemberDelete.setVisibility(View.GONE);
                    viewHolder.tvMemberName.setVisibility(View.INVISIBLE);
                    viewHolder.ivMemberPhoto.setImageResource(R.mipmap.em_smiley_minus_btn_pressed);
                }
            }else if(position ==userInfos.size()-2){
                if(isDeleteModle){//是删除模式
                    convertView.setVisibility(View.GONE);

                }else{
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.ivMemberDelete.setVisibility(View.GONE);
                    viewHolder.tvMemberName.setVisibility(View.INVISIBLE);
                    viewHolder.ivMemberPhoto.setImageResource(R.mipmap.em_smiley_add_btn_pressed);
                }
            }else{//群成员
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tvMemberName.setVisibility(View.VISIBLE);
                //根据删除模式决定是否展示小减号
                if(isDeleteModle){
                    viewHolder.ivMemberDelete.setVisibility(View.VISIBLE);

                }else {
                    viewHolder.ivMemberDelete.setVisibility(View.GONE);
                }
                viewHolder.tvMemberName.setText(userInfos.get(position).getUsername());
                viewHolder.ivMemberPhoto.setImageResource(R.mipmap.em_default_avatar);
            }
            //监听事件
            if(position ==userInfos.size()-1){
                viewHolder.ivMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isDeleteModle){
                            isDeleteModle =true;
                            notifyDataSetChanged();
                        }

                    }
                });
            }else if(position == userInfos.size()-2){
                viewHolder.ivMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onMembersChangeListener!=null){
                            onMembersChangeListener.onAddGroupMember(userInfos.get(position));
                        }

                    }
                });
            }else {
                viewHolder.ivMemberPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onMembersChangeListener!=null){
                            onMembersChangeListener.onRemoveGroupMember(userInfos.get(position));
                        }

                    }
                });
            }
        }else{
            //群成员
            if (position == userInfos.size()-1){
                convertView.setVisibility(View.GONE);
            }else if(position == userInfos.size()-2){
                convertView.setVisibility(View.GONE);
            }else{
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tvMemberName.setText(userInfos.get(position).getUsername());
                viewHolder.ivMemberDelete.setVisibility(View.GONE);
            }
        }


        return convertView;

    }


    class ViewHolder {
        @Bind(R.id.iv_member_photo)
        ImageView ivMemberPhoto;
        @Bind(R.id.tv_member_name)
        TextView tvMemberName;
        @Bind(R.id.iv_member_delete)
        ImageView ivMemberDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private onMembersChangeListener onMembersChangeListener;
    public interface onMembersChangeListener{
        void onRemoveGroupMember(UserInfo userInfo);
        void onAddGroupMember(UserInfo userInfo);
    }
}
