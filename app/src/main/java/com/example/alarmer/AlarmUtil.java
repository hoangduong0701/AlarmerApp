package com.example.alarmer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmUtil {


    public static void cancelAlarm(Context context, Alarm alarm) {
       AlarmApplication app = (AlarmApplication) context.getApplicationContext();

        app.updateAlarm(alarm);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {

            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "The alarm was disabled", Toast.LENGTH_SHORT).show();
    }


    public static void setAlarm(Context context, Alarm alarm){

        AlarmApplication app = (AlarmApplication) context.getApplicationContext();


        String[] parts = alarm.getTime().split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        String toastMessage = "Báo thức được bật cho ngày hôm nay";

        //Đặt thời gian báo thức sẽ đổ chuông
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // Thêm một ngày vào lịch
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            toastMessage = "Báo thức được bật cho ngày mai";
        }

        app.updateAlarm(alarm);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", alarm.getTitle());
        intent.putExtra("id", alarm.getId());

        // Tạo một PendingIntent sẽ khởi động AlarmReceiver
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else {
            pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        // Lên lịch báo thức
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

}
