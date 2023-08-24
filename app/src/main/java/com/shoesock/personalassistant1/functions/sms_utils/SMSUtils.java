package com.shoesock.personalassistant1.functions.sms_utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class SMSUtils {

    Context context;
    Activity activity;


    String SMS_PREFERENCES = "smsPreferences";
    String WHATSAPP_PREFERENCES = "whatsAppPreferences";

    public SMSUtils(Context context1, Activity activity1 ){
        context = context1;
        activity = activity1;

    }
    SharedPreferencesAssistant preferencesClass = new SharedPreferencesAssistant(context);
    SharedPreferences savedShared = activity.getSharedPreferences(SMS_PREFERENCES, Context.MODE_PRIVATE);
    String sms;

    public String checkSmsMessage(String userMessage){
        // this function to check if user want to send sms and his response is number or content or dateTime
        String returnAppMessage = ""; // this string to return it .


        if (userMessage.equals("הודעה רגילה"  )){
            preferencesClass.saveSharedPreferences(SMS_PREFERENCES, "smsPreferences", userMessage);
            returnAppMessage = "לשלוח הודעה למספר חדש, או הודעה לאיש קשר?";


        }else if (userMessage.equals("הועדעת וואטסאפ")){
            preferencesClass.saveSharedPreferences(WHATSAPP_PREFERENCES, "whatsAppPreferences", userMessage);


        } // close the if whatsApp

        return returnAppMessage;
    } // close the checkSmsMessage function


    boolean wantSms = false;
    public boolean userWant(String userMsg){
        if (userMsg.equals("הודעה רגילה")){
            wantSms = true;
        }else {
            wantSms = false;
        }
        return wantSms;
    }

    public String userRequestIs(String userMessage){
        if (userMessage.equals("הודעה רגילה")){
            preferencesClass.saveSharedPreferences(SMS_PREFERENCES, "smsPreferences", userMessage);
            wantSms = true;
        }
        if (userMessage.equals("הודעת וואטסאפ")){
            preferencesClass.saveSharedPreferences(WHATSAPP_PREFERENCES, "whatsAppPreferences", userMessage);
            wantSms = false;
        }
        if (wantSms) {
            switch (userMessage) {

                case "מספר חדש":
                    preferencesClass.saveSharedPreferences(SMS_PREFERENCES, "smsNewNumberPreferences", userMessage);
                    break;
                case "איש קשר קיים":
                    preferencesClass.saveSharedPreferences(SMS_PREFERENCES, "smsContactPreferences", userMessage);
                    break;
                case "הודעה מתוזמנת":
                    preferencesClass.saveSharedPreferences(SMS_PREFERENCES, "smsScheduledPreferences", userMessage);
                    break;
            } // close the switch in wantSms is true

        }else {
            // the user want whatsApp
            // the wantSms == false
            switch (userMessage){

                case "מספר חדש":
                    preferencesClass.saveSharedPreferences(WHATSAPP_PREFERENCES, "whatsNewNumberPreferences", userMessage);
                    break;
                case "איש קשר קיים":
                    preferencesClass.saveSharedPreferences(WHATSAPP_PREFERENCES, "whatsContactPreferences", userMessage);
                    break;
                case "הודעה מתוזמנת":
                    preferencesClass.saveSharedPreferences(WHATSAPP_PREFERENCES, "whatsScheduledPreferences", userMessage);
                    break;
            } // close switch

        }
    return "";
    } // close userRequestIs function

} // close the SMSFunctions class
