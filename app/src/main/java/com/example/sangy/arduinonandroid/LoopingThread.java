package com.example.sangy.arduinonandroid;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sangy on 2016-11-13.
 */

public class LoopingThread extends Thread {
    private Handler mHandler;


    public LoopingThread(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        while(true){
            StringBuffer data = new StringBuffer();
            //요청문 작성
            String device_no = "12345";//String.valueOf(MainActivity.mPreferences.getInt("device_no",0));
            Log.d("device_no" , "device_no" + device_no);
            String bright_set = String.valueOf(DeviceStatus.getBright_set());
            String connection_cycle = String.valueOf(DeviceStatus.getConnection_cycle());
            String alarm_set = String.valueOf(DeviceStatus.getAlarm_set());
            String alarm_start = DeviceStatus.getAlarm_start();
            String alarm_end = DeviceStatus.getAlarm_end();
            Log.d("hour", "hour :" + alarm_start);
            Log.d("hour", "hour :" + alarm_end);
            String status_change = String.valueOf(DeviceStatus.getStatus_change());
            String req = "device_no=" + device_no +
                    "&bright_set=" + bright_set +
                    "&connection_cycle=" + connection_cycle +
                    "&alarm_set=" + alarm_set +
                    "&alarm_start=" + alarm_start +
                    "&alarm_end=" + alarm_end +
                    "&status_change=" + status_change;
            if(DeviceStatus.getStatus_change()==1) DeviceStatus.setStatus_change(0);
            //네트워크 연결
            try {
                URL url = new URL(DeviceStatus.SERVER + "lfand?" + req);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                if(con != null){
                    con.setConnectTimeout(10000);
                    con.setUseCaches(false);
                    if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = con.getInputStream();
                        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                        BufferedReader br = new BufferedReader(isr);
                        while (true) {
                            String line = br.readLine();
                            if (line == null) break;
                            data.append(line);
                        }
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            String result = data.toString();
            Log.d("candle","루핑결과 : " + result);
            Message msg = Message.obtain(mHandler, 0, 0, 0, result);
            mHandler.sendMessage(msg);
            SystemClock.sleep(DeviceStatus.getAndCycle());
        }
    }

}
