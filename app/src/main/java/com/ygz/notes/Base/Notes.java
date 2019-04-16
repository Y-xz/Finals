package com.ygz.notes.Base;

import cn.bmob.v3.BmobObject;

/**
 * Note 扩展数据表base
 * Created by Administrator on 2018-01-20.
 */

public class Notes extends BmobObject {
    private String userId;
    private String username;
    private String nickName;
    private String gander;
    private String avatar;
    private String note;


    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }
    public String getUserName() {
        return username;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return note;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setGander(String gander) {
        this.gander = gander;
    }
    public String getGander() {
        return gander;
    }
}
