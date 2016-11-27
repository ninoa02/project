package com.example.sangy.arduinonandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.gson.Gson;

import static com.example.sangy.arduinonandroid.R.id.alarm;
import static com.example.sangy.arduinonandroid.R.id.brightness;
import static com.example.sangy.arduinonandroid.R.id.logout;

public class MainActivity extends AppCompatActivity {
    private ImageView candle;
    private Gson gson = new Gson();
    private LoopingThread thread;
    private Handler mHandler;
    private boolean fallFlag;
    private boolean brightFlag;
    static SharedPreferences mPreferences;
    static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        candle = (ImageView)findViewById(R.id.candle);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mPreferences.edit();

        //설정값을 읽어온다
        DeviceStatus.setBright_set(mPreferences.getInt("bright_set",0));
        DeviceStatus.setConnection_cycle(mPreferences.getInt("connection_cycle",0));
        DeviceStatus.setAlarm_set(mPreferences.getInt("alarm_set",0));
        DeviceStatus.setAlarm_start(mPreferences.getString("alarm_start", null));
        DeviceStatus.setAlarm_end(mPreferences.getString("alarm_end", null));

//        스플래시(로딩화면)를 호출한다
        startActivity(new Intent(this,Splash.class));

//        로그인 화면을 호출한다
        startActivityForResult(new Intent(this, LoginActivity.class),1000);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                DeviceStatus2  ds2 = gson.fromJson(msg.obj.toString(), DeviceStatus2.class);
                DeviceStatus.setStatus(ds2.getStatus());
                DeviceStatus.setBright_sta(ds2.getBright_sta());
                DeviceStatus.setFall(ds2.getFall());
                fall();//넘어짐처리
                status();//전구상태 처리
            }
        };

        candle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceStatus.setStatus_change(1);
            }
        });
    }

    public void fall(){
        //기기 넘어짐 알림
        fallFlag = (DeviceStatus.getFall() == 1) ? false : true;
        if(DeviceStatus.getFall() == 0 && fallFlag == false){
            Toast.makeText(getApplicationContext(),"쓰러졌습니다.",Toast.LENGTH_SHORT).show();
            fallFlag = true;
        }
    }

    public void status(){
        if(DeviceStatus.getStatus() == 1) candle.setAlpha((float)1);//전구가 켜졌을 때
        else candle.setAlpha((float) 0.2);//전구가 꺼졌을 때
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case alarm:
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
                break;
            case brightness:
                brightness();
                break;
            case R.id.connectionCycle:
                connectionCycle();
                break;
            case logout:
                finish();
                break;
        }
        return true;
    }

    public void brightness(){
        View brView = getLayoutInflater().inflate(R.layout.activity_brightness, null);
        final SeekBar seekBar = (SeekBar)brView.findViewById(R.id.seekBar);
        AlertDialog.Builder brDlg = new AlertDialog.Builder(MainActivity.this);
        brDlg.setTitle("조도 설정");
        seekBar.setProgress(DeviceStatus.getBright_set());
        brightFlag = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(brightFlag){
                    seekBar.setSecondaryProgress(DeviceStatus.getBright_sta());
                    SystemClock.sleep(500);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        brDlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putInt("bright_set",seekBar.getProgress());
                DeviceStatus.setBright_set(seekBar.getProgress());
                Toast.makeText(MainActivity.this,"저장되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
        brDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                brightFlag = false;
            }
        });
        brDlg.setView(brView);
        brDlg.show();
    }
    public void connectionCycle(){
        View conView = getLayoutInflater().inflate(R.layout.activity_connection, null);
        final EditText cycle = (EditText)conView.findViewById(R.id.cycle);
        final Button upCycle = (Button)conView.findViewById(R.id.upCycle);
        final Button downCycle = (Button)conView.findViewById(R.id.downCycle);
        upCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycle.setText(String.valueOf(Integer.parseInt(cycle.getText().toString())+1));
            }
        });
        downCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(cycle.getText().toString()) > 0){
                    cycle.setText(String.valueOf(Integer.parseInt(cycle.getText().toString())-1));
                }
            }
        });
        cycle.setText(String.valueOf(DeviceStatus.getConnection_cycle()));
        AlertDialog.Builder conDlg = new AlertDialog.Builder(MainActivity.this);
        conDlg.setTitle("통신주기 설정");
        conDlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeviceStatus.setConnection_cycle(Integer.parseInt(cycle.getText().toString()));
                editor.putInt("connection_cycle",Integer.parseInt(cycle.getText().toString()));
                Toast.makeText(MainActivity.this,"저장되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
        conDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        conDlg.setView(conView);
        conDlg.show();
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
