package com.shoesock.personalassistant1.functions.caller_utils;

import android.app.Activity;
import android.content.Context;

import android.widget.EditText;

import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.contact_utils.ContactUtils;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

public class CallerUtils {

    Context context;
    Activity activity;
    EditText messageEditText;
    SharedPreferencesAssistant preferencesClass;
    Functions functions;
    boolean callContacts;
    ContactUtils contactUtils;
    public CallerUtils(Context context1, Activity activity1, EditText messageEditText1){
        context = context1;
        activity = activity1;
        messageEditText = messageEditText1;
        preferencesClass = new SharedPreferencesAssistant(context);
        functions = new Functions(activity);
        contactUtils = new ContactUtils(activity1, context1);

    } // close CallerUtils function

    String callerHelpForUser = "לא הבנתי, בשביל להתקשר , להגיד אם להתקשר למספר חדש או להתקשר לאיש קשר.";

    public String checkCallerMessage(String userMessage) {
        // this function will check if the user want to call to new number or from contacts
        String returnAppMessage = callerHelpForUser; // this string to return it.


        if (userMessage.equals("להתקשר למספר חדש") || userMessage.equals("תתקשר למספר חדש")) {
            // will open the keyboard to set the number
            returnAppMessage = "לאיזה מספר אתה רוצה להתקשר?";

        } else if (userMessage.equals("להתקשר לאיש קשר") || userMessage.equals("תתקשר לאיש קשר")) {
            // will open the contacts
            // functions.openContacts();
            returnAppMessage = "למי רוצה להתקשר?";
            callContacts = true;

        }else if(functions.isValidPhoneNumber(userMessage) ){
            // call to the number
            String ltrPhoneNumber = "\u202A" + userMessage + "\u202C";

            functions.callPhoneNumber(ltrPhoneNumber);
            returnAppMessage= "הפעלתי את הטלפון";
        }else if(callContacts){
            // search name from contacts to call

            returnAppMessage = contactUtils.searchContactByName(userMessage);
            if (returnAppMessage.equals("לא נמצא איש קשר תואם.")){
                functions.openContacts();
            }

        }

        return returnAppMessage;
    } // close checkCallerMessage function






} // close CallerUtils class
