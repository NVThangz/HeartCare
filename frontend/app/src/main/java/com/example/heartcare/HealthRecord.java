package com.example.heartcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.object.HealthRecordItem;

import java.util.ArrayList;
import java.util.List;

public class HealthRecord extends AppCompatActivity {
    private RecyclerView healthRecordRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        map();
        eventHealthRecordRecycler();
    }

    private void map() {
        healthRecordRecycler = findViewById(R.id.health_record_recycler);
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