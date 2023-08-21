package com.shoesock.personalassistant1.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesAssistant {
    private Context context;
    SharedPreferences sharedPreferences;
    String REMINDER_PREFERENCES = "reminderPreferences";


    public SharedPreferencesAssistant(Context context){
        this.context = context;
    } // close SharedPreferencesAssistant function


    public void saveSharedPreferences(String sharedName, String key, String value ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    } // close saveSharedPreferences function

    public void removeShared(String sharedName, String keyToRemove){
         sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(keyToRemove);
        editor.apply();
    } // close removeShared function

    public void removeReminderFromShared() {
        // the keys is: reminderDatePreferences / reminderDatePreferences / reminderContentPreferences
        removeShared(REMINDER_PREFERENCES, "reminderDatePreferences");
        removeShared(REMINDER_PREFERENCES, "reminderTimePreferences");
        removeShared(REMINDER_PREFERENCES, "reminderContentPreferences");
    } // close removeReminderFromShared function

} // close SharedPreferences class
