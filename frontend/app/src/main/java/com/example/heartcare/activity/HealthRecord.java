package com.example.heartcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.QueryRecordQuery;
import com.example.heartcare.R;
import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.backend.Backend;
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
                List<HealthRecordItem> getListUsers = getListUsers();
                intent.putExtra("Age", String.valueOf(getListUsers.get(0).getContent()));
                intent.putExtra("Height", String.valueOf(getListUsers.get(1).getContent()));
                intent.putExtra("Weight", String.valueOf(getListUsers.get(2).getContent()));
                intent.putExtra("Problems", String.valueOf(getListUsers.get(3).getContent()));
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

//        /*
//            Ghép backend lấy giá trị để hiển thị thông tin người dùng
//         */

        QueryRecordQuery.Data data = null;
        try {
            data = Backend.queryRecord();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
// tinsh tuooi nuax
        String[] titles = {"Age:", "Height (cm): ", "Weight (kg): ", "Health problems: "};
        String[] contents = {"22",data.record.height.toString(),data.record.weight.toString(), data.record.HealthProblems};


        List <HealthRecordItem> healthRecordItemList = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            HealthRecordItem healthRecordItem = new HealthRecordItem(titles[i],contents[i]);
            healthRecordItemList.add(healthRecordItem);
        }
        return healthRecordItemList;
    }

}