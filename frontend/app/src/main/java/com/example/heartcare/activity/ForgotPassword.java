package com.example.heartcare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartcare.R;

public class ForgotPassword extends AppCompatActivity {
    private ImageView icBack;
    private TextView btnSend;
    private EditText etMail;

    private void map() {
        icBack = findViewById(R.id.ic_back);
        btnSend = findViewById(R.id.btn_send);
        etMail = findViewById(R.id.et_mail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        map();
        setFocusChangeListener();
        clickIcBack();
        clickBtnSend();
    }

    private void setFocusChangeListener() {
        etMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clickBtnSend() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, VerifyEmail.class);
                String email = String.valueOf(etMail.getText());
                intent.putExtra("Email", String.valueOf(etMail.getText()));
                /*
                    Lấy giá trị email --> backend --> gửi từ server
                 */
                startActivity(intent);
            }
        });
    }

    private void clickIcBack() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
