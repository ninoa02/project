package com.example.sangy.arduinonandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.navdrawer.SimpleSideDrawer;

public class MainActivity extends AppCompatActivity {
    private SimpleSideDrawer simpleSideDrawer;
    private ImageView candle;
    private TextView alarm;
    private TextView brightness;
    private TextView connectionCycle;
    private TextView logout;
    private Gson gson = new Gson();
    private LoopingThread thread;
    private Handler mHandler;
    private boolean fallFlag;
    static SharedPreferences mPreferences;
    static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        메인화면이 뜬다
        LayoutInflater inflater = getLayoutInflater();
        View main = inflater.inflate(R.layout.activity_main, null);
        setContentView(main);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mPreferences.edit();
        DeviceStatus.setDevice_no(mPreferences.getInt("device_no",0));
        DeviceStatus.setBright_sta(mPreferences.getInt("bright_set",0));



//        스플래시(로딩화면)이 뜬다
        startActivity(new Intent(this,Splash.class));

//        로그인 화면을 띄운다
        startActivityForResult(new Intent(this, LoginActivity.class),1000);

        simpleSideDrawer = new SimpleSideDrawer(this);
        simpleSideDrawer.setRightBehindContentView(R.layout.right_menu);
        candle = (ImageView)findViewById(R.id.candle);
        alarm = (TextView)findViewById(R.id.alarm);
        connectionCycle = (TextView)findViewById(R.id.connectionCycle);
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

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                DeviceStatus2  ds2 = gson.fromJson(msg.obj.toString(), DeviceStatus2.class);
                DeviceStatus.setStatus(ds2.getStatus());
                DeviceStatus.setBright_sta(ds2.getBright_sta());
                DeviceStatus.setFall(ds2.getFall());

                //기기 넘어짐 알림
                if(DeviceStatus.getFall() == 0) fallFlag = false;
                if(DeviceStatus.getFall() == 1 && fallFlag == false) Toast.makeText(getApplicationContext(),"쓰러졌습니다.",Toast.LENGTH_SHORT).show();
                //켜짐상태에 따른 조치
                if(DeviceStatus.getStatus() == 1){
                    candle.setVisibility(View.VISIBLE);
                }
                else {
                    candle.setImageAlpha(50);
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
                View brView = getLayoutInflater().inflate(R.layout.activity_brightness, null);
                final SeekBar seekBar = (SeekBar)brView.findViewById(R.id.seekBar);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("조도 설정");
                seekBar.setSecondaryProgress(DeviceStatus.getBright_sta());
                dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putInt("bright_set",seekBar.getProgress());
                        DeviceStatus.setBright_set(seekBar.getProgress());
                        Toast.makeText(MainActivity.this,"저장되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.setView(brView);

            }
        });
        connectionCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통신주기설정 완성할 것
//                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                dlg.setTitle("통신주기 설정");
//                dlg.setSingleChoiceItems();
//                dlg.setPositiveButton("저장");


            }
        });

        candle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceStatus.setStatus_change(1);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            thread = new LoopingThread(mHandler);
            thread.setDaemon(true);
            thread.start();
        }
    }

}
