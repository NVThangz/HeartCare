package com.example.heartcare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;

public class CreateNewPassword extends AppCompatActivity {
    private ImageView icBack;
    private TextView btnSend;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private void map() {
        icBack = findViewById(R.id.ic_back);
        btnSend = findViewById(R.id.btn_save);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);
        map();
        setFocusChangeListener();
        clickIcBack();
        clickBtnSend();
    }

    private void setFocusChangeListener() {
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                String newPassword = String.valueOf(etPassword.getText());
                String confirmPassword = String.valueOf(etConfirmPassword.getText());
                SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
                try {
                    if (!newPassword.equals(confirmPassword)) {
                        throw new Exception(getResources().getString(R.string.confirm_password_does_not_match));
                    }
                    Backend.resetPasswordConfirmed(Backend.email, newPassword, sharedPreferences);
                    Intent intent = new Intent(CreateNewPassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

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
