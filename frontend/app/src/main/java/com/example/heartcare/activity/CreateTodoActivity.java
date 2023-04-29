package com.example.heartcare.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;
import com.example.heartcare.fragment.CalendarFragment;

public class CreateTodoActivity extends AppCompatActivity {
    private EditText content;
    private ConstraintLayout btnSave;
    private ConstraintLayout btnCancel;

    private AlertDialog dialogCancel;

    private void map() {
        content = findViewById(R.id.content);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        map();

        setDialogCancel();
        clickBtnSave();
        clickBtnCancel();
        focusChangeEditTextInput();
    }

    private void setDialogCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.are_you_sure));
        builder.setMessage(getResources().getString(R.string.if_you_cancel_now_all_changes_will_be_lost));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogCancel = builder.create();
    }

    private void clickBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCancel.show();
            }
        });
    }

    private void clickBtnSave() {
        
        
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void focusChangeEditTextInput() {
        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
