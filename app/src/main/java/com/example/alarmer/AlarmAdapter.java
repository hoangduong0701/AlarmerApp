package com.example.alarmer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context context;

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    private List<Alarm> alarmList;
    private AlarmApplication app;

    public AlarmAdapter(Context context, List<Alarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
        this.app = (AlarmApplication) context.getApplicationContext();
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alarm_item_layout, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {

        Alarm alarm = alarmList.get(position);
        holder.idTextView.setText(alarm.getId() + "");
        holder.timeTextView.setText(alarm.getTime());
        holder.titleTextView.setText(alarm.getTitle());
        holder.enabledSwitch.setChecked(alarm.isEnabled());
        holder.enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    alarm.setEnabled(false);
                    AlarmUtil.cancelAlarm(context, alarm);
                } else {
                    alarm.setEnabled(true);
                    AlarmUtil.setAlarm(context, alarm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        if (alarmList == null) {
            return 0;
        }
        return alarmList.size();
    }

    public void deleteAlarm(int position) {
        if (alarmList == null || alarmList.isEmpty()) {
            return; // trả về nếu danh sách rỗng hoặc trống
        }
        Alarm alarm = alarmList.get(position);
        alarmList.remove(position);
        if (app.deleteAlarm(alarm.getId())) {
            System.out.println("*********************************************");
            Toast.makeText(context, "Báo thức đã bị xóa", Toast.LENGTH_SHORT).show();
        }

        //nó chỉ báo với bộ chuyển đổi rằng mục đó đã bị xóa
        notifyItemRemoved(position);

    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {

        public TextView idTextView;
        public TextView timeTextView;
        public TextView titleTextView;
        public Switch enabledSwitch;

        public AlarmViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.idTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            enabledSwitch = itemView.findViewById(R.id.enabledSwitch);

        }
    }


}

