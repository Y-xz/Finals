package com.ygz.notes.Tabs;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ygz.notes.Base.NoteMsg;
import com.ygz.notes.Base.NoteMsgAdapter;
import com.ygz.notes.Base.Notes;
import com.ygz.notes.Base.RecyclerViewItemDecoration;
import com.ygz.notes.Config.Constants;

import com.ygz.notes.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2018-01-15.
 */

public class NotesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private Boolean netWorkState = true;
    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private Button netW;
    private List<NoteMsg> noteMsgList = new ArrayList<>();
    BmobQuery<Notes> query = new BmobQuery<Notes>();

    private SwipeRefreshLayout noteMsgRefreshLayout;
    private RecyclerView noteMsgRecyclerView;

    private NoteMsgAdapter adapter;
    private String noteMsgDate = "2018-02-12 16:04:00";
    private String noteLastMsgDate = "0000-00-00 00:00:00";



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        noteMsgRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.notemsg_swipe_refresh);
        noteMsgRecyclerView = (RecyclerView) getView().findViewById(R.id.notemsg_recycle_view);
        netW = (Button)getView().findViewById(R.id.btn_note_net);
        initNetWorkState();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        noteMsgRecyclerView.setLayoutManager(layoutManager);
        adapter = new NoteMsgAdapter(noteMsgList);
        noteMsgRecyclerView.setAdapter(adapter);
        noteMsgRecyclerView.addItemDecoration(new RecyclerViewItemDecoration());

        noteMsgRefreshLayout.setOnRefreshListener(this);
        netW.setVisibility(View.GONE);
        if (!netWorkState){
            Toast.makeText(getActivity(), "请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return;
        }

        /*设置颜色*/
        /*设置变化颜色*/
        initNoteMsg();
/*监听 滑动事件*/
        noteMsgRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            Boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              //  super.onScrollStateChanged(recyclerView, newState);

                /*LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());*/
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    /** idle 滑动空闲时， （非自由滚动的时候手指离开屏幕）
                     * settling  下沉中
                    * dragging 拖动中*/
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()+1;

                    int totalItemCount = noteMsgList.size();
                    if (lastVisibleItem == (totalItemCount) && isSlidingToLast){

                        initPullNoteMsg();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "ing …", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    isSlidingToLast = true;
                }else {
                    isSlidingToLast = false;
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    /*初始化小纸条消息*/
    private void initNoteMsg(){
        if (!netWorkState){
             Toast.makeText(getActivity(), "请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return;
        }
        query.order("-createdAt");
        query.findObjects(new FindListener<Notes>() {
            @Override
            public void done(List<Notes> list, BmobException e) {
                if (e==null){
                    for (int i = 0;i<6;i++){
                        long compare = -1;
                        if (list.size()>0){
                            compare = noteMsgDate.compareTo(list.get(i).getCreatedAt().toString());
                        }
                        /*等于0 相等，小于0主体小，大于0主体大*/
                        if (i < list.size() && compare<0){
                            NoteMsg noteV = new NoteMsg(list.get(i).getUserId(),list.get(i).getNickName(),list.get(i).getGander(),list.get(i).getNote(),list.get(i).getCreatedAt().toString(),list.get(i).getAvatar());
                            noteMsgList.add(i,noteV);
                            adapter.notifyItemInserted(0);//从什么地方插入
                            noteMsgRecyclerView.scrollToPosition(0);//完事定位到什么位置
                            /*上拉加载
                            * 获取到 第一次 的item.size（最后一个）的时间（最后一个 时间） 然后想办法加载 */
                        }else break;
                    }

                    /*Toast.makeText(getActivity(), "查询成功", Toast.LENGTH_SHORT).show();*/
                }
                /*获取当前列表最新的item 的note时间*/
                noteMsgDate = list.get(0).getCreatedAt().toString();
                Toast.makeText(getActivity()," "+noteMsgList.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*上拉加载*/
    private void initPullNoteMsg(){
        if (!netWorkState){
            Toast.makeText(getActivity(), "请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return;
        }
        query.order("-createdAt");
        query.findObjects(new FindListener<Notes>() {
            @Override
            public void done(List<Notes> list, BmobException e) {
                noteLastMsgDate = list.get(noteMsgList.size()-1).getCreatedAt();
                if (e==null){
                    int itemCount = noteMsgList.size();
                    for (int i = 0,c = 0;i < list.size()&&c<6;i++){

                        int compare = noteLastMsgDate.compareTo(list.get(i).getCreatedAt());
                        /*等于0 相等，小于0主体小，大于0主体大*/
                        if (compare>0){
                            c++;
                            NoteMsg noteV = new NoteMsg(list.get(i).getObjectId(),list.get(i).getNickName(),list.get(i).getGander(),list.get(i).getNote(),list.get(i).getCreatedAt().toString(),list.get(i).getAvatar());
                            noteMsgList.add(i,noteV);
                            adapter.notifyItemInserted(noteMsgList.size()-1);//从什么地方插入
                            noteMsgRecyclerView.scrollToPosition(itemCount);//完事定位到什么位置
                            /*上拉加载
                            * 获取到 第一次 的item.size（最后一个）的时间（最后一个 时间） 然后想办法加载 */
                        }
                    }
                    //Toast.makeText(getActivity(), "加载成功！！！", Toast.LENGTH_SHORT).show();

                }
                noteLastMsgDate = list.get(noteMsgList.size()-1).getCreatedAt();
            }
        });
    }

    @Override
    public void onRefresh() {
        initNoteMsg();
        /*if (延时)*/
        adapter.notifyDataSetChanged();
        noteMsgRefreshLayout.setRefreshing(false);
    }
    /*监听网络状态*/
    class NetWorkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){
                netW.setVisibility(View.GONE);
                netWorkState = true;
            }else {
                netWorkState = false;
                netW.setVisibility(View.VISIBLE);
            }
        }
    }
    private void initNetWorkState(){
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        getActivity().registerReceiver(netWorkChangeReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(netWorkChangeReceiver);
    }
}