package com.example.heartcare.activity;

import static com.example.heartcare.utilities.Calculations.calculateAge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.QueryRecordQuery;
import com.example.heartcare.R;
import com.example.heartcare.adapter.HealthRecordAdapter;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.fragment.HomeFragment;
import com.example.heartcare.object.HealthRecordItem;
import com.example.heartcare.utilities.DateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthRecord extends AppCompatActivity {
    private RecyclerView healthRecordRecycler;
    private ImageView editHealthRecord;

    private TextView fullName;

    private TextView email;
    private ImageView btnBack;
    private ImageView icChatbot;

    private CircleImageView avatar_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        map();
        eventHealthRecordRecycler();
        setIcChatBot();
        clickBtnEditHealthRecord();
        clickBtnBack();
        fullName.setText(Backend.name);
        email.setText(Backend.email);

        SharedPreferences sharedPreferences = this.getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("image_data")) {
            String imageDataString = sharedPreferences.getString("image_data", null);

            // Giải mã chuỗi thành mảng byte
            byte[] imageData = Base64.decode(imageDataString, Base64.DEFAULT);

            // Khôi phục Bitmap từ mảng byte
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

            avatar_profile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, false));
        }
    }

    private void setIcChatBot() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        icChatbot.setOnTouchListener(new View.OnTouchListener() {
            private int lastX, lastY;
            private int dx, dy;
            private static final int CLICK_DRAG_TOLERANCE = 10;
            private boolean isOnClick;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int parentWidth = ((ViewGroup)v.getParent()).getWidth();
                int parentHeight = ((ViewGroup)v.getParent()).getHeight();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        isOnClick = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        int newX = v.getLeft() + dx;
                        int newY = v.getTop() + dy;

                        // Giới hạn vùng di chuyển của nút trong phạm vi màn hình
                        if (newX < 0) {
                            newX = 0;
                        } else if (newX + v.getWidth() > parentWidth) {
                            newX = parentWidth - v.getWidth();
                        }

                        if (newY < 0) {
                            newY = 0;
                        } else if (newY + v.getHeight() > parentHeight) {
                            newY = parentHeight - v.getHeight();
                        }

                        v.layout(newX, newY, newX + v.getWidth(), newY + v.getHeight());
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        if (Math.abs(dx) > CLICK_DRAG_TOLERANCE || Math.abs(dy) > CLICK_DRAG_TOLERANCE) {
                            isOnClick = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isOnClick) {
                            // Sự kiện Click được thực hiện
                            Intent intent = new Intent(HealthRecord.this, HealthConsultation.class);
                            startActivity(intent);
                        } else {
                            // Di chuyển nút đến vị trí gần sát màn hình
                            int currentX = v.getLeft();
                            int currentY = v.getTop();
                            int targetX, targetY;

                            if (currentX + v.getWidth() / 2 < screenWidth / 2) {
                                // Di chuyển về bên trái màn hình
                                targetX = 0;
                            } else {
                                // Di chuyển về bên phải màn hình
                                targetX = screenWidth - v.getWidth();
                            }

                            targetY = currentY;

                            // Animation di chuyển nút đến vị trí mới
                            v.animate()
                                    .x(targetX)
                                    .y(targetY)
                                    .setDuration(200)
                                    .start();

                            v.layout(targetX, targetY, targetX + v.getWidth(), targetY + v.getHeight());
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                        }
                        break;
                }
                return true;
            }
        });

    }


    private void map() {
        fullName = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        healthRecordRecycler = findViewById(R.id.health_record_recycler);
        editHealthRecord = findViewById(R.id.edit_health_record);
        btnBack = findViewById(R.id.ic_back);
        icChatbot = findViewById(R.id.ic_chatbot);
        avatar_profile = findViewById(R.id.avatar_profile);
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
                intent.putExtra("BloodType", String.valueOf(getListUsers.get(3).getContent()));
                intent.putExtra("Problems", String.valueOf(getListUsers.get(4).getContent()));
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

        QueryRecordQuery.Data data = Backend.queryRecord();

        String Date = Objects.toString(data.user.profile.dob, "");;
        if(Date != "") {
            Date = String.valueOf(calculateAge(DateFormat.ISO8601toDate(data.user.profile.dob.toString())));
        }

        String age = Date;
        String height = Objects.toString(data.user.record.height, "");
        String weight = Objects.toString(data.user.record.weight, "");
        String bloodType = Objects.toString(data.user.record.bloodType, "");
        String problems = Objects.toString(data.user.record.HealthProblems, "");

        String[] titles = {"Age:", "Height (cm): ", "Weight (kg): ", "Blood Type", "Health problems: "};
        String[] contents = {age,height,weight, bloodType, problems};



        List <HealthRecordItem> healthRecordItemList = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            HealthRecordItem healthRecordItem = new HealthRecordItem(titles[i],contents[i]);
            healthRecordItemList.add(healthRecordItem);
        }
        return healthRecordItemList;
    }

}