package com.example.heartcare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartcare.R;
import com.example.heartcare.message.Message;
import com.example.heartcare.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class HealthConsultation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textViewWelcome;
    private EditText editTextMessage;
    private ImageButton sendButton;
    private TextView textViewFullName;
    private TextView textViewEmail;
    private List<Message> messageList = new ArrayList<>();;
    private MessageAdapter messageAdapter;

    private void map() {
        recyclerView = findViewById(R.id.recycler_view);
        textViewWelcome = findViewById(R.id.welcome_text);
        editTextMessage = findViewById(R.id.edit_text_message);
        sendButton = findViewById(R.id.send_btn);
        textViewFullName = findViewById(R.id.full_name);
        textViewEmail = findViewById(R.id.email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_consultation);
        map();

        setTextView();
        setFocusChangeListener();
        setMessage();
        clickSendButton();
        setTextViewWelcome();
    }

    private void setTextView() {
        /*
            Ghép backend
         */
        textViewFullName.setText("Bùi Minh Hoạt");
        textViewEmail.setText("official.buiminhhoat@gmail.com");
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

    private void setTextViewWelcome() {
        if (!messageList.isEmpty()) {
            textViewWelcome.setVisibility(View.GONE);
        }
    }

    private void clickSendButton() {
        sendButton.setOnClickListener((v)->{
            String question = editTextMessage.getText().toString().trim();
            if (question.length() == 0) {
                return;
            }

            addToChat(question,Message.SENT_BY_ME);
            editTextMessage.setText("");

            /*
            * Ghép câu trả lời của ChatGPT vào đây
            * */
            addResponse("Hello World");

            textViewWelcome.setVisibility(View.GONE);
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