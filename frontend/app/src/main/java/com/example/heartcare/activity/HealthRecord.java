package com.example.heartcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.fragment.HomeFragment;
import com.example.heartcare.object.HealthRecordItem;

import java.util.ArrayList;
import java.util.List;

public class HealthRecord extends AppCompatActivity {
    private RecyclerView healthRecordRecycler;
    private ImageView editHealthRecord;

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        map();
        eventHealthRecordRecycler();
        clickBtnEditHealthRecord();
        clickBtnBack();
    }

    private void map() {
        healthRecordRecycler = findViewById(R.id.health_record_recycler);
        editHealthRecord = findViewById(R.id.edit_health_record);
        btnBack = findViewById(R.id.ic_arrow_type1);
    }

    private void clickBtnEditHealthRecord() {
        editHealthRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthRecord.this, EditHealthRecord.class);
                startActivity(intent);
            }
        });
    }

    private void clickBtnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthRecord.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void eventHealthRecordRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        healthRecordRecycler.setLayoutManager(linearLayoutManager);

        HealthRecordAdapter healthRecordAdapter = new HealthRecordAdapter(getListUsers());
        healthRecordRecycler.setAdapter(healthRecordAdapter);
    }

    private List<HealthRecordItem> getListUsers() {
        String[] titles = {"Age:", "Height:", "Weight:"};
        String[] contents = {"20","170 cm","60 kg"};

        List <HealthRecordItem> healthRecordItemList = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            HealthRecordItem healthRecordItem = new HealthRecordItem(titles[i],contents[i]);
            healthRecordItemList.add(healthRecordItem);
        }
        return healthRecordItemList;
    }

}