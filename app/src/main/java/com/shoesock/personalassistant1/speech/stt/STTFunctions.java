package com.shoesock.personalassistant1.speech.stt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

import java.util.Locale;

public class STTFunctions {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

     Activity activity;
    TTSFunctions ttsFunctions;


    public STTFunctions (Activity activity){
        this.activity = activity;
        ttsFunctions = new TTSFunctions(activity);

    }


    // listenToMic function it's to listen to microphone will used in all the activities
    public void listenToMic(){
        ttsFunctions.stop(); // stop the speak if speaking

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        try {
            activity.startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
            Toast.makeText(activity, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





} // close the STTFunctions class
