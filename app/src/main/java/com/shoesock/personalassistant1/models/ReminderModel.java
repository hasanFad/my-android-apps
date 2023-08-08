package com.shoesock.personalassistant1.models;

import java.io.Serializable;
import java.util.Date;

public class ReminderModel implements Serializable {

    //

    public Date reminderTime;
    public Date reminderDate;
    public String reminderContent, reminderRepeatFrequency, userName,  sDate,  sTime,  sContent;

    public String getReminderRepeatFrequency() {
        return reminderRepeatFrequency;
    }

    public String getReminderContent() {
        return reminderContent;
    }


    public Date getReminderTime() {
        return reminderTime;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public String getUserName() {
        return userName;
    }

    public ReminderModel(Date reminderDate, Date reminderTime, String reminderContent){
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.reminderContent =  reminderContent;
    }

    public ReminderModel(String sDate, String sTime, String sContent){
        this.sContent = sContent;
        this.sDate = sDate;
        this.sTime = sTime;

    }

    public ReminderModel(String userName, Date reminderDate, Date reminderTime, String reminderContent){
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
       this.reminderContent =  reminderContent;
       this.userName = userName;
    }

} // close the Reminder class
