package com.shoesock.personalassistant1.functions.sms_utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.contact_utils.ContactUtils;
import com.shoesock.personalassistant1.models.MessageModel;
import com.shoesock.personalassistant1.shared_preferences.SharedPreferencesAssistant;

import java.util.ArrayList;

public class SMSUtils2 {

    Context context;
    Activity activity;
    
    SharedPreferencesAssistant preferencesAssistant;
    SharedPreferences preferences;
    ContactUtils contactUtils;

    boolean sendContact, nowContact, content, sendWhatsApp, scheduleMessage;
    String contactName , messageContent , phoneNumber, messageDate , messageTime;

    public SMSUtils2(Context context1, Activity activity1 ){
        context = context1;
        activity = activity1;
        preferencesAssistant = new SharedPreferencesAssistant(context);
        
        preferences = context.getSharedPreferences("messages", Context.MODE_PRIVATE);

         contactUtils = new ContactUtils(activity1, context1);
         sendContact = preferences.getBoolean("sendToContact", false);
         nowContact = preferences.getBoolean("nowContact", false);
         content = preferences.getBoolean("content", false);
         sendWhatsApp = preferences.getBoolean("useWhatsApp", false);
         scheduleMessage = preferences.getBoolean("scheduleMessage", false);
         contactName = preferences.getString("contactName", null);
         messageContent = preferences.getString("messageContent", null);
         phoneNumber = preferences.getString("phoneNumber", null);
         messageDate = preferences.getString("scheduleMessageDate", null);
         messageTime = preferences.getString("messageTime", null);

    }
    RealTimeDataBase realTimeDataBase = new RealTimeDataBase();

    Functions functions = new Functions(activity);
    String doNotUnderStandSMS = "תגיד אחד מאלה, לשלוח וואטסאפ. או לשלוח הודעה רגילה.";

    public String checkSmsUserRequest(String userMessage){

        String returnMessage = doNotUnderStandSMS;

        if (userMessage.equals("לשלוח וואטסאפ") || userMessage.equals("הודעת וואטסאפ")){
            preferencesAssistant.saveBooleanSharedPreferences("messages","useWhatsApp" ,true);

            returnMessage = "לשלוח למספר חדש? או לאיש קשר?";
        }

        if (sendWhatsApp){
            Toast.makeText(context, "sendWhatsApp", Toast.LENGTH_SHORT).show();
        }
        
        if (userMessage.equals("לשלוח הודעה רגילה")){
            preferencesAssistant.saveBooleanSharedPreferences("messages","useWhatsApp" ,false);

            returnMessage = "לשלוח למספר חדש? או לשלוח לאיש קשר?";
        }

        if (userMessage.equals( "לשלוח לאיש קשר") || userMessage.equals("איש קשר")){
            preferencesAssistant.saveBooleanSharedPreferences("messages","sendToContact" ,true);
            preferencesAssistant.saveBooleanSharedPreferences("messages","nowContact" ,true);

            returnMessage = "מה שם איש הקשר?";
        }

        if (sendContact && nowContact){
            preferencesAssistant.saveStringSharedPreferences("messages","contactName" ,userMessage);
            preferencesAssistant.removeShared("messages", "nowContact");
            preferencesAssistant.saveBooleanSharedPreferences("messages","content" ,true);
            // returnMessage = getPhoneNumberFromName(context, contactName) ;
            returnMessage = contactUtils.searchContactByName( contactName);
            if (returnMessage.equals("לא נמצא איש קשר תואם.")){
                functions.openContacts();
            }
        }

        if (functions.isValidPhoneNumber(userMessage)){
            preferencesAssistant.saveStringSharedPreferences("messages","phoneNumber" ,userMessage);
            returnMessage = "מה תוכן ההודעה?";
        }

        if (content){
            preferencesAssistant.saveStringSharedPreferences("messages","messageContent" ,userMessage);
            preferencesAssistant.removeShared("messages", "content");
            returnMessage = "לשלוח עכשיו? או הודעה מתוזמנת?";
        }


        if (userMessage.equals("לשלוח עכשיו")){
            if (sendWhatsApp){
                if (sendContact){

                    // send to number
                    if (!phoneNumber.isEmpty() || phoneNumber != null){
                        functions.sendToWhatsApp(phoneNumber, messageContent);
                        returnMessage = "ההודעה נשלחה";
                    }
                }
            }else {
                if (sendContact){

                    // send to number
                    if (!phoneNumber.isEmpty() || phoneNumber != null){
                        sendToSMS(phoneNumber, messageContent);
                        returnMessage = "ההודעה נשלחה";
                    }
                }
            }
        }


        if (userMessage.equals("הודעה מתוזמנת")){
            preferencesAssistant.saveBooleanSharedPreferences("messages", "scheduleMessage", true);
            returnMessage = "באיזה תאריך לשלוח?";
        }

        if (scheduleMessage && functions.isValidDate(userMessage)){
            preferencesAssistant.saveStringSharedPreferences("messages", "scheduleMessageDate", userMessage);

            returnMessage = "באיזה שעה?";
        }

        if (scheduleMessage && messageDate != null && functions.isValidTime(userMessage)){
            preferencesAssistant.saveStringSharedPreferences("messages", "messageTime", userMessage);

            returnMessage = saveScheduleMessage();
        }


        return returnMessage;
    } // close checkSmsUserRequest function

    public String getPhoneNumberFromame(Context context, String contactName) {
        String phoneNumber = null;
        String response = "מה תוכן ההודעה?";
        // Define the columns you want to retrieve from the Contacts database
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // Specify the contact name in the selection criteria
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {contactName};

        // Query the Contacts database
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        // Check if the cursor contains any results
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve the phone number from the cursor
            int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNumber = cursor.getString(phoneNumberIndex);
            preferencesAssistant.saveStringSharedPreferences("messages", "phoneNumber", phoneNumber);

            cursor.close();
        }else {
            response = "השם לא נמצא";
        }
        return response;
    }




    public void sendToSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), PendingIntent.FLAG_IMMUTABLE);

        if (message.length() <= 160) {
            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent);
        } else {
            ArrayList<String> parts = smsManager.divideMessage(message);
            ArrayList<PendingIntent> sentIntents = new ArrayList<>();
            ArrayList<PendingIntent> deliveredIntents = new ArrayList<>();

            for (int i = 0; i < parts.size(); i++) {
                sentIntents.add(sentIntent);
                deliveredIntents.add(deliveredIntent);
            }

            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveredIntents);
        }
    }


    private String saveScheduleMessage() {
        String responseMessage = "";
        String sUsername = preferences.getString("userName", null);
        insertDataToModel(sUsername, phoneNumber, messageContent, messageDate, messageTime, sendWhatsApp);
        responseMessage = "נשמר ,אעדכן בזמו אמת.";

        return responseMessage;
    }


    private void insertDataToModel(String userName, String phoneNumber, String messageContent, String messageDate, String messageTime, boolean sendWhatsApp) {
        MessageModel messageModel = new MessageModel(userName, phoneNumber, messageContent, messageDate, messageTime, sendWhatsApp);
        realTimeDataBase.insertMessageToDB(messageModel, new RealTimeDataBase.OnListener() {
            @Override
            public void onSuccess() {
                functions.ToastFunction(context, "נשמר ,אעדכן בזמו אמת.");
            }

            @Override
            public void onError(String errorMessage) {
            functions.ToastFunction(context, errorMessage);
            }
        });

    }


} // close the SMSFunctions class
