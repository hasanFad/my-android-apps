package com.shoesock.personalassistant1.functions.reminder_utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.models.ReminderModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

import java.text.ParseException;

public class ReminderUtils {


    Activity activity;
    Context context;
    SharedPreferencesAssistant preferencesClass;

     String REMINDER_PREFERENCES = "reminderPreferences";

    public ReminderUtils(Context context1, Activity activity1, LinearLayout chatContainer, ScrollView scrollView) {

        context = context1; // Update this line if needed
        preferencesClass = new SharedPreferencesAssistant(context);
        activity = activity1;
    } // close the ReminderUtils function




    RealTimeDataBase realTimeDataBase = new RealTimeDataBase();

    Functions functions = new Functions(activity);



    public  String checkReminderMessage(String userMessage) {
        // this function to check if user want to reminder and his response is date or time or content

        String returnAppMessage = ""; // this string to return it.

        if (functions.isValidDate(userMessage)){ // user message is date
            // it is date
            preferencesClass.saveSharedPreferences(REMINDER_PREFERENCES, "reminderDatePreferences", userMessage);
            functions.ToastFunction(context, userMessage);
            returnAppMessage = context.getString(R.string.reminderDateSuccessfullyCaptured) + " " + context.getString(R.string.whatReminderTimeWithout);


        }else if (functions.isValidTime(userMessage)){ // user message is time

            preferencesClass.saveSharedPreferences(REMINDER_PREFERENCES, "reminderTimePreferences", userMessage);
            functions.ToastFunction(context, userMessage);

            returnAppMessage = context.getString(R.string.reminderTimeSuccessfullyCaptured) + context.getString(R.string.whatReminderContentWithout);
        } else  {
            // Handle any exceptions that might occur while creating and passing the ReminderModel
            // will save userMessage to sharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences(REMINDER_PREFERENCES, Context.MODE_PRIVATE);
            String shredDate = sharedPreferences.getString("reminderDatePreferences", null);
            String sharedTime = sharedPreferences.getString("reminderTimePreferences", null);
            if(shredDate != null){
                if (sharedTime != null){

                    preferencesClass.saveSharedPreferences(REMINDER_PREFERENCES, "reminderContentPreferences", userMessage);
                    SharedPreferences sharedPreferencesFromLogin = activity.getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
                    String sUsername = sharedPreferencesFromLogin.getString("userName", null);
                    // (String userName, Date reminderDate, Date reminderTime, String reminderContent){
                    try {
                        insertDataToModel(sUsername, shredDate, sharedTime, userMessage);

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    returnAppMessage = context.getString(R.string.reminderContentSuccessfullyCaptured);


                }else {
                    returnAppMessage = context.getString(R.string.whatReminderTimeWithout);
                }
            }else {
                returnAppMessage = context.getString(R.string.whatReminderDateWithout);
            }

        }


        return returnAppMessage;

    }

                        //
    private void insertDataToModel(String sUsername, String date, String time, String content) throws ParseException {

/////////////////////////////

            ReminderModel reminderModel = new ReminderModel(sUsername, date, time, content);

            realTimeDataBase.insertReminderToDB(reminderModel, new RealTimeDataBase.OnListener() {
                @Override
                public void onSuccess() {
                    preferencesClass.removeReminderFromShared();
                    functions.ToastFunction(context, activity.getString(R.string.reminderSuccess));


                }

                @Override
                public void onError(String errorMessage) {

                    functions.ToastFunction(context, errorMessage);
                }
            });


    } // close the insertDataToModel function


} // close ReminderUtils class
