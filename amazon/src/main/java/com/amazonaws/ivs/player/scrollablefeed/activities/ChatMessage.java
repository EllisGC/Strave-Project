package com.amazonaws.ivs.player.scrollablefeed.activities;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ChatMessage {
    private String textMessage;
    private String userMessage;
    private long messageTime;

    public ChatMessage(String textMessage, String userMessage) {
        this.textMessage = textMessage;
        this.userMessage = userMessage;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public ChatMessage(String textMessage, FirebaseUser currentUser) {
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
