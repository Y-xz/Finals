package com.ygz.notes.Tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ygz.notes.Base.ActivityCollector;
import com.ygz.notes.Base.Conversation;
import com.ygz.notes.Base.PrivateConversation;
import com.ygz.notes.Chat.ChatConAdapter;
import com.ygz.notes.Chat.IMutlipleItem;
import com.ygz.notes.Chat.OnRecyclerViewListener;
import com.ygz.notes.LoginActivity;
import com.ygz.notes.NotesActivity;
import com.ygz.notes.R;
import com.ygz.notes.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.BmobUser;

/**
 * 聊天列表
 * Created by 袁小正 on 2018-01-15.
 */

public class ChatFragment extends Fragment {

    RecyclerView rc_view;
    SwipeRefreshLayout sw_refresh;

    ChatConAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

    private void setListener(){

        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity()," "+"单击了", Toast.LENGTH_SHORT).show();
                adapter.getItem(position).onClick(getActivity());
            }
            /*长按Item 事件*/
            @Override
            public boolean onItemLongClick(final int position) {

                /*弹出对话框*/

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("确定要删除当前会话吗?删除后不可恢复")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*逻辑代码*/
                                adapter.getItem(position).onLongClick(getActivity());
                                adapter.remove(position);


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onStart() {
        super.onStart();
        /*EventBus.getDefault().register(this);*/
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        IMutlipleItem<Conversation>mutlipleItem = new IMutlipleItem<Conversation>() {
            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemViewType(int postion, Conversation conversation) {
                return 0;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };

        rc_view = (RecyclerView)getView().findViewById(R.id.rl_fm_chat);
        sw_refresh = (SwipeRefreshLayout)getView().findViewById(R.id.srl_fm_chat);
        adapter = new ChatConAdapter(getActivity(),mutlipleItem,null);
        rc_view.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        rc_view.setLayoutManager(layoutManager);
        sw_refresh.setEnabled(true);
        setListener();

    }

    public void query(){
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }
    private List<Conversation> getConversations(){
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        //TODO 会话：4.2、查询全部会话
        List<BmobIMConversation>list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1:
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        Collections.sort(conversationList);
        return conversationList;
    }

    /*自定义接收事件*/
    public void onEventMainThread(RefreshEvent event){
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }
    /*离线接收事件*/

}
