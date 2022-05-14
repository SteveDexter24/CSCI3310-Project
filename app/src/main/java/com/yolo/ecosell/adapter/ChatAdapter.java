package com.yolo.ecosell.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.ChatActivity;
import com.yolo.ecosell.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;

    public ChatAdapter(Context context){
        this.context = context;
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
        holder.usernameTextView.setText("DummyUser");
        holder.lastMessageTextView.setText("hahaha");
        holder.chatUnseenTextView.setText("1");
        Glide.with(holder.itemView.getContext())
                .load("https://images.lululemon.com/is/image/lululemon/LM5ADRS_0001_1?size=800,800").into(holder.profileImageView);

        holder.rootLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("username", "DummyUser");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return 1;
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
