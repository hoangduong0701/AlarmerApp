package com.example.alarmer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class SplashActivity extends AppCompatActivity {


  //  private int seePass = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

//
//        loginBtn.setOnClickListener(v -> {
//            login();
//        });
//        /// hien thi mat khau
//        seePassword.setOnClickListener(v -> {
//            if (seePass == 0) {
//                inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                seePass = 1;
//            } else {
//                inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                seePass = 0;
//            }
//        });
//        CreateNewAccountBtn.setOnClickListener(v -> {
//            startActivity(new Intent(this, SignActivity.class));
//        });
//    }
//    void login(){
//        String userName = inputEmail.getText().toString().trim();
//        String passWord = inputPassword.getText().toString().trim();
//
//        if (userName.isEmpty() || passWord.isEmpty()){
//            Toast.makeText(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
//        }else {
//
//            AlarmApplication app = (AlarmApplication) getApplication();
//                boolean isValid = app.check_All(userName, passWord);
//                if (isValid) {
//                    startActivity(new Intent(this, MainActivity.class));
//                } else {
//                    Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
//                }
//
//
//        }
//    }
    }
}