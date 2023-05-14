package com.example.heartcare.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartcare.R;

import org.intellij.lang.annotations.Language;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {
    private Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", null);
        if (language == null) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Welcome.this, SelectLanguage.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
            return;
        }

        Locale locale = new Locale(language);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

}
