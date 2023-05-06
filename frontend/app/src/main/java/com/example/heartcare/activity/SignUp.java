package com.example.heartcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;

public class SignUp extends AppCompatActivity {
    private EditText txt_full_name = null;
    private EditText txt_email = null;
    private EditText txt_password = null;
    private EditText txt_confirm_password = null;

    private ConstraintLayout btn_sign_up = null;

    private TextView tv_sign_up = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        map();

        clickBtnSignUp();
        clickTvSignUp();
    }

    private void clickBtnSignUp() {
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createAccount();
                    Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                    finish();
                } catch (Exception exception) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clickTvSignUp() {
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void map() {
        txt_full_name = findViewById(R.id.txt_full_name);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        tv_sign_up = findViewById(R.id.tv_sign_up);
    }

    void createAccount() throws Exception {
        String fullname = txt_full_name.getText().toString().trim();
        String email = txt_email.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String confirm_password = txt_confirm_password.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            throw new Exception("Enter fullname address!");
        }

        if (TextUtils.isEmpty(email)) {
            throw new Exception("Enter email address!");
        }

        if (TextUtils.isEmpty(password)) {
            throw new Exception("Enter password!");
        }

        if (TextUtils.isEmpty(confirm_password)) {
            throw new Exception("Enter confirm_password!!");
        }

        if (password.length() < 6) {
            throw new Exception("Password too short, enter minimum 6 characters!");
        }

        if (!password.equals(confirm_password)) {
            throw new Exception("Your password and confirmation password must match!");
        }

        /*
            Kết nối backend để đăng ký và throw new Expection("Nội dung lỗi");
            Nếu đăng ký thành công thì không cần phải trả ra gì
         */
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}
