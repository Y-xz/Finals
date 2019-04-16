package com.ygz.notes.Base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ygz.notes.Chat.ChatActivity;
import com.ygz.notes.Chat.MyIMUserInfo;
import com.ygz.notes.NotesActivity;
import com.ygz.notes.R;
import com.ygz.notes.Tabs.NotesFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * RecyclerView 的适配器
 * Created by Administrator on 2018-01-22.
 */

public class NoteMsgAdapter extends RecyclerView.Adapter<NoteMsgAdapter.ViewHolder>{
    private List<NoteMsg> mNoteMsgList;

    /**************正常的显示 Holder -》 持有人*********************/
    static class ViewHolder extends RecyclerView.ViewHolder{
        /*定义控件name*/
        LinearLayout noteLayout;
        TextView NoteTitle;
        ImageButton noteAvatar;
        TextView NoteMsg;
        TextView NoteTime;
        Button goChat,like,collection;
        /*实例化控件*/
        public ViewHolder(View view){
            super(view);
            noteLayout = (LinearLayout)view.findViewById(R.id.layout_note);
            NoteTitle = (TextView)view.findViewById(R.id.tv_notetitle);
            noteAvatar = (ImageButton)view.findViewById(R.id.ib_note_avatar);
            NoteMsg = (TextView)view.findViewById(R.id.tv_notemsg);
            NoteTime = (TextView)view.findViewById(R.id.tv_notetime);
            goChat = (Button)view.findViewById(R.id.btn_notegochat);
            like = (Button)view.findViewById(R.id.btn_note_like);
            collection = (Button)view.findViewById(R.id.btn_note_collection);

        }
    }
    /***********************************/


    public NoteMsgAdapter(List<NoteMsg>noteMsgList){
        mNoteMsgList = noteMsgList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        /***************Chat Button的 点击事件*****************************/
        holder.goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    Toast.makeText(v.getContext(),"IM服务器未连接",Toast.LENGTH_SHORT).show();
                    return;
                }
                int position = holder.getAdapterPosition();
                NoteMsg noteMsg = mNoteMsgList.get(position);
                /*聊天相关 ↓ */
                /*生成 用户信息*/
                /*Log.i("uid",noteMsg.getUserId());*/

                Bundle bundle = new Bundle();
                /*BmobIMUserInfo userInfo = new BmobIMUserInfo(noteMsg.getUserId(),noteMsg.getNickName(),noteMsg.getAvatar());
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(userInfo,null);
                bundle.putSerializable("c",conversationEntrance);*/
                bundle.putSerializable("userInfoId",noteMsg.getUserId());
                bundle.putSerializable("userInfoNn",noteMsg.getNickName());
                bundle.putSerializable("userInfoA",noteMsg.getAvatar());
                bundle.putSerializable("sign",1);
                Intent intent = new Intent();
                intent.setClass(v.getContext(),ChatActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);

                Toast.makeText(v.getContext(),"点击了"+noteMsg.getNickName(),Toast.LENGTH_SHORT).show();
            }
        });
        /******************Chat Button的 点击事件*******************/
        /***************like Buttonn的 点击事件*****************************/
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    Toast.makeText(v.getContext(),"IM服务器未连接",Toast.LENGTH_SHORT).show();
                    return;
                }
                int position = holder.getAdapterPosition();
                NoteMsg noteMsg = mNoteMsgList.get(position);

                AlertDialog.Builder softwareSettingDialog = new AlertDialog.Builder(v.getContext());
                softwareSettingDialog.setMessage("点击了"+noteMsg.getNickName()+"的[LIKE.喜欢modular]计划5.26日完成，技术小哥正在熬夜写代码~")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

                Toast.makeText(v.getContext(),"点击了"+noteMsg.getNickName(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    Toast.makeText(v.getContext(),"IM服务器未连接",Toast.LENGTH_SHORT).show();
                    return;
                }
                int position = holder.getAdapterPosition();
                NoteMsg noteMsg = mNoteMsgList.get(position);

                AlertDialog.Builder softwareSettingDialog = new AlertDialog.Builder(v.getContext());
                softwareSettingDialog.setMessage("点击了"+noteMsg.getNickName()+"的[COLLECTION.收藏modular]计划5.27日完成，技术小哥正在熬夜写代码~")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

                Toast.makeText(v.getContext(),"点击了"+noteMsg.getNickName(),Toast.LENGTH_SHORT).show();
            }
        });


        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteMsg noteMsg = mNoteMsgList.get(position);
        holder.NoteTitle.setText(noteMsg.getNickName());
        holder.NoteMsg.setText(noteMsg.getNote());
        holder.NoteTime.setText(noteMsg.getCreatedAt());
        holder.noteAvatar.setImageResource(getAva(noteMsg.getGander()));

    }


    /*获取Item列表 总长度*/
    @Override
    public int getItemCount() {
        return mNoteMsgList.size();
    }


    /*设置头像*/
    private int getAva(String gander){
        int bitmap = 0;
        if (gander.equals("男")){
            bitmap = R.drawable.m0;
        }else if (gander.equals("女")){
            bitmap = R.drawable.w0;
        }else if (gander.equals("保密")){
            bitmap = R.drawable.s0;
        }
        return bitmap;
    }

}
