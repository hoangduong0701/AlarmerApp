package com.example.alarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class StopAlarmActivity extends AppCompatActivity {

    private int alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        View view = findViewById(R.id.stop_alarm_container);
        Snackbar.make( view, "The alarm is ringing", Snackbar.LENGTH_LONG).show();

        alarmId = getIntent().getIntExtra("id", -1);

        //Thêm trình nghe nhấp chuột vào nút dừng
        Button btnStop = findViewById(R.id.button_stop_alarm);
        TextView titleTextView = findViewById(R.id.titleTextView);

        titleTextView.setText(getIntent().getStringExtra("title"));
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vô hiệu hóa báo động
                AlarmApplication app = (AlarmApplication) getApplicationContext();
                app.updateAlarm(new Alarm(alarmId,"","",false));
                //Dừng báo thức
                stopAlarm();

                //Kết thúc hoạt động
                finish();
            }
        });
    }

    private void stopAlarm() {
        // Dừng âm thanh báo động
        Intent intent = new Intent(this.getApplicationContext(), AlarmService.class);
        stopService(intent);
        // Hủy thông báo
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(alarmId);

        intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);


        // TODO: Update the database to mark the alarm as disabled
    }
}