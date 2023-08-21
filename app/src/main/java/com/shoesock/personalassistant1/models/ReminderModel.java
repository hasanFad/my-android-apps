package com.shoesock.personalassistant1.models;

import java.io.Serializable;
import java.util.Date;

public class ReminderModel implements Serializable {

    //



    public String reminderContent, reminderRepeatFrequency, userName,  reminderDate,  reminderTime;

    public String getReminderRepeatFrequency() {
        return reminderRepeatFrequency;
    }

    public String getReminderContent() {
        return reminderContent;
    }



    public String getUserName() {
        return userName;
    }

    public ReminderModel(){

    }


    public ReminderModel(String reminderDate, String reminderTime, String reminderContent){
        this.reminderContent = reminderContent;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;

    }

    public ReminderModel(String userName, String reminderDate, String reminderTime, String reminderContent){
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
       this.reminderContent =  reminderContent;
       this.userName = userName;
    }

} // close the Reminder class
