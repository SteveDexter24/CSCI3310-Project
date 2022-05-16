package com.yolo.ecosell.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.ChatActivity;
import com.yolo.ecosell.R;

import java.util.List;

import model.ChatRoom;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final String TAG = "ChatAdapter";
    private Context context;
    private List<ChatRoom> chatRoomList;
    private CollectionReference userCollectionReference;
    private String currentUserId;

    public ChatAdapter(Context context, List<ChatRoom> chatRoomList, CollectionReference userCollectionReference, String currentUserId) {
        this.context = context;
        this.chatRoomList = chatRoomList;
        this.userCollectionReference = userCollectionReference;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_adapter_layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        ChatRoom chatRoom = chatRoomList.get(position);
        String otherUserId = null;
        for (String users : chatRoom.getUsers()) {
            if (!users.equals(currentUserId)) {
                otherUserId = users;
            }
        }

        try {

            userCollectionReference
                    .whereEqualTo("userId", otherUserId)
                    .addSnapshotListener((value, error) -> {
                        assert value != null;
                        if (!value.isEmpty()) {
                            try {
                                for (QueryDocumentSnapshot snapshot : value) {
                                    String username = snapshot.getString("username");
                                    String profileImage = snapshot.getString("imageUrl");
                                    holder.usernameTextView.setText(username);
                                    holder.lastMessageTextView.setText("hahaha");
                                    holder.chatUnseenTextView.setText("1");
                                    Glide.with(holder.itemView.getContext())
                                            .load(profileImage).into(holder.profileImageView);
                                    holder.rootLinearLayout.setOnClickListener(view -> {
                                        Intent intent = new Intent(context, ChatActivity.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("profileImage", profileImage);
                                        intent.putExtra("chatRoomId", chatRoom.getChatRoomId());
                                        context.startActivity(intent);
                                    });
                                }


                            } catch (Exception e) {
                            }
                        }
                    });
        }catch(Exception error){
            Log.d(TAG, "Cannot get chat room details");
        }

    }

    private void getOtherUserDetails(DocumentReference otherUser) {
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImageView;
        private TextView usernameTextView, lastMessageTextView, chatUnseenTextView;
        private LinearLayout rootLinearLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.chat_profile_image);
            usernameTextView = itemView.findViewById(R.id.chat_username_textView);
            lastMessageTextView = itemView.findViewById(R.id.chat_last_message_textView);
            chatUnseenTextView = itemView.findViewById(R.id.chat_unseen_textView);
            rootLinearLayout = itemView.findViewById(R.id.chat_root_linearLayout);
        }
    }

}
