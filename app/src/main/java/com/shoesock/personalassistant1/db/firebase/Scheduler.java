package com.shoesock.personalassistant1.db.firebase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Scheduler {

    public static  void scheduleTask(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // create an intent to launch the boardCastReceiver
        Intent intent = new Intent(context, ScheduledTaskWorker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // set the interval for periodic checks (once a day at 00:01)
        long intervalMillis = 24 * 60 * 60 * 1000; // its 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, intervalMillis, pendingIntent);

    } // close scheduleTask function

} // close Scheduler class
