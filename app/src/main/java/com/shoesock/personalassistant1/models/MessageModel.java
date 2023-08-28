package com.shoesock.personalassistant1.models;

import java.io.Serializable;

public class MessageModel implements Serializable {

    private String messageAddress, messageContent, userName, messageDate, messageTime;
    boolean isWhatsApp;



    public String getMessageContent() {
        return messageContent;
    }

    public boolean isWhatsApp() {
        return isWhatsApp;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getUserName() {
        return userName;
    }

    public MessageModel(String userName, String messageAddress, String messageContent, String messageDate , String messageTime, boolean isWhatsApp){
        this.messageDate = messageDate;
        this.messageAddress = messageAddress;
        this.messageContent = messageContent;
        this.messageTime = messageTime;
        this.userName = userName;
        this.isWhatsApp = isWhatsApp;
    } // close the Message function


} // close the Message class
