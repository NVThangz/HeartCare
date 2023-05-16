package com.example.heartcare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo3.api.Optional;
import com.example.heartcare.R;
import com.example.heartcare.adapter.EditHealthRecordAdapter;
import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.object.HealthRecordItem;
import com.example.heartcare.utilities.Calculations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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




                Double height = null;
                Double weight = null;
                String bloodType = null;
                String healthProblems = null;

                List<HealthRecordItem> healthRecordItemList = new ArrayList<>();

                for (int i = 0; i < healthRecordRecycler.getAdapter().getItemCount(); i++) {
                    EditHealthRecordAdapter.HealthRecordViewHolder viewHolder = (EditHealthRecordAdapter.HealthRecordViewHolder) healthRecordRecycler.findViewHolderForAdapterPosition(i);

                    if (viewHolder != null) {
                        String title = viewHolder.title.getText().toString();
                        String content = viewHolder.content.getText().toString();

                        if (i == 1) {
                            if(content != null && !content.isEmpty()) height = Double.parseDouble(content);
                        }
                        if (i == 2) {
                            if(content != null && !content.isEmpty()) weight = Double.parseDouble(content);
                        }
                        if (i == 3) {
                            if(content != null && !content.isEmpty()) bloodType = content;
                        }
                        if (i == 4) {
                            if(content != null && !content.isEmpty()) healthProblems = content;
                        }

                        System.out.println("title: " + height + " content: " + weight);


//                        // Tạo một đối tượng HealthRecordItem từ giá trị lấy được
//                        HealthRecordItem healthRecordItem = new HealthRecordItem(title, content);
//                        healthRecordItemList.add(healthRecordItem);
                    }
                }

                Double BMI = null;
                if (height != null && weight != null) {
                    BMI = Math.round(Calculations.calculateBMI(weight, height/100) * 100.0) / 100.0;
                }
                Backend.updateRecord(height, weight, BMI, bloodType, healthProblems);
                Intent intent = new Intent(EditHealthRecord.this, HealthRecord.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private List<HealthRecordItem> getListUsers() {
        String[] titles = {"Age:", "Height (cm):", "Weight (kg):", "Blood Type: ", "Health problems:"};
        Intent intent = getIntent();
        String[] contents = {intent.getStringExtra("Age"),
                intent.getStringExtra("Height"),
                intent.getStringExtra("Weight"),
                intent.getStringExtra("BloodType"),
                intent.getStringExtra("Problems")};

        List <HealthRecordItem> healthRecordItemList = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            HealthRecordItem healthRecordItem = new HealthRecordItem(titles[i],contents[i]);
            healthRecordItemList.add(healthRecordItem);
        }
        return healthRecordItemList;
    }

}