package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yolo.ecosell.adapter.ChatBubbleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Chat;
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
    private CollectionReference chatRoomsCollectionReference = db.collection("ChatRooms");
    private FirebaseAuth firebaseAuth;

    private UserViewModel userViewModel;
    private String currentUserImageUrl, otherUserImageUrl;
    private String currentUserName;
    private List<Chat> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        getSupportActionBar().hide();


        // to query and make new chat -> add to chatroom
        chatRoomId = intent.getStringExtra("chatRoomId");
        otherUserImageUrl = intent.getStringExtra("profileImage");
        otherUserName = intent.getStringExtra("username");
        currentUserImageUrl = intent.getStringExtra("currentUserProfileImage");
        currentUserName = intent.getStringExtra("currentUserUsername");
        Log.d(TAG, currentUserImageUrl);
        Log.d(TAG, currentUserName);

        messageList = new ArrayList<>();

        chatBubbleUserNameTextView = findViewById(R.id.chatBubbleUserNameTextView);
        chatBubbleUserNameTextView.setText(otherUserName);

        profileImageView = findViewById(R.id.chatBubbleProfileImageView);
        Glide.with(this)
                .load(otherUserImageUrl).into(profileImageView);

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
                    Log.d(TAG, value.toString());

                    if (value != null && !value.isEmpty()) {
                        messageList = new ArrayList<>();
                        for (QueryDocumentSnapshot chats : value) {
                            Chat chat = chats.toObject(Chat.class);
                            messageList.add(chat);
                        }
                        // Setup adapter
                        chatBubbleAdapter = new ChatBubbleAdapter(ChatActivity.this,
                                messageList, otherUserImageUrl, currentUserImageUrl,
                                otherUserName, currentUserName, firebaseAuth.getCurrentUser().getUid());
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

        Chat message = new Chat(Timestamp.now(), currentUserMessage, firebaseAuth.getCurrentUser().getUid(), chatRoomId);

        Map<String,Object> updates = new HashMap<>();

        chatsCollectionReference
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    updates.put("chats", FieldValue.arrayUnion(documentReference));
                    updates.put("lastMessage", currentUserMessage);
                    chatRoomsCollectionReference
                            .document(chatRoomId)
                            .update(updates)
                            .addOnCompleteListener(task -> Log.d(TAG, "Line 160: " + "Add to chat room list"));
                });
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}

// sQ9rWNe6qHxr4PuAcsKo