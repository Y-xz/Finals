package com.ygz.notes.Chat;

/**
 * Created by Administrator on 2018-02-05.
 */

public class ChatMsg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;

    public ChatMsg(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
