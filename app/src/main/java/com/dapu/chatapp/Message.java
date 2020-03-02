package com.dapu.chatapp;

import android.util.Log;

public class Message {
    private String text;

    private String UID;
    private Long time;

    public Message() {
    }

    public Message(String text, String UID,Long time) {
        this.text = text;
        this.UID = UID;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public Long getTime() {
        return time;
    }


    public boolean isCurrentUserMessage(String UId) {
        return UId.equals(UID);
    }

    public void setUID(String UID){ this.UID = UID;}

    public String getUID(){ return UID;}
}