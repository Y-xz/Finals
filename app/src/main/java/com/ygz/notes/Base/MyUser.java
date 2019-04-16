package com.ygz.notes.Base;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017-11-21.
 */

public class MyUser extends BmobUser {
    private String nickname;
    private String gander;
    private String avatar;


    public MyUser(){}
/*    public MyUser(String id){
        setObjectId(id);
    }*/

    /*昵称*/
    public void setNickname(String nickname){this.nickname = nickname;}
    public String getNickname(){return nickname;}
    /*性别*/
    public void setGander(String gander){this.gander = gander;}
    public String getGander(){return gander;}
    /*头像*/

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }
}
