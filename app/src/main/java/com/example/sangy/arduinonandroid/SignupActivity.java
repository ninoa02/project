package com.example.sangy.arduinonandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sangy on 2016-11-13.
 */

public class SignupActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private EditText etDeviceNumber;
    private Button btnDone;
    private Button btnCancel;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etDeviceNumber = (EditText)findViewById(R.id.etDeviceNumber);
        etPasswordConfirm = (EditText)findViewById(R.id.etPasswordConfirm);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 입력 확인
                if( etEmail.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "Email을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                }
                // 비밀번호 입력 확인
                if( etPassword.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                }
                // 비밀번호 확인 입력 확인
                if( etPasswordConfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    etPasswordConfirm.requestFocus();
                }
                // 비밀번호 일치 확인
                if( !etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()) ) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    etPassword.setText("");
                    etPasswordConfirm.setText("");
                    etPassword.requestFocus();
                }
                //회원가입 처리
                Handler signupHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.obj.toString().equals("1")){
                            Toast.makeText(getApplicationContext(), "회원가입을 완료했습니다", Toast.LENGTH_SHORT).show();
                            Intent result = new Intent(getApplicationContext(),LoginActivity.class);
                            result.putExtra("email", etEmail.getText().toString());
                            result.putExtra("password", etPassword.getText().toString());
                            // 자신을 호출한 Activity로 데이터를 보낸다.
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                };
                NetworkThread thread = new NetworkThread(
                        signupHandler,
                        "signup?email=" + etEmail.getText().toString() + "&password=" +
                        etPassword.getText().toString() + "&device-number=" +
                        etDeviceNumber.getText().toString()
                );
                thread.setDaemon(true);
                thread.start();
                try{
                    thread.join();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
