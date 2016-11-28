package com.example.sangy.arduinonandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.R.attr.start;

/**
 * Created by sangy on 2016-11-13.
 */

public class AlarmActivity extends AppCompatActivity {
    TimePicker timePickerStart;
    TimePicker timePickerStop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        timePickerStart = (TimePicker)findViewById(R.id.timePickerStart);
        timePickerStop = (TimePicker)findViewById(R.id.timePickerStop);
        timePickerStart.setHour(Integer.parseInt(DeviceStatus.getAlarm_start().substring(0,2)));
        timePickerStart.setMinute(Integer.parseInt(DeviceStatus.getAlarm_start().substring(2)));
        timePickerStop.setHour(Integer.parseInt(DeviceStatus.getAlarm_end().substring(0,2)));
        timePickerStop.setMinute(Integer.parseInt(DeviceStatus.getAlarm_end().substring(2)));
    }
    public void release(View view){
        DeviceStatus.setAlarm_set(0);
        MainActivity.editor.putInt("alarm_set", 0);
        Toast.makeText(getApplicationContext(),"해제 되었습니다",Toast.LENGTH_SHORT).show();
        finish();
    }
    public void setting(View view){
        String startAlarm = "";
        String endAlarm = "";
        int startHour = timePickerStart.getHour();
        startAlarm += (startHour < 10) ? "0" + startHour : startHour;
        int startMinute = timePickerStart.getMinute();
        startAlarm += (startMinute < 10) ? "0" + startMinute : startMinute;
        int stopHour = timePickerStop.getHour();
        endAlarm += (stopHour < 10) ? "0" + stopHour : stopHour;
        int stopMinute = timePickerStop.getMinute();
        endAlarm += (stopMinute < 10) ? "0" + stopMinute : stopMinute;
        DeviceStatus.setAlarm_start(startAlarm);
        DeviceStatus.setAlarm_end(endAlarm);
        DeviceStatus.setAlarm_set(1);
        MainActivity.editor.putString("alarm_start", startAlarm);
        MainActivity.editor.putString("alarm_end", endAlarm);
        MainActivity.editor.putInt("alarm_set", 1);
        Toast.makeText(getApplicationContext(),"설정 되었습니다",Toast.LENGTH_SHORT).show();
        finish();
    }
    public void cancel(){
        finish();
    }
}
