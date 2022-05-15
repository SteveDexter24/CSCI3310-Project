package com.yolo.ecosell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.ChatBubbleAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Chat;
import model.ChatRoom;
import model.User;
import model.UserViewModel;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private RecyclerView chatBubbleRecyclerView;
    private ChatBubbleAdapter chatBubbleAdapter;
    private TextView chatBubbleUserNameTextView;
    private EditText messageEditText;
    private Button sendButton;
    private ImageView profileImageView;
    private String chatRoomId, otherUserName;

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatsCollectionReference = db.collection("Chats");
    //private FirebaseAuth firebaseAuth;

    private UserViewModel userViewModel;
    private String currentUserImageUrl, otherUserImageUrl;
    private String currentUserName;
    private List<Chat> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        getSupportActionBar().hide();


        // to query and make new chat -> add to chatroom
        chatRoomId = intent.getStringExtra("chatRoomId");
        otherUserImageUrl = intent.getStringExtra("profileImage");
        otherUserName = intent.getStringExtra("username");

        messageList = new ArrayList<>();

        chatBubbleUserNameTextView = findViewById(R.id.chatBubbleUserNameTextView);
        chatBubbleUserNameTextView.setText(otherUserName);

        profileImageView = findViewById(R.id.chatBubbleProfileImageView);
        Glide.with(this)
                .load(otherUserImageUrl).into(profileImageView);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(ChatActivity.this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            currentUserImageUrl = users.get(0).getImageUrl();
            currentUserName = users.get(0).getUsername();

        });

        messageEditText = findViewById(R.id.chat_input);
        sendButton = findViewById(R.id.btn_chatsend);

        chatBubbleRecyclerView = findViewById(R.id.chatBubble_recyclerView);
        chatBubbleRecyclerView.setHasFixedSize(true);
        // Chat layout starts at the bottom (latest message)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        chatBubbleRecyclerView.setLayoutManager(layoutManager);

        sendButton.setOnClickListener(view -> {
            onSubmitMessage();
            hideKeybaord(view);
            messageEditText.setText("");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatsCollectionReference
                .whereEqualTo("chatRoom", chatRoomId)
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        messageList = new ArrayList<>();
                        for (QueryDocumentSnapshot chats : value) {
                            Chat chat = chats.toObject(Chat.class);
                            messageList.add(chat);
                            Log.d(TAG, chat.getUserMessage());
                            // Setup adapter

                        }
                        Log.d(TAG, "Line 125" + messageList.size());
                        chatBubbleAdapter = new ChatBubbleAdapter(ChatActivity.this, messageList, otherUserImageUrl, currentUserImageUrl, otherUserName, currentUserName);
                        chatBubbleRecyclerView.setAdapter(chatBubbleAdapter);
                        chatBubbleAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                });
    }

    private void onSubmitMessage(){
        String currentUserMessage = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(currentUserMessage) || TextUtils.isEmpty(chatRoomId)) return;

        Chat message = new Chat(Timestamp.now(), currentUserMessage, "", chatRoomId);

        chatsCollectionReference
                .add(message)
                .addOnSuccessListener(documentReference -> {

                });
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}