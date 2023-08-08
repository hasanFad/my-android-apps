package com.shoesock.personalassistant1.functions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.activities.dialer.AssistantDialer;
import com.shoesock.personalassistant1.activities.reminder.Reminder;
import com.shoesock.personalassistant1.activities.sms.SMSAssistant;
import com.shoesock.personalassistant1.activities.topics.Topics;
import com.shoesock.personalassistant1.models.ReminderModel;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstFunctionsForTest {


    public Activity activity;

    private TTSFunctions ttsFunctions;


    public FirstFunctionsForTest (Activity _activity){
        this.activity = _activity;

    }
    Date reminderTimeF = null;
    Date reminderDateF = null;


    public void ToastFunction(Context context, String textToToast){

        Toast.makeText(context, textToToast, Toast.LENGTH_SHORT).show();
    } // close the ToastFunction function








    public String getRsponseFromAppAssistant(String userMessage) {
        Resources res = activity.getResources();
      //  ttsFunctions = new TTSFunctions(activity);
        final Intent[] intent = new Intent[1];
        int secondsDelayed = 1;

        String[] cases = res.getStringArray(R.array.userWords);
        String aMessage = "";

        int i;
        for(i = 0; i < cases.length; i++)
            if(userMessage.contains(cases[i])) break;

        switch(i) {
            case 0: //אני רוצה לשלוח הודעה

                aMessage = activity.getString(R.string.goToMessageScreen);
                ttsFunctions.speak(aMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(activity, SMSAssistant.class);
                        activity.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);
                break;

            case 1: //אני רוצה לדבר על משהו

                aMessage = activity.getString(R.string.goToTopicScreen);
                ttsFunctions.speak(aMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(activity, Topics.class);
                        activity.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);
                break;

            case 2: //אני רוצה שתזכירי לי משהו

                aMessage = activity.getString(R.string.goToReminderScreen);
                ttsFunctions.speak(aMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(activity, Reminder.class);
                        activity.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);
                break;


            case 3: // אני רוצה להתקשר למישהו

                aMessage = activity.getString(R.string.goToAssistantDialerScreen);
                ttsFunctions.speak(aMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(activity, AssistantDialer.class);
                        activity.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);

                break;

            default:
                aMessage =   doNotUnderstand();
        }

        return aMessage;
    }

    private String checkMessage(String userMessage) {
        return userMessage;
    }


    public String getResponseFromAppAssistant(String userMessage, Context context){
        String callingActivityName = getCallingActivityName();

        if(callingActivityName != null){
            if (callingActivityName.equals(Reminder.class.getName())){
                //
                checkReminderMessage(userMessage);
                ToastFunction(context, "checkReminderMessage : " + userMessage );
            }else if (callingActivityName.equals(Topics.class.getName())){
                checkTopics(userMessage);

            }else if (callingActivityName.equals(SMSAssistant.class.getName())){
                checkSMSMessage(userMessage);
            }else if (callingActivityName.equals(AssistantDialer.class.getName())){
                checkDialer(userMessage);
            }else if (callingActivityName.equals(MainActivity.class.getName())){
                checkMainActivityMessage(userMessage);
            } else {
                // action from unknown activity
                doNotUnderstand();
            }
        } // close the if callingActivityName not null

        return "";
    } // close the getResponseFromAppAssistant function

    public String checkReminderMessage(String  userMessage) {
        String reminderString = "checkReminderMessageFunction";

        // Check if the user message is in the format "dd\\mm"
        if (isValidDate(userMessage)) {
            Log.e("checkReminderMessage","the date is : " + userMessage);
            ttsFunctions.speak("checkReminderMessage");
        } else {
            // Check if the user message is in the format "hh:mm"
            if (isValidTime(userMessage)) {
                Log.e("checkReminderMessage","the time is : " + userMessage);
            } else {
                // The user message is a general string
                Log.e("checkReminderMessage","the content is : " + userMessage);
            }
        }





        return reminderString;
    }



    // Check if the input string matches the "dd\\mm" date format
    private boolean isValidDate(String input) {
        String dateRegex = "\\d{2}\\\\\\d{2}";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // Check if the input string matches the "hh:mm" time format
    private boolean isValidTime(String input) {
        String timeRegex = "\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }


    public String checkReminderMessage2(String  userMessage) {
        // ReminderModel reminderModel = new ReminderModel();
        String string = "check Message functions";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        String reminderDateString = userMessage.substring(userMessage.indexOf("תאריך התזכורת") + 14, userMessage.indexOf("לחודש") - 1);
        try {
            reminderDateF = dateFormat.parse(reminderDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            e.getMessage();
        }


        // Extract time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeString = userMessage.substring(userMessage.indexOf("שעה") + 4, userMessage.indexOf("דקות") - 1);

        try {
            reminderTimeF = timeFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String reminderContent = userMessage.substring(userMessage.lastIndexOf("תוכן התזכורת") + 14).trim();

        ReminderModel reminderModel = new ReminderModel(reminderDateF, reminderTimeF, reminderContent);
        // string = " time: " + reminderTimeF;
        reminderModel.reminderTime = reminderTimeF;
        reminderModel.reminderDate =  reminderDateF;
        reminderModel.reminderContent = reminderContent;
        //  return string;
        return "date : " + reminderDateF + " time : " + reminderTimeF + " content : " + reminderContent;
    } // close checkMessage function


    // this function to get the activity name
    public static String getCallingActivityName(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // the caller's information is at the index 3 in the stack trace array
        if(stackTraceElements.length >= 4){
            StackTraceElement callerElement = stackTraceElements[3];
            return callerElement.getClassName();
        }
        return null;

    } // close getCallingActivityName function

    public String checkMainActivityMessage(String userMessage){
        return userMessage;
    }

    public String checkSMSMessage(String userMessage){

        return userMessage;
    }


    public String checkTopics(String userMessage){

        return userMessage;
    }

    public String checkDialer(String userMessage){
        return  userMessage;
    }


    public String doNotUnderstand(){
       // ttsFunctions = new TTSFunctions(activity);

        String stringForUserUI, stringForSpeck;

        stringForUserUI =  "לא הבנתי. בשביל לעזור לך תגיד אחד מאלה: " +
                activity.getString(R.string.toSendMessage)+ " , או , " +
                activity.getString(R.string.toSpeak) + " , או , " +
                activity.getString(R.string.toReminderMe) + " , או , " +
                activity.getString(R.string.wantToDialer);

        stringForSpeck = "לֹא הֵבַנְתִּי. בִּשְׁבִיל לַעֲזֹר לְךָ תַּגִּיד אֶחָד מֵאֵלֶּה:" +
                activity.getString(R.string.toSendMessage)+ " , או , " +
                activity.getString(R.string.toSpeak) + " , או , " +
                activity.getString(R.string.toReminderMe) + " , או , " +
                activity.getString(R.string.wantToDialer);

        ttsFunctions.speak(stringForSpeck);


        return stringForUserUI;
    }
}
