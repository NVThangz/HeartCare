package com.example.heartcare.activity;

import static com.example.heartcare.utilities.Constants.languages;
import static com.example.heartcare.utilities.Constants.languages_ISO_639;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;

import java.util.Locale;

public class SelectLanguage extends AppCompatActivity {
    private ConstraintLayout selectLanguageBox;
    private ConstraintLayout btnConfirm;
    private TextView tvLanguage;
    private ImageView icFlag;

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
        icFlag = findViewById(R.id.ic_flag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLanguage("en");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        map();
        clickSelectLanguageBox();
        clickBtnConfirm();
    }

    private void clickSelectLanguageBox() {
        selectLanguageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSelectLanguage();
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

    public void showDialogSelectLanguage() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.language_bottom_sheet_layout_item);

        LinearLayout englishLayout = dialog.findViewById(R.id.btn_english);
        englishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                icFlag.setImageResource(R.drawable.ic_english);
                tvLanguage.setText("English");
            }
        });

        LinearLayout vietnameseLayout = dialog.findViewById(R.id.btn_vietnamese);
        vietnameseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                icFlag.setImageResource(R.drawable.ic_vietnamese);
                tvLanguage.setText("Vietnamese");
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
