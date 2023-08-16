package com.shoesock.personalassistant1.functions.reminder_functions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class ReminderUtils {


    Activity activity;
    Context context;
    SharedPreferencesAssistant preferencesClass;

    ChatUtils chatUtils;
     String REMINDER_PREFERENCES = "reminderPreferences";

    public ReminderUtils(Context context1, Activity activity1, LinearLayout chatContainer, ScrollView scrollView) {

        context = context1; // Update this line if needed
        preferencesClass = new SharedPreferencesAssistant(context);
        activity = activity1;
    } // close the ReminderUtils function

    SharedPreferencesAssistant sharedPreferencesAssistant;


    RealTimeDataBase realTimeDataBase = new RealTimeDataBase();

    Functions functions = new Functions(activity);



    public String checkReminderMessage(String  userMessage) {
        // this function to check if user want to reminder and his response is date or ti=ime or content

        String returnAppMessage = ""; // this string to return it.

        if (functions.isValidDate(userMessage)){ // user message is date
            // it is date
            preferencesClass.saveSharedPreferences(REMINDER_PREFERENCES, "reminderDatePreferences", userMessage);

            returnAppMessage = context.getString(R.string.reminderDateSuccessfullyCaptured) + " " + context.getString(R.string.whatReminderTimeWithout);


        }else if (functions.isValidTime(userMessage)){ // user message is time

            preferencesClass.saveSharedPreferences(REMINDER_PREFERENCES, "reminderTimePreferences", userMessage);


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


} // close ReminderUtils class
