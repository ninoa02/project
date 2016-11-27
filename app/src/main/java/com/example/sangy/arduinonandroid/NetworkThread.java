package com.example.sangy.arduinonandroid;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sangy on 2016-11-13.
 */

public class NetworkThread extends Thread {
    private Handler mHandler;
    private String addr;
    private StringBuffer data = new StringBuffer();

    public NetworkThread(Handler mHandler, String addr) {
        this.mHandler = mHandler;
        this.addr = addr;
    }

    @Override
    public void run() {

        try {
            URL url = new URL(DeviceStatus.SERVER + addr);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            if(con != null){
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    Log.d("candle", "연결성공");
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
        Message msg = Message.obtain(mHandler, 0, 0, 0, result);
        mHandler.sendMessage(msg);
    }

}
