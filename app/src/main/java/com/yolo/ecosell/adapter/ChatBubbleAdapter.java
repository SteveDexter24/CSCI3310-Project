package com.yolo.ecosell.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.R;

import java.util.List;

import model.Chat;

public class ChatBubbleAdapter extends RecyclerView.Adapter<ChatBubbleAdapter.ChatBubbleViewHolder> {

    private final String TAG = "ChatBubbleAdapter";

    private Context context;
    private List<Chat> messageList;
    private String otherUserImageUrl;
    private String currentUserImageUrl;
    private String otherUserUsername;
    private String currentUserUsername;
    private String currentUserId;

    public ChatBubbleAdapter(Context context, List<Chat> messageList,
                             String otherUserImageUrl, String currentUserImageUrl,
                             String otherUserUsername, String currentUserUsername,
                             String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.otherUserImageUrl = otherUserImageUrl;
        this.currentUserImageUrl = currentUserImageUrl;
        this.otherUserUsername = otherUserUsername;
        this.currentUserUsername = currentUserUsername;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatBubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new ChatBubbleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBubbleViewHolder holder, int position) {
        // Each chat bubble holds userMessage, otherUserMessage, time, chatRoomId
        Chat chat = messageList.get(position);

        holder.chatOtherUserLayout.setVisibility(View.GONE);
        holder.chatUserLayout.setVisibility(View.GONE);

        if (chat.getUserId().equals(currentUserId)) {
            holder.chatUserLayout.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(currentUserImageUrl).into(holder.currentUserImageView);
            holder.currentUserNameTextView.setText(currentUserUsername);
            holder.currentTimeTextView.setText(chat.getTime().toDate().toString().split("GMT")[0]);
            holder.currentMessageTextView.setText(chat.getMessage());
        } else {
            holder.chatOtherUserLayout.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(otherUserImageUrl).into(holder.otherImageView);
            holder.otherUserNameTextView.setText(otherUserUsername);
            holder.otherUserTimeTextView.setText(chat.getTime().toDate().toString().split("GMT")[0]);
            holder.otherMessageTextView.setText(chat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ChatBubbleViewHolder extends RecyclerView.ViewHolder {

        private ImageView otherImageView, currentUserImageView;
        private TextView otherUserNameTextView, currentUserNameTextView;
        private TextView otherUserTimeTextView, currentTimeTextView;
        private TextView otherMessageTextView, currentMessageTextView;

        private RelativeLayout chatOtherUserLayout, chatUserLayout;

        public ChatBubbleViewHolder(@NonNull View itemView) {
            super(itemView);

            chatOtherUserLayout = itemView.findViewById(R.id.chatotherUserLayout);
            chatUserLayout = itemView.findViewById(R.id.chatUserLayout);

            otherImageView = itemView.findViewById(R.id.msg_ousericon);
            currentUserImageView = itemView.findViewById(R.id.msg_usericon);

            otherUserNameTextView = itemView.findViewById(R.id.msg_ousername);
            currentUserNameTextView = itemView.findViewById(R.id.msg_username);

            otherUserTimeTextView = itemView.findViewById(R.id.msg_otime);
            currentTimeTextView = itemView.findViewById(R.id.msg_time);

            otherMessageTextView = itemView.findViewById(R.id.msg_ocontent);
            currentMessageTextView = itemView.findViewById(R.id.msg_content);

        }
    }
}
