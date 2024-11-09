package com.example.alarmer;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmApplication extends Application {
    private static final String DB_NAME = "db_alarmer";
    private static final int DB_VERSION = 2;
    private SQLiteOpenHelper helper;
    private boolean isForeground = true;

    private AlarmsFragment alarmsFragment;

    public void setAlarmsFragment(AlarmsFragment alarmsFragment) {
        this.alarmsFragment = alarmsFragment;
    }

    public AlarmsFragment getAlarmsFragment() {
        return alarmsFragment;
    }

    // Define the table name and columns
    private static final String TABLE_NAME = "tb_alarms";
    private static final String TABLE_USER = "tb_user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ENABLED = "enabled";

    private static final String COLUMN_USER = "user";
    private static final String COLUMN_PASSWORD = "password";


    @Override
    public void onCreate() {

        super.onCreate();

        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String createTableSql = "CREATE TABLE " + TABLE_NAME + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_TIME + " TEXT, "
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_ENABLED + " INTEGER DEFAULT 0)";
                db.execSQL(createTableSql);

                String createTableUser = "CREATE TABLE " + TABLE_USER + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_USER + " TEXT, "
                        + COLUMN_PASSWORD + " TEXT)";
                db.execSQL(createTableUser);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
                onCreate(db);
            }
        };

    }


    // luu thong tin bao thuc vao db
    public int addAlarm(Alarm alarm) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, alarm.getTime());
        values.put(COLUMN_TITLE, alarm.getTitle());
        values.put(COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);

        long newRowId = db.insert(TABLE_NAME, null, values);

        db.close();
        return (int) newRowId;
    }
    public boolean check_All(String userName, String passWord) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{userName, passWord});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public  boolean insertAccount( String USERNAME, String PASSWORD){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER, USERNAME);
        values.put(COLUMN_PASSWORD, PASSWORD);

        long result = db.insert( TABLE_USER, null, values);
        db.close();
        return result != -1;

    }
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER,
                    new String[]{COLUMN_USER},
                    COLUMN_USER + "=?",
                    new String[]{username},
                    null, null, null);
            return cursor != null && cursor.moveToFirst();
        } catch (SQLException e) {
            Log.e("error", "Lỗi truy vấn");
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    public List<Alarm> loadAlarms() {

        List<Alarm> alarmList = new ArrayList<>();
//        alarmList.add(new Alarm(1, "8:00 AM", "Wake Up", true));
//        alarmList.add(new Alarm(2, "3:00 PM", "Gym Time", true));
//        return alarmList;
        // Xác định các cột bạn muốn lấy từ bảng
        String[] projection = {
                COLUMN_ID,
                COLUMN_TIME,
                COLUMN_TITLE,
                COLUMN_ENABLED
        };

        // Sắp xếp kết quả theo ID tăng dần
        String sortOrder = COLUMN_ID + " ASC";
        SQLiteDatabase db = helper.getReadableDatabase();
        // Truy vấn bảng và lấy kết quả
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,                            // Các cột để lấy
                null,                                  // Các cột cho mệnh đề WHERE
                null,                                  //Các giá trị của mệnh đề WHERE
                null,
                null,
                sortOrder                              //Thứ tự sắp xếp
        );

        // Loop through the results and add each row to the list
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            boolean enabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENABLED)) == 1;

            Alarm alarm = new Alarm(id, time, title, enabled);
            alarmList.add(alarm);
        }

        // Close the cursor and return the list
        cursor.close();
        return alarmList;
    }

    public void updateAlarm(Alarm alarm) {
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111");
        System.out.println(alarm.getId());
        System.out.println(alarm.isEnabled());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //we are just updating the enabled column
        values.put(COLUMN_ENABLED, alarm.isEnabled());
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(alarm.getId())};
        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }


    public boolean deleteAlarm(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
//        if (deletedRows > 0) {
//            mAlarms.remove(getAlarmIndex(id));
//            notifyDataSetChanged();
//        }
    }


}
