package com.example.sangy.arduinonandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

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
        timePickerStart.setHour(12);
        timePickerStart.setMinute(5);
        timePickerStop.setHour(13);
        timePickerStop.setMinute(34);


    }


    public void release(View view){
        int startHour = timePickerStart.getHour();
        int startMinute = timePickerStart.getMinute();
        int stopHour = timePickerStop.getHour();
        int stopMinute = timePickerStop.getMinute();
        finish();
    }
    public void setting(View view){
        finish();
    }
}
