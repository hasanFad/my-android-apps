package com.shoesock.personalassistant1.functions.reminder_functions;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.chat_functions.ChatUtils;
import com.shoesock.personalassistant1.models.ReminderModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReminderUtils2 {

    private String REMINDER_PREFERENCES = "reminderPreferences";
    private Activity activity;
    private  Context context;
    private  ChatUtils chatUtils;
    private  SharedPreferencesAssistant preferences;

    SharedPreferencesAssistant sharedPreferencesAssistant;

    RealTimeDataBase realTimeDataBase = new RealTimeDataBase();

    Functions functions = new Functions(activity);


    public ReminderUtils2(Context context, Activity activity, LinearLayout chatContainer, ScrollView scrollView) {

        this.context = context; // Update this line if needed
        preferences = new SharedPreferencesAssistant(context);
        this.activity = activity;
        chatUtils = new ChatUtils(context, activity,chatContainer, scrollView);
    } // close the ReminderUtils function







    private void insertDataToModel(String sUsername, String sGetDate, String sGetTime, String sGetContent) throws ParseException {
        Date reminderDateForModel = null;
        Date reminderTimeForModel = null;
        String reminderContentForModel;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Extract reminder time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        reminderDateForModel = dateFormat.parse(sGetDate);
        reminderTimeForModel = timeFormat.parse(sGetTime);
        reminderContentForModel = sGetContent;

        sharedPreferencesAssistant = new SharedPreferencesAssistant(context);

        ReminderModel reminderModel = new ReminderModel(sUsername, reminderDateForModel, reminderTimeForModel, reminderContentForModel);

        realTimeDataBase.insertReminderToDB(reminderModel, new RealTimeDataBase.OnListener() {
            @Override
            public void onSuccess() {
                sharedPreferencesAssistant.removeReminderFromShared();
                functions.ToastFunction(context, activity.getString(R.string.reminderSuccess));

            }

            @Override
            public void onError(String errorMessage) {

                functions.ToastFunction(context, errorMessage);
            }
        });


    }




} // close the ReminderUtils2 class
