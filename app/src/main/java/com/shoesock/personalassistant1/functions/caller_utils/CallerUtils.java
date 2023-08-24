package com.shoesock.personalassistant1.functions.caller_utils;

import android.app.Activity;
import android.content.Context;

import android.widget.EditText;

import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class CallerUtils {

    Context context;
    Activity activity;
    EditText messageEditText;
    SharedPreferencesAssistant preferencesClass;
    Functions functions;
    boolean callContacts;
    public CallerUtils(Context context1, Activity activity1, EditText messageEditText1){
        context = context1;
        activity = activity1;
        messageEditText = messageEditText1;
        preferencesClass = new SharedPreferencesAssistant(context);
        functions = new Functions(activity);

    } // close CallerUtils function


    public String checkCallerMessage(String userMessage) {
        // this function will check if the user want to call to new number or from contacts
        String returnAppMessage = ""; // this string to return it.


        if (userMessage.equals("להתקשר למספר חדש")) {
            // will open the keyboard to set the number
            returnAppMessage = "לאיזה מספר אתה רוצה להתקשר?";

        } else if (userMessage.equals("להתקשר לאיש קשר")) {
            // will open the contacts
            // functions.openContacts();
            callContacts = true;
            returnAppMessage = "למי רוצה להתקשר?";


        }else if(functions.isValidPhoneNumber(userMessage) ){
            // call to the number
               functions.callPhoneNumber(userMessage);

        }else if(callContacts){
            // search name from contacts to call

            returnAppMessage = functions.callContactsNameIfExists(userMessage);

        }

        return returnAppMessage;
    } // close checkCallerMessage function






} // close CallerUtils class
