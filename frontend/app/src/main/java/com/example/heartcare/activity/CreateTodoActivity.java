package com.example.heartcare.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.utilities.DateFormat;

import java.util.Date;

public class CreateTodoActivity extends AppCompatActivity {
    private EditText editTextContent;
    private ConstraintLayout btnSave;
    private ConstraintLayout btnCancel;
    private TextView textViewCreateEventTitleDate;

    private AlertDialog dialogCancel;

    private TimePicker timePickerStartTime;
    private TimePicker timePickerEndTime;

    private void map() {
        editTextContent = findViewById(R.id.content);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        textViewCreateEventTitleDate = findViewById(R.id.create_event_title_date);
        timePickerStartTime = findViewById(R.id.start_time);
        timePickerEndTime = findViewById(R.id.end_time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        map();

        Intent intent = getIntent();
        int date = intent.getIntExtra("DATE", 0);
        int month = intent.getIntExtra("MONTH", 0);
        int year = intent.getIntExtra("YEAR", 0);

        String[] months = new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        textViewCreateEventTitleDate.setText(year + ". " + months[month - 1] + " " + date);
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                int date = intent.getIntExtra("DATE", 0);
                int month = intent.getIntExtra("MONTH", 0);
                int year = intent.getIntExtra("YEAR", 0);

                int hourStartTime = timePickerStartTime.getHour();
                int minuteStartTime = timePickerStartTime.getMinute();

                int hourEndTime = timePickerEndTime.getHour();
                int minuteEndTime = timePickerEndTime.getMinute();

                String content = String.valueOf(editTextContent.getText());

                /*
                    Ghép phần lưu ở backend
                 */
                String startTime = DateFormat.ISO8601format(new Date(year-1900, month-1, date, hourStartTime, minuteStartTime));
                String endTime = DateFormat.ISO8601format(new Date(year-1900, month-1, date, hourEndTime, minuteEndTime));
                Backend.createNote(content, startTime, endTime);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void focusChangeEditTextInput() {
        editTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
