package com.shoesock.personalassistant1.activities.sms;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.activities.MainActivity;
import com.shoesock.personalassistant1.functions.chat_utils.ChatUtils;
import com.shoesock.personalassistant1.speech.stt.STTFunctions;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

public class SMSAssistant extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText messageEditText;
    private ImageButton sendButton, micButton;
    private ScrollView scrollView;
    private Context context;
    private STTFunctions sttFunctions;
    private ChatUtils  chatUtils;
    TTSFunctions ttsFunctions;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIds();

    } // close the onCreate function

    private void setIds() {

        context = this;
        chatContainer = findViewById(R.id.chatContainer);
        messageEditText = findViewById(R.id.messageEditText);
        scrollView = findViewById(R.id.scrollView_activityChat);
        micButton = findViewById(R.id.micImageButton);
        sendButton = findViewById(R.id.sendImageButton);

        classesInit();

    } // close the setIds function

    private void classesInit() {
         sttFunctions = new STTFunctions(SMSAssistant.this);
        chatUtils = new ChatUtils(context, SMSAssistant.this, chatContainer, scrollView, messageEditText); // init the chat helper class
        ttsFunctions = new TTSFunctions(SMSAssistant.this); // init the ttsFunction class

         setPointer();
    }

    private void setPointer() {
        chatUtils.addMessage(chatContainer,scrollView,"זהו מסך המטפל בשליחת הודעות.", false);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ttsFunctions.speak(getString(R.string.wantToSendWhatsAppOrSMS)); // speak function

                chatUtils.addMessage(chatContainer,scrollView,getString(R.string.wantToSendWhatsAppOrSMS), false);

            }
        }, secondsDelayed * 3000);



        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sttFunctions.listenToMic();

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userMessage = messageEditText.getText().toString();

                chatUtils.msg("smsActivity" ,messageEditText, userMessage);
            }
        });


    } // close the setPointer function

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatUtils.onActivityResult(requestCode, resultCode, data, messageEditText, "smsActivity");

    } // close the onActivityResult function


} // close the SMSAssistant class
