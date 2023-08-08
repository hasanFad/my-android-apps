package com.shoesock.personalassistant1.activities.dialer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.functions.chat_functions.ChatUtils;
import com.shoesock.personalassistant1.speech.stt.STTFunctions;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

public class AssistantDialer extends AppCompatActivity {

    private Context context;
    private LinearLayout chatContainer;
    private EditText messageEditText;
    private ImageButton sendButton, micButton;
    private ScrollView scrollView;
    private   STTFunctions sttFunctions;
    private ChatUtils chatUtils;
    private TTSFunctions ttsFunctions;

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

        initClasses();

    } // close the setIds function

    private void initClasses() {
        ttsFunctions = new TTSFunctions(AssistantDialer.this); // init the ttsFunction class
        sttFunctions = new STTFunctions(AssistantDialer.this);
        chatUtils = new ChatUtils(context, AssistantDialer.this, chatContainer, scrollView); // init the chat helper class

        setPointer();
    } // close the initClasses function

    private void setPointer() {

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
                chatUtils.msg(messageEditText, userMessage);
            }
        });

    } // close the setPointer Function


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


} // close the AssistantDialer class
