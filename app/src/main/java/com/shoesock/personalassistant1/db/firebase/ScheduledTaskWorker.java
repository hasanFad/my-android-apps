package com.shoesock.personalassistant1.db.firebase;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScheduledTaskWorker extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // now here to check if have reminders, scheduled sms, scheduled whatsApp on fireBase.
        Toast.makeText(context, "s", Toast.LENGTH_SHORT).show();
        // logic saving at firebase hashed userName -> reminder ?
        // get the reminder for the user.


    }
} // close ScheduledTaskReceiver class
