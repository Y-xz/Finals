package com.ygz.notes.Chat;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ygz.notes.Base.ActivityCollector;
import com.ygz.notes.Base.BaseActivity;

import com.ygz.notes.LoginActivity;
import com.ygz.notes.NotesActivity;
import com.ygz.notes.R;
import com.ygz.notes.Tabs.ChatFragment;

import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;


import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;

import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends BaseActivity implements View.OnClickListener,MessageListHandler{

    String sign;

    private RelativeLayout rl;
    private EditText inputText;
    private Button send,back;
    private TextView titleName;
    private RecyclerView chatMsgRecyclerView;
    private ChatMsgAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected LinearLayoutManager layoutManager;

    BmobIMConversation mConversationManager;
    /*记录管理*/
    BmobRecordManager recordManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*设置不使用系统自带标题栏*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        /*******/
        Bundle bundle = getIntent().getExtras();
        String ObjectId = bundle.getString("userInfoId");
        String Username = bundle.getString("userInfoNn");
        String Avatar = bundle.getString("userInfoA");
        int sign = 0;
        sign = bundle.getInt("sign");
        //TODO 消息：5.1、根据会话入口获取消息管理，聊天页面
        /*根据会话来源 判断如何创建*/
        if (sign == 0){
            BmobIMConversation conversationEntrance = (BmobIMConversation) getBundle().getSerializable("c");
            mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(),conversationEntrance);
        }else {
            BmobIMUserInfo userInfo = new BmobIMUserInfo(ObjectId,Username,Avatar);

            BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
                @Override
                public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                    if (e == null){
                        Toast.makeText(ChatActivity.this,"CE 成功",Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(ChatActivity.this,"CE 错误"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mConversationManager = BmobIMConversation.obtain(BmobIMClient.getInstance(),conversationEntrance);
        }

        /*******/
        initWidget();
        /*titleName.setText(Username);*/
        initSwipeRefreshLayout();


    }
    /*初始化控件*/
    private void initWidget(){
        rl = (RelativeLayout)findViewById(R.id.rl_chat);
        inputText = (EditText)findViewById(R.id.et_chatinput);
        send = (Button)findViewById(R.id.btn_chatsend);
        back = (Button)findViewById(R.id.btn_chat_back);
        titleName = (TextView)findViewById(R.id.tv_chat_title_name);

        chatMsgRecyclerView = (RecyclerView)findViewById(R.id.chatmsg_recycle_view);
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.srl_chat);

        send.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        chatMsgRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChatMsgAdapter(this,mConversationManager);
        chatMsgRecyclerView.setAdapter(adapter);
        /*视图监听*/
        rl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                /*自动刷新*/
                swipeRefreshLayout.setRefreshing(true);
                queryMessages(null);
            }
        });

        /*下拉加载*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        /*Item 点击事件*/
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ChatActivity.this,""+position,Toast.LENGTH_SHORT);
            }
            @Override
            public boolean onItemLongClick(final int position) {
                Toast.makeText(ChatActivity.this,"删除这条消息："+position,Toast.LENGTH_SHORT);
                //TODO 消息：5.3、删除指定聊天消息

                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatActivity.this);
                dialog.setMessage("确定要删除这条的消息吗？删除后不可恢复")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mConversationManager.deleteMessage(adapter.getItem(position));
                                adapter.remove(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chatsend :
                if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
                    toast("尚未连接IM服务器");
                    return;
                }
                sendMessage();
                break;
            case R.id.btn_chat_back:
                finish();
                break;
            default:
                break;
        }

    }
    //TODO 发送消息：6.1、发送文本消息
    private void sendMessage(){
        String text = inputText.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("不能发送空白消息哦~");
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        mConversationManager.sendMessage(msg,listener);
    }

    /**
     * 消息发送监听器
     */
    public MessageSendListener listener = new MessageSendListener() {
        @Override
        public void onStart(BmobIMMessage bmobIMMessage) {
            super.onStart(bmobIMMessage);
            adapter.addMessages(bmobIMMessage);
            inputText.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
            adapter.notifyDataSetChanged();
            inputText.setText("");
            scrollToBottom();
            Toast.makeText(ChatActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
            if (e != null){
                Toast.makeText(ChatActivity.this,"发送失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };

    /*获取用户对象信息*/
    public Bundle getBundle(){
        if (getIntent() != null && getIntent().hasExtra(getPackageName())){
            return getIntent().getBundleExtra(getPackageName());
        }
        else{
            return null;
        }
    }





    private void scrollToBottom(){
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1,0);
    }



    //TODO 消息接收：8.2、单个页面的自定义接收器
    /*接收在线消息*/
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        for (int i = 0;i < list.size();i++){
            addMessageChat(list.get(i));
        }
    }
    private void addMessageChat(MessageEvent event){
        BmobIMMessage msg = event.getMessage();
        if (mConversationManager != null && event != null && mConversationManager.getConversationId().equals(event.getConversation().getConversationId())  /*如果是当前会话的消息*/&& !msg.isTransient()/*判断是不是自己的消息*/){
            /*不是暂态消息*/
            if (adapter.findPosition(msg) < 0){
                adapter.addMessages(msg);
                mConversationManager.updateReceiveStatus(msg);
            }
            scrollToBottom();
        }else {
            com.orhanobut.logger.Logger.i("不是与当前聊天对象的消息");
            toast("不是当前聊天的对象");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*锁屏期间的收到的未读消息需要添加到聊天界面中*/
        addUnReadMessage();
        BmobIM.getInstance().addMessageListHandler(this);
        /*有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知*/
        BmobNotificationManager.getInstance(this).cancelNotification();
    }
    /**
     *添加未读的通知栏消息到聊天界面
     * */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0){
            int size = cache.size();
            for (int i = 0;i < size;i++){
                MessageEvent event = cache.get(i);
                addMessageChat(event);
            }
        }
        scrollToBottom();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recordManager != null){
            recordManager.clear();
        }
        //TODO 消息：5.4、更新此会话的所有消息为已读状态
        if (mConversationManager != null){
            mConversationManager.updateLocalCache();
        }
        /*隐藏软键盘*/
        hideSoftInputView();
    }

    public void queryMessages(BmobIMMessage msg){
        //TODO 消息：5.2、查询指定会话的消息记录
        mConversationManager.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null){
                    if (null != list && list.size() > 0){
                       adapter.addMessages(list);
                       layoutManager.scrollToPositionWithOffset(list.size()-1,0);
                    }
                }else {
                    toast(e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });
    }

}
