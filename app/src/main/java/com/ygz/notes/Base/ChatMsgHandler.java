package com.ygz.notes.Base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ygz.notes.Chat.ChatActivity;
import com.ygz.notes.MainActivity;
import com.ygz.notes.R;
import com.ygz.notes.model.UserModel;
import com.ygz.notes.model.i.UpdateCacheListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;


/**
 * Handler 管理者
 * Created by Administrator on 2018-01-31.
 */
//TODO 集成：1.6、自定义消息接收器处理在线消息和离线消息
public class ChatMsgHandler extends BmobIMMessageHandler{

    private Context context;

    public ChatMsgHandler(Context context){
        this.context = context;
    }
    /**
     *接受在线消息
     * Receive 接收
     * 当接收到服务器发来的消息时，此方法被调用*/
    @Override
    public void onMessageReceive(final MessageEvent event) {
        executeMessage(event);

    }
    /**
     *接收离线消息,每次connect的时候会查询离线消息，如果有，此方法会被调用
     * Offline 离线 （脱机）*/
    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        Map<String,List<MessageEvent>>map = event.getEventMap();

        for (Map.Entry<String,List<MessageEvent>>entry : map.entrySet()){
            List<MessageEvent>list = entry.getValue();
            int size = list.size();

            for (int i = 0;i < size;i++){
                executeMessage(list.get(i));
            }
        }
    }

    /*处理消息*/
    private void executeMessage(final MessageEvent event){
        UserModel.getInstance().updateUserInfo(event, new UpdateCacheListener() {
            @Override
            public void done(BmobException e) {
                BmobIMMessage msg = event.getMessage();
                processSDKMessage(msg,event);
            }
        });


    }

    private void processSDKMessage(BmobIMMessage msg,MessageEvent event){
        if (BmobNotificationManager.getInstance(context).isShowNotification()){
            /*显示通知*/
            /*pending 代办意图*/
            Intent pendingIntent = new Intent(context, ChatActivity.class);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //TODO 消息接收：8.5、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            BmobNotificationManager.getInstance(context).showNotification(event,pendingIntent);
            /*Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);
            BmobNotificationManager.getInstance(context).showNotification()*/;
        }else {
            EventBus.getDefault().post(event);
        }
    }
}
