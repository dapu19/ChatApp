package com.dapu.chatapp;

public class Message {
    private String text;

    private String UID;

    public Message(String text, String UID) {
        this.text = text;
        this.UID = UID;
    }

    public String getText() {
        return text;
    }


    public boolean isCurrentUserMessage(String UId) {
        return UId.equals(UID);
    }
    public String getUID(){ return UID;}
}