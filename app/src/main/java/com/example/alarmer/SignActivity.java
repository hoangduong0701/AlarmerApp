package com.example.alarmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {
    EditText  inputEmail, inputPassword, inputConfirmPassword;
    Button CreateAccountBtn;
    TextView LoginAccountBtn;
    AlarmApplication database;
    ImageButton back_btn;
    AppCompatImageView seePassword;
    private int seePass = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        CreateAccountBtn = findViewById(R.id.CreateAccountBtn);
        LoginAccountBtn = findViewById(R.id.LoginAccountBtn);
        seePassword = findViewById(R.id.seePassword);
        back_btn = findViewById(R.id.back_btn);
        database = new AlarmApplication();
        CreateAccountBtn.setOnClickListener(v -> {
            if (isValidSignUpDetails()){
                signUp();
            }
        });
        LoginAccountBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SplashActivity.class));
        });
        back_btn.setOnClickListener(v -> {
            onBackPressed();
        });
        seePassword.setOnClickListener(v -> {
            if (seePass == 0) {
                inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                seePass = 1;
            } else {
                inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                seePass = 0;
            }
        });
    }

    void signUp(){
        // 45-48 get du lieu tu edittext truyefn vào string

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String conFirmPass = inputConfirmPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || conFirmPass.isEmpty()){
            showToast("Điền đầy đủ thông tin");
        }else {
            AlarmApplication app = (AlarmApplication) getApplication();
            boolean checkEmail = app.isUsernameExists(email);
            if (!checkEmail){
                boolean insertAccount = app.insertAccount( email, password);
                if (insertAccount){
                    showToast("Đăng ký thành công");
                    startActivity(new Intent(this, SplashActivity.class));
                }else {
                    showToast("Đăng ký thất bại");
                }
            }else {
                showToast("Tài khoản này đã đăng ký rồi");
            }

        }

    }


    private Boolean isValidSignUpDetails() {
        // kiem tra dang ky
//        if (inputName.getText().toString().trim().isEmpty()) {
//            showToast("Nhập tên tài khoản");
//            return false;
//       } else
           if (inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Nhập email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
            showToast("Hãy nhập email hợp lệ");
            return false;
        } else if (inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Nhập mật khẩu");
            return false;
        }
        else if (inputPassword.getText().toString().trim().length() <= 6) {
            showToast("Mật khẩu phải có độ dài trên 6 ký tự");
            return false;
        }else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Nhập lại mật khẩu");
            return false;
        } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
            showToast("Mật khẩu nhập lại không khớp với mật khẩu đã nhập trước đó!");
            return false;
        } else {
            return true;
        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}