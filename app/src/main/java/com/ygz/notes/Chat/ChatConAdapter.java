package com.ygz.notes.Chat;

import android.content.Context;
import android.view.View;

import com.ygz.notes.Base.Conversation;
import com.ygz.notes.R;
import com.ygz.notes.util.TimeUtil;

import java.util.Collection;

/**
 * Created by Administrator on 2018-03-24.
 */

public class ChatConAdapter extends BaseRecyclerAdapter<Conversation>{
    public ChatConAdapter(Context context, IMutlipleItem<Conversation> items, Collection<Conversation> datas){
        super(context,items,datas);
    }

    @Override
    public void bindView(BaseRecyclerHolder holder, Conversation item, int position) {
        holder.setText(R.id.tv_recent_msg,item.getLastMessageContent());
        holder.setText(R.id.tv_recent_time,TimeUtil.getChatTime(false,item.getLastMessageTime()));
        /*头像*/
        Object obj = item.getAvatar();
        if (obj instanceof String){
            String avatar = (String)obj;
            holder.setImageView(avatar,R.drawable.m0,R.id.iv_recent_avatar);
        }else {
            int defaultRes = (int)obj;
            holder.setImageView(null,defaultRes,R.id.iv_recent_avatar);
        }
        /*标题*/
        holder.setText(R.id.tv_recent_name,item.getcName());
        /*未读消息数*/
        long unread = item.getUnReadCount();
        if (unread > 0){
            holder.setVisible(R.id.tv_recent_unread, View.VISIBLE);
            holder.setText(R.id.tv_recent_unread,String.valueOf(unread));
        }else {
            holder.setVisible(R.id.tv_recent_unread,View.GONE);
        }
    }
}
