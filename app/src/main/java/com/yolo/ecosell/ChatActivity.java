package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yolo.ecosell.adapter.ChatBubbleAdapter;

import java.util.LinkedList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatBubbleRecyclerView;
    private ChatBubbleAdapter chatBubbleAdapter;
    private TextView chatBubbleUserNameTextView;
    private EditText messageEditText;
    private Button sendButton;
    private LinkedList<String> chatMessageList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        getSupportActionBar().hide();

        chatBubbleUserNameTextView = findViewById(R.id.chatBubbleUserNameTextView);
        chatBubbleUserNameTextView.setText(intent.getStringExtra("username"));

        messageEditText = findViewById(R.id.chat_input);
        sendButton = findViewById(R.id.btn_chatsend);

        chatBubbleRecyclerView = findViewById(R.id.chatBubble_recyclerView);
        chatBubbleRecyclerView.setHasFixedSize(true);
        chatBubbleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup adapter
        chatBubbleAdapter = new ChatBubbleAdapter(this, chatMessageList);
        chatBubbleRecyclerView.setAdapter(chatBubbleAdapter);
        chatBubbleRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }
}