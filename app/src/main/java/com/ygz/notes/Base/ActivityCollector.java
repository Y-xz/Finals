package com.ygz.notes.Base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-11-14.
 * 活动收集器
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    //添加
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    //去除
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:activities){
            activity.finish();
        }
    }
}
