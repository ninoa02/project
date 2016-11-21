package com.example.sangy.arduinonandroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by sangy on 2016-11-13.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }


}