package com.dapu.chatapp;

public class Message {
    private String text;

    private boolean belongsToCurrentUser;

    public Message(String text, boolean belongsToCurrentUser) {
        this.text = text;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }


    public boolean isCurrentUserMessage() {
        return belongsToCurrentUser;
    }
}