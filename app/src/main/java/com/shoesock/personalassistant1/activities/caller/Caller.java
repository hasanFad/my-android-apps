package com.shoesock.personalassistant1.activities.caller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.functions.Functions;
import com.shoesock.personalassistant1.functions.chat_utils.ChatUtils;
import com.shoesock.personalassistant1.speech.stt.STTFunctions;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

public class Caller extends AppCompatActivity {

    private Context context;
    private LinearLayout chatContainer;
    private EditText messageEditText;
    private ImageButton sendButton, micButton;
    private ScrollView scrollView;
    private   STTFunctions sttFunctions;
    private ChatUtils chatUtils;
    private TTSFunctions ttsFunctions;
    Functions functions;
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 1;
    private static final int PERMISSION_REQUEST_CALL_PHONE = 1;



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
        ttsFunctions = new TTSFunctions(Caller.this); // init the ttsFunction class
        sttFunctions = new STTFunctions(Caller.this);
        chatUtils = new ChatUtils(context, Caller.this, chatContainer, scrollView, messageEditText); // init the chat helper class
        functions = new Functions(Caller.this);
        setPointer();
    } // close the initClasses function

    private void setPointer() {
        int secondsDelayed = 1;

        chatUtils.addMessage(chatContainer, scrollView, getString(R.string.screenToCall), false);

        functions.checkCallPermissions(context); // wil check permissions
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatUtils.addMessage(chatContainer, scrollView,getString(R.string.howToCall), false);
                ttsFunctions.speak(getString(R.string.howToCall));

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
                chatUtils.msg("callerActivity" ,messageEditText, userMessage);
            }
        });

    } // close the setPointer Function


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatUtils.onActivityResult(requestCode, resultCode, data, messageEditText, "callerActivity");

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            if (contactUri != null) {
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = cursor.getString(phoneNumberColumnIndex);
                    cursor.close();
                    // Set the selected phone number to the EditText
                    messageEditText.setText(phoneNumber);
                }
            }
        }
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
