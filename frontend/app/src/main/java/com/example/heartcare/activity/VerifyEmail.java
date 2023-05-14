package com.example.heartcare.activity;

import android.accessibilityservice.InputMethod;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.heartcare.R;

public class VerifyEmail extends AppCompatActivity {
    private ImageView icBack;
    private TextView btnVerify;
    private TextView textViewEmail;
    private PinView pinView;
    private TextView textViewResendCode;

    private void map() {
        icBack = findViewById(R.id.ic_back);
        btnVerify = findViewById(R.id.btn_verify);
        pinView = findViewById(R.id.pin_view);
        textViewEmail = findViewById(R.id.email);
        textViewResendCode = findViewById(R.id.tv_resend_code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        map();

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");
        textViewEmail.setText(email);
        setFocusChangeListener();
        setPinView();
        clickIcBack();
        clickBtnSend();
        clickTextViewResendCode();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setFocusChangeListener() {
        pinView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void setPinView() {
        pinView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void clickTextViewResendCode() {
        textViewResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notificationResend), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clickBtnSend() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = String.valueOf(pinView.getText());

                /*
                    Kiểm tra code có đúng không?
                    Nếu đúng thì chạy tiếp, không thì dùng câu lệnh
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                 */

                Intent intent = new Intent(VerifyEmail.this, CreateNewPassword.class);
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
