package com.example.sangy.arduinonandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sangy on 2016-11-13.
 */

public class LoginActivity extends Activity{
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private CheckBox checkBox;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this,Splash.class));
        emailInput = (EditText)findViewById(R.id.emailInput);
        passwordInput = (EditText)findViewById(R.id.passwordInput);
        loginButton = (Button)findViewById(R.id.loginButton);
        signupButton = (Button)findViewById(R.id.signupButton);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        if (MainActivity.mPreferences.getBoolean("check",true)){
            checkBox.setChecked(true);
            emailInput.setText(MainActivity.mPreferences.getString("email",null));
            passwordInput.setText(MainActivity.mPreferences.getString("password",null));
        }

        //메인 핸들러 - 네트워크 스레드의 처리를 받아
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("candle", "메시지" + msg.obj.toString());
                String[] str = msg.obj.toString().split(":");
                if(str[0].equals(email)){
                    DeviceStatus.setDevice_no(Integer.parseInt(str[1]));
                    MainActivity.editor.putInt("device_no", Integer.parseInt(str[1]));
                    Toast.makeText(getApplicationContext(),"로그인 성공하였습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    setResult(1, intent);
                    finish();
                }
                else if(str[0].equals("no_email")){
                    Toast.makeText(getApplicationContext(),"존재하지 않는 email입니다.",Toast.LENGTH_SHORT).show();
                }
                else if(str[0].equals("wr_ps")){
                    Toast.makeText(getApplicationContext(),"비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show();
                }
            }
        };
        //로그인버튼에 대한 처리
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                String addr = "login?email=" + email + "&password=" + password;
                if(checkBox.isChecked()){
                    MainActivity.editor.putBoolean("check", true);
                    MainActivity.editor.putString("email", email);
                    MainActivity.editor.putString("password", password);
                }
                else {
                    MainActivity.editor.putBoolean("check", false);
                    MainActivity.editor.putString("email", "");
                    MainActivity.editor.putString("password", "");
                }
                NetworkThread thread = new NetworkThread(mHandler,addr);
                thread.setDaemon(true);
                thread.start();
                try {
                    thread.join();
                }catch (Exception e){
                    e. printStackTrace();
                }
            }
        });
        //회원가입 버튼에 대한 처리
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),SignupActivity.class),1000);
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // setResult를 통해 받아온 요청번호, 상태, 데이터
        if(requestCode == 1000 && resultCode == RESULT_OK) {
            emailInput.setText(data.getStringExtra("email"));
            passwordInput.setText(data.getStringExtra("password"));
        }
    }
}
