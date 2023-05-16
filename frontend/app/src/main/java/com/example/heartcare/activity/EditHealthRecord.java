package com.example.heartcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.adapter.EditHealthRecordAdapter;
import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.object.HealthRecordItem;

import java.util.ArrayList;
import java.util.List;

public class EditHealthRecord extends AppCompatActivity {
    private ConstraintLayout cancelEdit;
    private ConstraintLayout saveEdit;

    private RecyclerView healthRecordRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_health_record);
        map();

        eventHealthRecordRecycler();
        clickBtnCancelEdit();
        clickBtnSaveEdit();
    }

    private void eventHealthRecordRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        healthRecordRecycler.setLayoutManager(linearLayoutManager);

        EditHealthRecordAdapter healthRecordAdapter = new EditHealthRecordAdapter(getListUsers());
        healthRecordRecycler.setAdapter(healthRecordAdapter);
    }

    private void map() {
        healthRecordRecycler = findViewById(R.id.edit_health_record_recycler);
        cancelEdit = findViewById(R.id.cancel_edit);
        saveEdit = findViewById(R.id.save_edit);
    }

    private void clickBtnCancelEdit() {
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditHealthRecord.this, HealthRecord.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickBtnSaveEdit() {
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    Ghép backend để lưu thông tin người dùng
                 */


                Intent intent = new Intent(EditHealthRecord.this, HealthRecord.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private List<HealthRecordItem> getListUsers() {
        String[] titles = {"Age:", "Height:", "Weight:", "Health problems:"};
        Intent intent = getIntent();
        String[] contents = {intent.getStringExtra("Age"),
                intent.getStringExtra("Height"),
                intent.getStringExtra("Weight"),
                intent.getStringExtra("Problems")};

        List <HealthRecordItem> healthRecordItemList = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            HealthRecordItem healthRecordItem = new HealthRecordItem(titles[i],contents[i]);
            healthRecordItemList.add(healthRecordItem);
        }
        return healthRecordItemList;
    }

}