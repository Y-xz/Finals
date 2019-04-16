package com.ygz.notes.Base;

import java.util.Date;

/**
 * 纸条消息Msg的实体类
 * Created by Administrator on 2018-01-22.
 */

public class NoteMsg {

    private String userId;
    private String nickName;
    private String username;/*该纸条的用户名*/
    private String gander;
    private String avatar;/*头像*/
    private String note;/*纸条内容*/
    private String createdAt ;/*发送纸条的时间*/

    public NoteMsg(String userId,String nickName,String gander, String note,String createdAt,String avatar) {
        this.userId = userId;
        this.nickName = nickName;
        this.gander = gander;
        this.note = note;
        this.createdAt = createdAt;
        this.avatar = avatar;
    }



    public String getAvatar() {
        return avatar;
    }
    public String getNickName() {
        return nickName;
    }

    public String getGander() {
        return gander;
    }

    public String getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    public String getNote() {
        return note;
    }
    public String getCreatedAt() {
        return createdAt;
    }
}