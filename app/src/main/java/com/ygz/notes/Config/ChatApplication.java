package com.ygz.notes.Config;

import android.app.Application;

import com.ygz.notes.Base.ChatMsgHandler;
import com.ygz.notes.Base.UniversalImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2018-01-31.
 */
//TODO 集成：1.7、自定义Application，并在AndroidManifest.xml中配置
public class ChatApplication extends Application {

    private static ChatApplication INSTANCE;

    public static ChatApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(ChatApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(ChatApplication a) {
        ChatApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
/*        Bmob.initialize(this,Constants.Bmob_APPID);*/
        BmobSMS.initialize(this,Constants.Bmob_APPID);
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器
        /**
         * 初始化方法包含了DataSDK的初始化步骤，故无需再初始化DataSDK
         * 最好判断只有主进程运行的时候才进行初始化，避免资源浪费
         * */
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new ChatMsgHandler(this));
        }
        UniversalImageLoader.initImageLoader(this);
    }

    /**获取当前的进程名
     * PID == Process ID 进程标识符
     * cmdline 命令行*/
    public static String getMyProcessName(){
        try {
            File file = new File("/proc/"+android.os.Process.myPid()+"/"+"cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
