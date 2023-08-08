package com.shoesock.personalassistant1.functions.chat_functions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shoesock.personalassistant1.R;
import com.shoesock.personalassistant1.speech.tts.TTSFunctions;

import java.util.ArrayList;
import java.util.Objects;

public class ChatMessages {

    private Activity activity;
    private Context context;
    private TTSFunctions ttsFunctions;

    public ChatMessages(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        ttsFunctions = new TTSFunctions(activity);
    }



} // close the ChatMessages class
