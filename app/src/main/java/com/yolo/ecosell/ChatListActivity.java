package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.ChatAdapter;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.ChatRoom;
import model.User;
import model.UserViewModel;

public class ChatListActivity extends AppCompatActivity {
    private final String TAG = "ChatListActivity";

    private String username;
    private String email;
    private UserViewModel userViewModel;
    private String profileImageUrl;

    // Views
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatRecyclerViewAdapter;
    private ImageView profileImageView, backImageView;

    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRoomsCollectionReference = db.collection("ChatRooms");
    private CollectionReference userCollectionReference = db.collection("Users");
    private User user;
    private List<ChatRoom> chatRoomList;



    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRoomList = new ArrayList<>();

        getSupportActionBar().hide();
        profileImageView = findViewById(R.id.chat_list_activity_ImageView);
        backImageView = findViewById(R.id.chat_list_back_image);

        backImageView.setOnClickListener(view -> {
            startActivity(new Intent(ChatListActivity.this, HomeActivity.class));
        });

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(ChatListActivity.this.getApplication())
                .create(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            User user = users.get(0);
            profileImageUrl = user.getImageUrl();
            Glide.with(this).load(profileImageUrl).into(profileImageView);
            username = user.getUsername();
        });
        firebaseAuth = FirebaseAuth.getInstance();

        chatRecyclerView = findViewById(R.id.chat_list_RecyclerView);
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ChatRoom chatRoom = chatRoomList.get(position);
                chatRoomsCollectionReference
                        .document(chatRoom.getChatRoomId())
                        .delete()
                        .addOnCompleteListener(task -> {

                        });
            }
        }).attachToRecyclerView(chatRecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatRoomList = new ArrayList<>();
        getChatRooms();

    }

    private void getChatRooms(){
        currentUser = firebaseAuth.getCurrentUser();
        Log.d(TAG, "line 96: " + currentUser.getUid());
        chatRoomsCollectionReference
                .whereArrayContains("users", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot chatRooms : queryDocumentSnapshots) {
                            ChatRoom chatRoom = chatRooms.toObject(ChatRoom.class);
                            chatRoomList.add(chatRoom);
                        }
                        // Setup adapter
                        chatRecyclerViewAdapter = new ChatAdapter(this, chatRoomList,
                                userCollectionReference, currentUser.getUid(), profileImageUrl, username);
                        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);
                        chatRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "chat room is empty");
                    }
                });
    }
}