package com.example.sangy.arduinonandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends AppCompatActivity {
    private SimpleSideDrawer simpleSideDrawer;
    private ImageView candle;
    private TextView alarm;
    private TextView brightness;
    private TextView logout;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View main = inflater.inflate(R.layout.activity_main, null);
        setContentView(main);
        startActivity(new Intent(this,Splash.class));
        startActivityForResult(new Intent(this, LoginActivity.class),1000);
        simpleSideDrawer = new SimpleSideDrawer(this);
        simpleSideDrawer.setRightBehindContentView(R.layout.right_menu);
        candle = (ImageView)findViewById(R.id.candle);
        alarm = (TextView)findViewById(R.id.alarm);
        brightness = (TextView)findViewById(R.id.brightness);
        logout = (TextView)findViewById(R.id.logout);

        main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("candle","슬라이드");
                simpleSideDrawer.toggleRightDrawer();
                return false;
            }
        });

        Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                DeviceStatus2  ds2 = gson.fromJson(msg.obj.toString(), DeviceStatus2.class);
                DeviceStatus.setStatus(ds2.getStatus());
                DeviceStatus.setBright_sta(ds2.getBright_sta());
                DeviceStatus.setFall(ds2.getFall());

                //기기 넘어짐 알림
                if(DeviceStatus.getFall() == 1) Toast.makeText(getApplicationContext(),"쓰러졌습니다.",Toast.LENGTH_SHORT).show();
                Log.d("candle","status : " + DeviceStatus.getStatus());
                //켜짐상태에 따른 조치
                if(DeviceStatus.getStatus() == 1){
                    candle.setVisibility(View.VISIBLE);
                }
                else {
                    candle.setVisibility(View.INVISIBLE);
                    //꺼졌을 때
                }
            }
        };

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BrightnessActivity.class));
            }
        });

        LoopingThread thread = new LoopingThread(mHandler);
        thread.setDaemon(true);
        thread.start();

        candle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceStatus.setStatus_change(1);

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
