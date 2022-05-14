package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yolo.ecosell.adapter.ChatAdapter;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.Objects;

public class ChatListActivity extends AppCompatActivity {

    private String username;
    private String email;

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatRecyclerViewAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatCollectionReference = db.collection("Chats");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Messages");
        getSupportActionBar().hide();

        chatRecyclerView = findViewById(R.id.chat_list_RecyclerView);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup adapter
        chatRecyclerViewAdapter = new ChatAdapter(this);

        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);

        chatRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }



}