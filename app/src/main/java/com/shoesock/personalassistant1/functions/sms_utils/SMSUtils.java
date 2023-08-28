package com.shoesock.personalassistant1.functions.sms_utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.shoesock.personalassistant1.db.firebase.RealTimeDataBase;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.models.MessageModel;

import java.util.ArrayList;

public class SMSUtils {

    Context context;
    Activity activity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    boolean sendContact = preferences.getBoolean("sendToContact", false);
    boolean nowContact = preferences.getBoolean("nowContact", false);
    boolean content = preferences.getBoolean("content", false);
    boolean sendWhatsApp = preferences.getBoolean("useWhatsApp", false);
    boolean scheduleMessage = preferences.getBoolean("scheduleMessage", false);
    String contactName = preferences.getString("contactName", null);
    String messageContent = preferences.getString("messageContent", null);
    String phoneNumber = preferences.getString("phoneNumber", null);
    String messageDate = preferences.getString("scheduleMessageDate", null);
    String messageTime = preferences.getString("messageTime", null);
    public SMSUtils(Context context1, Activity activity1 ){
        context = context1;
        activity = activity1;

     preferences = PreferenceManager.getDefaultSharedPreferences(context);

    editor = preferences.edit();
    }
    RealTimeDataBase realTimeDataBase = new RealTimeDataBase();

    Functions functions = new Functions(activity);

    public String checkSmsUserRequest(String userMessage){

        String returnMessage = "לא הבנתי- SMSUTILS -LINE 34";

        if (userMessage.equals("לשלוח וואטסאפ")){
            editor.putBoolean("useWhatsApp", true);
            returnMessage = "לשלוח למספר חדש? או לאיש קשר?";
        }

        if (userMessage.equals("לשלוח הודעה רגילה")){
            editor.putBoolean("useWhatsApp", false);
            returnMessage = "לשלוח למספר חדש? או לשלוח לאיש קשר?";
        }

        if (userMessage.equals("לשלוח לאיש קשר")){
            editor.putBoolean("sendToContact", true);
            editor.putBoolean("nowContact", true);
            returnMessage = "מה שם איש הקשר?";
        }

        if (sendContact && nowContact){
            editor.putString("contactName", userMessage);
            editor.remove("nowContact");
            editor.putBoolean("content", true);
            returnMessage = getPhoneNumberFromName(context, contactName) ;
        }

        if (functions.isValidPhoneNumber(userMessage)){
            editor.putString("phoneNumber", userMessage);
            returnMessage = "מה תוכן ההודעה?";
        }

        if (content){
            editor.putString("messageContent", userMessage);
            editor.remove("content");
            returnMessage = "לשלוח עכשיו? או הודעה מתוזמנת?";
        }


        if (userMessage.equals("לשלוח עכשיו")){
            if (sendWhatsApp){
                if (sendContact){

                    // send to number
                    if (!phoneNumber.isEmpty() || phoneNumber != null){
                        sendToWhatsApp(phoneNumber, messageContent);
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
            editor.putBoolean("scheduleMessage", true);
            returnMessage = "באיזה תאריך לשלוח?";
        }
        if (scheduleMessage && functions.isValidDate(userMessage)){
            editor.putString("scheduleMessageDate", userMessage);
            returnMessage = "באיזה שעה?";
        }

        if (scheduleMessage && messageDate != null && functions.isValidTime(userMessage)){
            editor.putString("messageTime", userMessage);

            returnMessage = saveScheduleMessage();
        }


        return returnMessage;
    } // close checkSmsUserRequest function

    public String getPhoneNumberFromName(Context context, String contactName) {
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
            editor.putString("phoneNumber", phoneNumber);
            cursor.close();
        }else {
            response = "השם לא נמצא";
        }
        return response;
    }


    private void sendToWhatsApp(String nameOrNumber, String messageContent) {
        if (functions.isValidPhoneNumber(nameOrNumber)) {

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + nameOrNumber + "&text=" + messageContent);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }


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
