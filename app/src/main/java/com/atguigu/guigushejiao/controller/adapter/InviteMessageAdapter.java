package com.atguigu.guigushejiao.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.atguigu.guigushejiao.R;
import com.atguigu.guigushejiao.modle.bean.GroupInfo;
import com.atguigu.guigushejiao.modle.bean.InvitationInfo;
import com.atguigu.guigushejiao.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/2/17.
 */

public class InviteMessageAdapter extends BaseAdapter {

    private final Context mContext;
    private List<InvitationInfo> invitationInfos;

    public void refresh(List<InvitationInfo> invitationInfos) {

        //校验
        if (invitationInfos == null) {
            return;
        }
        this.invitationInfos.clear();
        //添加数据
        this.invitationInfos.addAll(invitationInfos);
        //刷新界面
        notifyDataSetChanged();
    }

    public InviteMessageAdapter(Context context,OnInviteChangeListener onInviteChangeListener) {
        this.onInviteChangeListener =onInviteChangeListener;
        this.mContext = context;
        invitationInfos = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return invitationInfos == null ? 0 : invitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return invitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_invite_msg_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);

        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        //绑定数据
        final InvitationInfo invitationInfo = invitationInfos.get(position);
        GroupInfo groupInfo = invitationInfo.getGroupInfo();
        if(groupInfo!=null){
            //群邀请
            vh.tvInviteName.setText(groupInfo.getInvitePerson());

            //隐藏按钮
            vh.btInviteReject.setVisibility(View.GONE);
            vh.btInviteAccept.setVisibility(View.GONE);
            switch(invitationInfo.getStatus()) {
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:

                    vh.tvInviteReason.setText("您的群申请请已经被接受");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    vh.tvInviteReason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    vh.tvInviteReason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    vh.tvInviteReason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    vh.tvInviteReason.setText("您收到了群邀请");
                    //展示按钮
                    vh.btInviteReject.setVisibility(View.VISIBLE);
                    vh.btInviteAccept.setVisibility(View.VISIBLE);
                    vh.btInviteAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onInviteChangeListener != null) {
                                onInviteChangeListener.onInviteAccept(invitationInfo);
                            }
                        }
                    });
                    vh.btInviteReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onInviteChangeListener != null) {
                                onInviteChangeListener.onInviteReject(invitationInfo);
                            }
                        }
                    });
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    vh.tvInviteReason.setText("您收到了群申请");
                    vh.btInviteReject.setVisibility(View.VISIBLE);
                    vh.btInviteAccept.setVisibility(View.VISIBLE);
                    vh.btInviteAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onInviteChangeListener != null) {
                                onInviteChangeListener.onApplicationAccept(invitationInfo);
                            }
                        }
                    });
                    vh.btInviteReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onInviteChangeListener != null) {
                                onInviteChangeListener.onApplicationReject(invitationInfo);
                            }
                        }
                    });
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    vh.tvInviteReason.setText("你接受了群邀请");
                    break;

                // 您批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    vh.tvInviteReason.setText("您批准了群申请");
                    break;

                // 你拒绝了群邀请
                case GROUP_REJECT_INVITE:
                    vh.tvInviteReason.setText("你拒绝了群邀请");
                    break;

                // 您拒绝了群申请
                case GROUP_REJECT_APPLICATION:
                    vh.tvInviteReason.setText("您拒绝了群申请");
                    break;
            }
        }else{
            //联系人邀请
            UserInfo userInfo = invitationInfo.getUserInfo();
            vh.tvInviteName.setText(userInfo.getUsername());
            //隐藏button
            vh.btInviteAccept.setVisibility(View.GONE);
            vh.btInviteReject.setVisibility(View.GONE);
            //新邀请
            if (invitationInfo.getStatus()
                    == InvitationInfo.InvitationStatus.NEW_INVITE){

                //展示button
                vh.btInviteReject.setVisibility(View.VISIBLE);
                vh.btInviteAccept.setVisibility(View.VISIBLE);
                //设置reason
                if (invitationInfo.getReason() == null){
                    vh.tvInviteReason.setText("邀请好友");
                }else{
                    vh.tvInviteReason.setText(invitationInfo.getReason());
                }
                //接受按钮监听
                vh.btInviteAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onInviteChangeListener!=null){
                            onInviteChangeListener.onAccept(invitationInfo);
                        }
                    }
                });
                //拒绝按钮监听
                vh.btInviteReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onInviteChangeListener!=null){
                            onInviteChangeListener.onReject(invitationInfo);
                        }
                    }
                });
            }else if(invitationInfo.getStatus() //邀请被接受
                    == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){
                if (invitationInfo.getReason() == null){
                    vh.tvInviteReason.setText("邀请被接受");
                }else{
                    vh.tvInviteReason.setText(invitationInfo.getReason());
                }
            }else if(invitationInfo.getStatus()  //接受邀请
                    == InvitationInfo.InvitationStatus.INVITE_ACCEPT){
                if (invitationInfo.getReason() == null){
                    vh.tvInviteReason.setText("接受邀请");
                }else{
                    vh.tvInviteReason.setText(invitationInfo.getReason());
                }
            }
        }

        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.tv_invite_name)
        TextView tvInviteName;
        @Bind(R.id.tv_invite_reason)
        TextView tvInviteReason;
        @Bind(R.id.bt_invite_accept)
        Button btInviteAccept;
        @Bind(R.id.bt_invite_reject)
        Button btInviteReject;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


          /*
    * 第一步  定义接口
    * 第二步  定义接口的变量
    * 第三步  设置set方法
    * 第四步  接受接口的实例对象
    * 第五步  调用接口方法
    *
    * */

    private OnInviteChangeListener onInviteChangeListener;

    public void setOnInviteChangeListener(OnInviteChangeListener onInviteChangeListener) {
        this.onInviteChangeListener = onInviteChangeListener;
    }


    public interface OnInviteChangeListener{
        void onAccept(InvitationInfo info); //同意
        void onReject(InvitationInfo info);  //拒绝

        void onInviteAccept(InvitationInfo info);
        void onInviteReject(InvitationInfo info);

        void onApplicationAccept(InvitationInfo info);
        void onApplicationReject(InvitationInfo info);
    }
}
