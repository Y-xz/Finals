package com.ygz.notes.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ygz.notes.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018-02-05.
 */

public class ChatMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    private List<BmobIMMessage> msgs = new ArrayList<>();

    private String currentUid="";
    BmobIMConversation c;

    /*适配器 构造函数 */
    public ChatMsgAdapter(Context context,BmobIMConversation c){
        try{
            currentUid = BmobUser.getCurrentUser().getObjectId();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.c = c;
    }
    /*找到 消息的位置*/
    public int findPosition(BmobIMMessage message){
        int index = this.getCount();
        int position = -1;
        while (index-- >0){
            if (message.equals(this.getItem(index))){
                position = index;
                break;
            }
        }
        return  position;
    }
    /*Count：计数，得到长度*/
    public int getCount(){
        return this.msgs == null? 0 : this.msgs.size();
    }
    /*添加消息*/
    public void addMessages(List<BmobIMMessage>messages){
        msgs.addAll(0,messages);
        notifyDataSetChanged();
    }
    public void addMessages(BmobIMMessage message){
        msgs.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }
    /**获取消息
     * @param position
     * @return
     */
    /*获取该Item的消息*/
    public BmobIMMessage getItem(int position){
        return this.msgs == null?null:(position >= this.msgs.size()?null : this.msgs.get(position));
    }
    /**移除消息
     * @param position
     */
    public void  remove(int position){
        msgs.remove(position);
        notifyDataSetChanged();
    }
    /*获取 首条 消息*/
    public BmobIMMessage getFirstMessage(){
        if (null != msgs && msgs.size() > 0){
            return msgs.get(0);
        }else {
            return null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT){
            return new SendTextHolder(parent.getContext(),parent,c,onRecyclerViewListener);
        }else if (viewType == TYPE_RECEIVER_TXT){
            return new ReceiveTextHolder(parent.getContext(),parent,onRecyclerViewListener);
        }else {
            return null;
        }
    }
    /**
     * instanceof 判断 后者是不是前者的实例*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(msgs.get(position));
        if (holder instanceof ReceiveTextHolder){
            ((ReceiveTextHolder)holder).showTime(shouldShowTime(position));
        }else if (holder instanceof SendTextHolder){
            ((SendTextHolder)holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
        }else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }
    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    /*显示时间*/
    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = msgs.get(position - 1).getCreateTime();
        long curTime = msgs.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}

