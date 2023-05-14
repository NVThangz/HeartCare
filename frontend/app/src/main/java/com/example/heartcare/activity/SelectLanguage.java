package com.example.heartcare.activity;

import static com.example.heartcare.utilities.Constants.languages;
import static com.example.heartcare.utilities.Constants.languages_ISO_639;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {
    private ConstraintLayout selectLanguageBox;
    private ConstraintLayout btnConfirm;
    private TextView tvLanguage;

    private PopupMenu popupLanguage;

    private void setLanguage(String languagesISO639) {
        Locale locale = new Locale(languagesISO639);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languagesISO639);
        editor.apply();

    }

    private void map() {
        selectLanguageBox = findViewById(R.id.select_language_box);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvLanguage = findViewById(R.id.tv_language);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLanguage("en");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        map();
        setPopupMenuLanguage();
        clickSelectLanguageBox();
        clickBtnConfirm();
    }

    private void clickSelectLanguageBox() {
        selectLanguageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLanguage.show();
            }
        });
    }

    private void clickBtnConfirm() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languagesISO639 = languages_ISO_639.get(languages.indexOf((String) tvLanguage.getText()));
                setLanguage(languagesISO639);
                Intent intent = new Intent(SelectLanguage.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setPopupMenuLanguage() {
        popupLanguage = new PopupMenu(this, selectLanguageBox);
        for (int i = 0; i < languages.size(); i++) {
            popupLanguage.getMenu().add(languages.get(i));
        }

        popupLanguage.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String selectedLanguage = item.getTitle().toString();
                tvLanguage.setText(selectedLanguage);
                return true;
            }
        });

    }

}
