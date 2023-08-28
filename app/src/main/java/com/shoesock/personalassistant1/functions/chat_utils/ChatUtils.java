package com.shoesock.personalassistant1.functions.chat_utils;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.caller.Caller;
import com.shoesock.personalassistant1.activities.reminder.Reminder;
import com.shoesock.personalassistant1.activities.sms.SMSAssistant;
import com.shoesock.personalassistant1.activities.topics.Topics;
import com.shoesock.personalassistant1.functions.caller_utils.CallerUtils;
import com.shoesock.personalassistant1.functions.reminder_utils.ReminderUtils;
import com.shoesock.personalassistant1.functions.sms_utils.SMSUtils;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

import java.util.ArrayList;
import java.util.Objects;

public class ChatUtils {

     Context context;
     Activity activity;
     LinearLayout chatContainer;
     ScrollView scrollView;

    ReminderUtils reminderUtils;
     TTSFunctions ttsFunctions;
     CallerUtils callerUtils;
     SMSUtils smsUtils;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;



    public ChatUtils (Context context1, Activity activity1, LinearLayout chatContainer1, ScrollView scrollView1, EditText messageEditText){
        context = context1;
        activity = activity1;
        ttsFunctions = new TTSFunctions(activity);
        chatContainer = chatContainer1;
        scrollView = scrollView1;
        reminderUtils = new ReminderUtils(context1, activity1, chatContainer, scrollView1);
        callerUtils = new CallerUtils(context1, activity1, messageEditText);
        smsUtils = new SMSUtils(context1, activity1);
    }



    public String doNotUnderstand(){


        String stringForUserUI, stringForSpeck;

        stringForUserUI =  "בשביל לעזור לך תגיד אחד מאלה: " +
                context.getString(R.string.toSendMessage)+ " , או , " +
                context.getString(R.string.toSpeak) + " , או , " +
                context.getString(R.string.toReminderMe) + " , או , " +
                context.getString(R.string.wantToDialer);

        stringForSpeck = "בִּשְׁבִיל לַעֲזֹר לְךָ תַּגִּיד אֶחָד מֵאֵלֶּה:" +
                context.getString(R.string.toSendMessage)+ " , או , " +
                context.getString(R.string.toSpeak) + " , או , " +
                context.getString(R.string.toReminderMe) + " , או , " +
                context.getString(R.string.wantToDialer);

        ttsFunctions.speak(stringForSpeck);


        return stringForUserUI;
    }




    public String getResponseForMainMenu(String userMessage) {     // this function to check if the user want the main menu

        Resources res = context.getResources();
        final Intent[] intent = new Intent[1];
        int secondsDelayed = 1;
        // this array string to get what user can say
        String[] cases = res.getStringArray(R.array.userWords);
        String appMessage = "";

        int i;
        for(i = 0; i < cases.length; i++)
            if(userMessage.contains(cases[i])) break;

        switch(i) {

            case 0: // this case user want message

                appMessage = context.getString(R.string.goToMessageScreen); // add the app response message and will return it.
                ttsFunctions.speak(appMessage); // speak the response
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // go to message screen
                        intent[0] = new Intent(context, SMSAssistant.class);
                        context.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000); // after adding response message to chat and speak it, will go to screen after 4 seconds
                break;

            case 1: //אני רוצה לדבר על משהו

                appMessage = context.getString(R.string.goToTopicScreen);
                ttsFunctions.speak(appMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(context, Topics.class);
                        context.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);
                break;

            case 2: //אני רוצה שתזכירי לי משהו

                appMessage = context.getString(R.string.goToReminderScreen);
                ttsFunctions.speak(appMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(context, Reminder.class);
                        context.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);
                break;


            case 3: // אני רוצה להתקשר למישהו

                appMessage = context.getString(R.string.goToAssistantDialerScreen);
                ttsFunctions.speak(appMessage);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent[0] = new Intent(context, Caller.class);
                        context.startActivity(intent[0]);
                    }
                }, secondsDelayed * 4000);

                break;

            default:
                // if not all screens want the user will add not understand
                appMessage =   doNotUnderstand();
        }
        // returning the app response
        return appMessage;
    } // close the getResponseForMainMenu function


    // i have some notes in the notebook מחברת

    public void msg(String witchActivity,EditText messageEditText, String userMessage){

        if (!userMessage.isEmpty()) {
            String assistantResponse = "";
                switch (witchActivity){
                    case "mainActivity":
                        assistantResponse = getResponseForMainMenu(userMessage);
                        break;
                    case "reminderActivity":
                        assistantResponse = reminderUtils.checkReminderMessage(userMessage);
                        break;
                    case "callerActivity":
                        assistantResponse = callerUtils.checkCallerMessage(userMessage);
                        break;
                    case "smsActivity":
                        assistantResponse = smsUtils.checkSmsUserRequest(userMessage);
                        break;
                    case "topicsActivity":
                        assistantResponse = "topicsActivity";
                        break;
                } // close the switch

            addMessage(chatContainer, scrollView, userMessage, true); // Add user message
            messageEditText.setText("");

            ttsFunctions.speak(assistantResponse);
            addMessage(chatContainer, scrollView, assistantResponse, false); //
        }

    } // // close the msg function

    public void addMessage(LinearLayout chatContainer, ScrollView scrollView,String message, boolean isUserMessage) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout chatItem = (LinearLayout) inflater.inflate(R.layout.chat_bubble, chatContainer, false);
        TextView messageText = chatItem.findViewById(R.id.messageText);
        messageText.setText(message);

        // Set appropriate background and gravity for user and app messages
        int backgroundResId = isUserMessage ? R.drawable.user_message_background : R.drawable.assestant_message_background;
        int gravity = isUserMessage ? Gravity.START : Gravity.END;
        chatItem.setBackgroundResource(backgroundResId);
        chatItem.setGravity(gravity);

        chatContainer.addView(chatItem);

        // Scroll to the bottom of the ScrollView
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    } // close the addMessage function


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data,  EditText messageEditText,  String witchActivity) {
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                messageEditText.setText(Objects.requireNonNull(result).get(0));
                String s = Objects.requireNonNull(result).get(0);
                msg(witchActivity,messageEditText,s); // to add the message to the chat and
            }
        }
    } // close the onActivityResult function




} // close ChatUtils class
