package com.shoesock.personalassistant1.functions.contact_utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shoesock.personalassistant1.Manifest;
import com.shoesock.personalassistant1.functions.Functions;

import java.util.ArrayList;
import java.util.List;

public class ContactUtils {

    Activity activity;
    Context context;
    Functions functions;
    public ContactUtils(Activity activity1, Context contex1){
        this.activity = activity1;
        this.context = contex1;
        functions = new Functions(activity1);
    }

    public String searchContactByName(String userMessage) {
        // Check if the app has permission to read contacts
        functions.checkAllPermissionsNeeded(context);

        // Perform the contact search
        Cursor cursor = activity.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?",
                new String[]{"%" + userMessage + "%"},
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            // Handle the case where multiple contacts match the search
            StringBuilder names = new StringBuilder();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                names.append(contactName).append(", ");
            }
            cursor.close();
            return "נמצא מספר אנשי קשר : " + names.toString();

        } else if (cursor != null) {
            // No matching contact found
            cursor.close();
            return "לא נמצא איש קשר תואם.";

        } else {
            // Cursor is null, indicating a permission issue
            functions.checkAllPermissionsNeeded(context);
            return "נדרש הרשאה לקריאת אנשי קשר.";
        }
    }

} // close the ContactUtils class
