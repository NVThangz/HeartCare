package com.example.heartcare.activity;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.heartcare.R;
import com.example.heartcare.SignupMutation;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.type.AuthInput;
import com.example.heartcare.type.UpdateProfileInput;

import io.reactivex.rxjava3.core.Single;

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

        setFocusChangeListener();
        clickBtnSignUp();
        clickTvSignUp();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setFocusChangeListener() {
        txt_full_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        txt_confirm_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
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

        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);

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
        Backend.signup(fullname, email, password, sharedPreferences);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}
