package com.shoesock.personalassistant1.speech.tts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import com.shoesock.personalassistant1.R;

import java.util.HashMap;
import java.util.Locale;

public class TTSFunctions {

    private static final int TTS_ENGINE_REQUEST_CODE = 1;
    private final Activity activity;
    private TextToSpeech textToSpeech;
    boolean isInitialized ;

    String text1;

    Context context;

    // New constructor with both Activity and Context parameters
    public TTSFunctions(Activity activity) {
        this.activity = activity;
        initializeTextToSpeech();
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(activity, status -> {
            if (status == TextToSpeech.SUCCESS) {
                Locale locale = new Locale("he", "IL");
                int result = textToSpeech.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTSFunctions", "Hebrew language is not supported on the device.");
                    promptLanguageInstallation();
                    Toast.makeText(activity, "Hebrew language is not supported on the device", Toast.LENGTH_SHORT).show();
                } else {
                    isInitialized = true;
                    speakGreetingMessage(text1);
                }
            } else {
                Log.e("TTSFunctions", "Text-to-speech initialization failed.");
                Toast.makeText(activity, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Utterance started
            }

            @Override
            public void onDone(String utteranceId) {
                // Utterance completed
            }

            @Override
            public void onError(String utteranceId) {
                // Utterance encountered an error
            }
        });
    }

    private void promptLanguageInstallation() {
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        activity.startActivity(installIntent);
    }

    private void promptTTSEngineInstallation() {
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        ((Activity) activity).startActivityForResult(installIntent, TTS_ENGINE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_ENGINE_REQUEST_CODE) {
            initializeTextToSpeech();
        }
    }

    public void speak(String text) {
        if (textToSpeech != null) {
            text1 = text;
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void speakGreetingMessage(String string) {
        String greetingMessage = activity.getString(R.string.assistantName);

        if (isInitialized && textToSpeech != null) {
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "GreetingUtterance");
            textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, params);
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }


} // close the TTSFunctions class
