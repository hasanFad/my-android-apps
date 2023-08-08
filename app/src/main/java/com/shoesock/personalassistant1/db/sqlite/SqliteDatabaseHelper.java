package com.shoesock.personalassistant1.db.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shoesock.personalassistant1.models.ReminderModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminders_and_tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "reminder_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "reminder_date";
    private static final String COLUMN_TIME = "reminder_time";
    private static final String COLUMN_CONTENT = "reminder_content";

    private static SqliteDatabaseHelper instance;
    private SQLiteDatabase database;

    public static synchronized SqliteDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SqliteDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private SqliteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_TIME + " DATE, " +
                COLUMN_CONTENT + " TEXT);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }

    // Function to save reminder data
    public long saveReminder(Date date, Date time, String content) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date.getTime());
        values.put(COLUMN_TIME, time.getTime());
        values.put(COLUMN_CONTENT, content);
        return database.insert(TABLE_NAME, null, values);
    }

    // Function to read all reminder data
    public List<ReminderModel> readAllReminders() {
        List<ReminderModel> reminders = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

                ReminderModel reminder = new ReminderModel(date, time, content);
                reminders.add(reminder);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reminders;
    }
}
