package com.ygz.notes.Chat;

import com.ygz.notes.Base.MyUser;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by Administrator on 2018-02-06.
 */

public class MyIMUserInfo extends BmobIMUserInfo {

    private String userId;
    private String nickName;
    private String avatar;

    public MyIMUserInfo(String id,String name,String avatar){
        this.userId = id;
        this.nickName = name;
        this.avatar = avatar;

    }


}
