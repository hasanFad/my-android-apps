package com.shoesock.personalassistant1.functions;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shoesock.personalassistant1.activities.caller.Caller;
import com.shoesock.personalassistant1.activities.login.Login;
import com.shoesock.personalassistant1.functions.chat_utils.ChatUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions implements Serializable {

    private Activity activity;
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int PERMISSION_REQUEST_READ_CONTACTS = 1;
    private static final int PERMISSION_REQUEST_CALL_PHONE = 1;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 1;
    private static final int PERMISSION_REQUEST_INTERNET = 1;


    public Functions (Activity activity){
        this.activity = activity;
    } // close Functions function

    public void ToastFunction(Context context, String textToToast){

        Toast.makeText(context, textToToast, Toast.LENGTH_SHORT).show();
    } // close the ToastFunction function

    // Check if the input string matches the "dd\\mm" date format
    public boolean isValidDate(String input) {
        String dateRegex = "\\d{2}/\\d{2}/\\d{4}";
        Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    } // close isValidDate function

    // Check if the input string matches the "hh:mm" time format
    public boolean isValidTime(String input) {
        String timeRegex = "\\d{2}:\\d{2}";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    } // close isValidTime function

    public boolean isValidPhoneNumber(String userMessage) {
        String phoneNumber = userMessage.replace(" ","");
        return Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length() == 10;
    } // close isValidPhoneNumber function

    // Validate and save reminder
    public void validateAndSaveReminder(String sGetDate, String sGetTime, String sGetContent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            Date parsedDate = dateFormat.parse(sGetDate);
            Date parsedTime = timeFormat.parse(sGetTime);

            // Get current date and time
            Calendar currentCalendar = Calendar.getInstance();
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.setTime(parsedDate);
            selectedCalendar.set(Calendar.HOUR_OF_DAY, parsedTime.getHours());
            selectedCalendar.set(Calendar.MINUTE, parsedTime.getMinutes());

            if (parsedDate.before(currentCalendar.getTime()) ||
                    parsedTime.before(currentCalendar.getTime())) {
                // The selected date or time is in the past
                // Show an error message or prompt the user to select a future date/time
            } else {
                // The selected date and time are valid
                // Save the reminder
                // ...
            }
        } catch (ParseException e) {
            // Handle invalid date or time format
            // Show an error message or prompt the user to enter a valid date/time
        }
    } // close validateAndSaveReminder function


    public void openContacts(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, PICK_CONTACT_REQUEST);
    } // close openContacts function

    public void callPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    } // close callPhoneNumber function


    public void checkCallPermissions(Context context) {
        // Check and request permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_PHONE);
        }

    } // close checkCallPermissions function

    public void checkAllPermissionsNeeded(Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CALL_PHONE);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_INTERNET);
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_RECORD_AUDIO);
        }



    } // close checkAllPermissionsNeeded function


    public void sendToWhatsApp(String nameOrNumber, String messageContent) {
        if (isValidPhoneNumber(nameOrNumber)) {

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + nameOrNumber + "&text=" + messageContent);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    // for menu
    public void exitAndDeleteUsername() {
        // Delete the username from shared preferences
        SharedPreferences preferences = activity.getSharedPreferences("loginPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("username");
        editor.apply();

        // Navigate to the login activity
        Intent intent = new Intent(activity, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }



} // close the Functions class
