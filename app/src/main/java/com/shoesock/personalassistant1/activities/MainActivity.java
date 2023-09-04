package com.shoesock.personalassistant1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.chat_utils.ChatUtils;
import com.shoesock.personalassistant1.speech.stt.STTFunctions;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;



public class MainActivity extends AppCompatActivity {

    // will get at the start of the app the assistant's commands to say (the greeting and what can i do)
    //  get all of voice and chat commands from the user after this will go to the special layout

    private LinearLayout chatContainer;
    private EditText messageEditText;
    private ImageButton sendButton, micButton;
    private ScrollView scrollView;
    public   ChatUtils chatUtils;
    private String greetingSpeech, greetingMsg;
    private   STTFunctions sttFunctions;
    Functions functions;
    private   String username;

    private Context context;

     TTSFunctions ttsFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIds();

    }

    private void setIds() {

        context = this;
        chatContainer = findViewById(R.id.chatContainer); // the LinearLayout
        messageEditText = findViewById(R.id.messageEditText);
        scrollView = findViewById(R.id.scrollView_activityChat);
        micButton = findViewById(R.id.micImageButton); // button to operation the microphone
        sendButton = findViewById(R.id.sendImageButton); // button to send the message

        classesInit(); // init the classes and the greetingSpeech


    } // close the onCreate function

    private void classesInit() {
        functions = new Functions(MainActivity.this);
        sttFunctions = new STTFunctions(this); // init speech to text class
        chatUtils = new ChatUtils(context, MainActivity.this, chatContainer, scrollView, messageEditText); // init the chat helper class
        ttsFunctions = new TTSFunctions(MainActivity.this); // init the ttsFunction class
                // set the username at shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginPreferences", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("userName", null);

        greetingSpeech = getString(R.string.assistantGreeting) + username+ "." + getString(R.string.assistantName) + getString(R.string.assistantDescription); // string to speech
        greetingMsg = getString(R.string.assistantGreetingWithout) + username +"." + getString(R.string.assistantNameWithout) + getString(R.string.assistantDescriptionWithout); // string to chat

        setPointer(); // will do all activity operation

    } // close the classesInit class

    private void setPointer() {

        functions.checkAllPermissionsNeeded(context);
        ttsFunctions.speak(greetingSpeech); // greeting on speak function
        chatUtils.addMessage(chatContainer, scrollView, greetingMsg, false);

        int secondsDelayed = 1;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String donNotUnderstand =  chatUtils.doNotUnderstand();
                chatUtils.addMessage(chatContainer, scrollView, donNotUnderstand, false);
            }
        }, secondsDelayed * 17000);

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
                chatUtils.msg("mainActivity" ,messageEditText, userMessage);

            }
        });

    } // close the setPointer function

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatUtils.onActivityResult(requestCode, resultCode, data, messageEditText, "mainActivity");

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




} // close the mainActivity class