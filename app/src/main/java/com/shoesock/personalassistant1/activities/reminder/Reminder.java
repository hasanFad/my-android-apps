package com.shoesock.personalassistant1.activities.reminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.chat_functions.ChatUtils;
import com.shoesock.personalassistant1.functions.reminder_functions.ReminderUtils;
import com.shoesock.personalassistant1.speech.stt.STTFunctions;
import com.shoesock.personalassistant1.models.ReminderModel;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

import java.util.Date;

public class Reminder extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText messageEditText;
    private ImageButton sendButton, micButton;
    private ScrollView scrollView;
    private ReminderModel reminderModel;
    private TTSFunctions ttsFunctions;
    private  ChatUtils chatUtils;
    private String userMessage;
    private STTFunctions sttFunctions;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIds();



    } // close the onCreate function


        // set the id for elements and buttons click functions
    private void setIds(){

        // for this layout
        chatContainer = findViewById(R.id.chatContainer);
        messageEditText = findViewById(R.id.messageEditText);
        scrollView = findViewById(R.id.scrollView_activityChat);
        micButton = findViewById(R.id.micImageButton);
        sendButton = findViewById(R.id.sendImageButton);


        classesInit();

    } // close the setIds function

    private void classesInit() {
        checkReminderInBundle();
        Context context = this;
        chatUtils = new ChatUtils(context, Reminder.this, chatContainer, scrollView); // Pass "this" (Activity)
        sttFunctions = new STTFunctions(Reminder.this);
        ttsFunctions = new TTSFunctions(Reminder.this);
        reminderModel = null;
        setPointer();
    }

    private void checkReminderInBundle() {

        Intent intent = getIntent();
        reminderModel = (ReminderModel) intent.getSerializableExtra("reminder_Kay");
        if (reminderModel != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("userName", null);

            // we have a reminder object. now will prompt to user if he want to change it.
            chatUtils.addMessage(chatContainer, scrollView, getString(R.string.confirmTheData), false);
            ttsFunctions.speak(getString(R.string.confirmTheData));

        } // close the if model != null


    } // close checkReminderInBundle function

    private void setPointer() {
        int secondsDelayed = 1;
        chatUtils.addMessage(chatContainer, scrollView, getString(R.string.thisScreenForReminder), false);

        // explain to user how can reminder start to speak after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatUtils.addMessage(chatContainer, scrollView,getString(R.string.howCanReminderWithout), false);
                ttsFunctions.speak(getString(R.string.howCanReminder));


            }
        }, secondsDelayed * 3000);


        // add an example for reminder
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatUtils.addMessage(chatContainer, scrollView,getString(R.string.exampleForReminderWithout), false);
                ttsFunctions.speak(getString(R.string.exampleForReminder));
            }
        }, secondsDelayed * 41000);


        //  will ask about reminder date
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatUtils.addMessage(chatContainer, scrollView,getString(R.string.whatReminderDateWithout), false);
                ttsFunctions.speak(getString(R.string.whatReminderDate));
                messageEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);

            }
        }, secondsDelayed * 60000);



        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to listen to microphone
                sttFunctions.listenToMic();

            }
        }); // close the micButton .setOnClickListener function


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get string the user clicked it and check it
                userMessage = messageEditText.getText().toString();
                chatUtils.msg(messageEditText, userMessage);

            }
        }); // close the sendButton.setOnClickListener function

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatUtils.onActivityResult(requestCode, resultCode, data, messageEditText);

    } // close onActivityResult function

    @Override
    protected void onPause() {
        super.onPause();
        // Pause TTS when the activity is paused
        if (ttsFunctions != null) {
            ttsFunctions.stop();
        }
    } // close the onPause function

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release TTS resources when the activity is destroyed

        if (ttsFunctions != null){
            ttsFunctions.shutdown();
        }
    } // close the onDestroy function


} // close the Reminder class




