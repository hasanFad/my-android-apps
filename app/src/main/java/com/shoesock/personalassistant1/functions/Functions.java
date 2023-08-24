package com.shoesock.personalassistant1.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shoesock.personalassistant1.functions.chat_utils.ChatUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions implements Serializable {



    private Activity activity;



    private static final int PICK_CONTACT_REQUEST = 1;

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

    public boolean isValidPhoneNumber(String phoneNumber) {
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


    public String callContactsNameIfExists(String userMessage){
            String returnIfNotExist = "";
        Cursor cursor = activity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?",
                new String[]{"%" + userMessage +"%" },
                null
        );

        if (cursor.getCount() > 0) {
            // Name exists in contacts, initiate a call
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String phoneNumberToCall = "tel:" + phoneNumber;
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumberToCall));
                activity.startActivity(dialIntent);
            }
        } else {
            // Name not found, perform TTS and open contacts
           returnIfNotExist = "השם לא נמצא, פותח אנשי קשר";
            openContacts();
        }

        return returnIfNotExist;
    } // close callContactsNameIfExists function



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









        } // close the Functions class
