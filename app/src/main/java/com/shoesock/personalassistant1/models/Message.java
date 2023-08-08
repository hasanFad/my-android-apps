package com.shoesock.personalassistant1.models;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String messageAddress, messageContent, userName;
    private Date messageTime;

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageAddress() {
        return messageAddress;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void Message(String userName, String messageAddress, String messageContent, Date messageTime){

        this.messageAddress = messageAddress;
        this.messageContent = messageContent;
        this.messageTime = messageTime;
        this.userName = userName;
    } // close the Message function


} // close the Message class
