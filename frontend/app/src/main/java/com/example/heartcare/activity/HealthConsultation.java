package com.example.heartcare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.backend.Backend;
import com.example.heartcare.message.Message;
import com.example.heartcare.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthConsultation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageButton sendButton;
    private TextView textViewFullName;
    private TextView textViewEmail;
    private List<Message> messageList = new ArrayList<>();;
    private MessageAdapter messageAdapter;
    private ImageView icBack;
    private TextView textViewConsultNow;
    private FrameLayout shadow;

    private CircleImageView avatar_profile;

    private void map() {
        recyclerView = findViewById(R.id.recycler_view);
        editTextMessage = findViewById(R.id.edit_text_message);
        sendButton = findViewById(R.id.send_btn);
        textViewFullName = findViewById(R.id.full_name);
        textViewEmail = findViewById(R.id.email);
        icBack = findViewById(R.id.ic_back);
        textViewConsultNow = findViewById(R.id.text_view_consult_now);
        shadow = findViewById(R.id.shadow);
        avatar_profile = findViewById(R.id.avatar_profile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_consultation);
        map();

        setConsultNow();
        clickIcBack();
        setTextView();
        setFocusChangeListener();
        setMessage();
        clickSendButton();

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

    private void setConsultNow() {
        textViewConsultNow.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  textViewConsultNow.setVisibility(View.GONE);
                  shadow.setVisibility(View.GONE);

                  addResponse("...");
                  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
                                String lang = sharedPreferences.getString("language", null);
                                String FirstMessage = Backend.getAdvisoryFirst(lang);
                                messageList.get(0).setMessage(FirstMessage);
                                messageAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                messageList.get(0).setMessage(e.getMessage());
                                editTextMessage.setEnabled(false);
                                sendButton.setEnabled(false);
                                messageAdapter.notifyDataSetChanged();
                            }
                        }
                    }, 1000);
              }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String FirstMessage = Backend.getAdvisoryFirst();
//                    messageList.get(0).setMessage(FirstMessage);
////                    textViewWelcome.setVisibility(View.GONE);
//                } catch (Exception e) {
//                    messageList.get(0).setMessage(e.getMessage());
//                    editTextMessage.setEnabled(false);
//                    sendButton.setEnabled(false);
//                    messageAdapter.notifyDataSetChanged();
//                }
//            }
//        }, 1000);
//    }

    private void clickIcBack() {
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTextView() {
        /*
            Ghép backend
         */
        textViewFullName.setText(Backend.name);
        textViewEmail.setText(Backend.email);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setFocusChangeListener() {
        editTextMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void clickSendButton() {
        SharedPreferences sharedPreferences = getSharedPreferences("HeartCare", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", null);

        sendButton.setOnClickListener((v)->{
            String question = editTextMessage.getText().toString().trim();
            if (question.length() == 0) {
                return;
            }

            addToChat(question, Message.SENT_BY_ME);
            editTextMessage.setText("");
            String answer = Backend.getAdvisory(question);

            /*
            * Ghép câu trả lời của ChatGPT vào đây
            * */
            addResponse(answer);

//            textViewWelcome.setVisibility(View.GONE);
        });
    }

    private void setMessage() {
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
    }

    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        addToChat(response,Message.SENT_BY_BOT);
    }

}