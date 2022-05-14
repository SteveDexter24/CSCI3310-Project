package com.yolo.ecosell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yolo.ecosell.R;

import java.util.LinkedList;

public class ChatBubbleAdapter extends RecyclerView.Adapter<ChatBubbleAdapter.ChatBubbleViewHolder> {

    private Context context;
    private LinkedList<String> chatMessages;

    public ChatBubbleAdapter (Context context, LinkedList<String> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatBubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new ChatBubbleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBubbleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class ChatBubbleViewHolder extends RecyclerView.ViewHolder {

        private ImageView senderImageView, receiverImageView;
        private TextView senderUserNameTextView, receiverUserNameTextView;
        private TextView senderTimeTextView, receiverTimeTextView;
        private TextView senderMessageTextView, receiverMessageTextView;

        public ChatBubbleViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImageView = itemView.findViewById(R.id.msg_ousericon);
            receiverImageView = itemView.findViewById(R.id.msg_usericon);

            senderUserNameTextView = itemView.findViewById(R.id.msg_ousername);
            receiverUserNameTextView = itemView.findViewById(R.id.msg_username);

            senderTimeTextView = itemView.findViewById(R.id.msg_otime);
            receiverTimeTextView = itemView.findViewById(R.id.msg_time);

            senderMessageTextView = itemView.findViewById(R.id.msg_ocontent);
            receiverMessageTextView = itemView.findViewById(R.id.msg_content);

        }
    }
}
