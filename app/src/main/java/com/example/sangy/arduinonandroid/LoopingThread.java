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
            String device_no = String.valueOf(DeviceStatus.getDevice_no());
            String bright_set = String.valueOf(DeviceStatus.getBright_set());
            String req = "device_no=" + device_no +
                    "&bright_set=" + bright_set;
            if(DeviceStatus.getStatus_change() == 1){
                req += "&status_change=" + 1;
                DeviceStatus.setStatus_change(0);
            }
            else{
                req += "&status_change=" + 0;
            }

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
            SystemClock.sleep(500);
        }
    }

}
