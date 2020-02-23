package com.dapu.chatapp;

public class Message {
    private String text;

    private String belongsToCurrentUser;

    public Message(String text, String belongsToCurrentUser) {
        this.text = text;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }


    public boolean isCurrentUserMessage(String UId) {
        return UId.equals(belongsToCurrentUser);
    }
}