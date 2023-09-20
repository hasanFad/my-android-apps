package com.shoesock.personalassistant1.functions.sms_utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class MessageUtils {

    Context context;
    Activity activity;
    SharedPreferencesAssistant preferencesAssistant;
    SharedPreferences preferences;
    private static final String PREFERENCES_MESSAGES_NAME = "messages";


    public MessageUtils(Context context, Activity activity){
        this.activity = activity;
        this.context = context;

        preferencesAssistant = new SharedPreferencesAssistant(context);

        preferences = context.getSharedPreferences(PREFERENCES_MESSAGES_NAME, Context.MODE_PRIVATE);


    } // close MessageUtils function


} // close MessageUtils class
