package com.example.heartcare.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartcare.R;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {
    private Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, SelectLanguage.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
