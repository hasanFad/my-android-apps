package com.shoesock.personalassistant1.functions;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions implements Serializable {



    private Activity activity;



    public Functions (Activity activity){
        this.activity = activity;

    }

    public void ToastFunction(Context context, String textToToast){

        Toast.makeText(context, textToToast, Toast.LENGTH_SHORT).show();
    } // close the ToastFunction function


    // Check if the input string matches the "dd\\mm" date format
    public boolean isValidDate(String input) {
        String dateRegex = "\\d{2}/\\d{2}/\\d{4}";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // Check if the input string matches the "hh:mm" time format
    public boolean isValidTime(String input) {
        String timeRegex = "\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }





} // close the Functions class
