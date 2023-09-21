package com.shoesock.personalassistant1.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesAssistant {
    private Context context;
    SharedPreferences sharedPreferences;


    public SharedPreferencesAssistant(Context context){
        this.context = context;
    } // close SharedPreferencesAssistant function


    public void saveStringSharedPreferences(String sharedName, String key, String value ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    } // close saveStringSharedPreferences function


    public void saveBooleanSharedPreferences(String sharedName, String key, boolean value ){
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
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
        removeShared(SharedPrefKeys.REMINDER_PREFERENCES_KEY, "reminderDatePreferences");
        removeShared(SharedPrefKeys.REMINDER_PREFERENCES_KEY, "reminderTimePreferences");
        removeShared(SharedPrefKeys.REMINDER_PREFERENCES_KEY, "reminderContentPreferences");
    } // close removeReminderFromShared function

} // close SharedPreferences class
