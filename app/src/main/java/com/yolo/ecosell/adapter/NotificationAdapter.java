package com.yolo.ecosell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Post;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context context;
    public NotificationAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.notification_username.setText("dummy2Username");
        holder.notification_content.setText("this is a test notification");
        holder.notification_time.setText("13:31:23 15/05/21");

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView notification_usericon;
        private TextView notification_username, notification_content, notification_time;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_usericon = itemView.findViewById(R.id.notification_usericon);
            notification_username = itemView.findViewById(R.id.notification_username);
            notification_content = itemView.findViewById(R.id.notification_content);
            notification_time = itemView.findViewById(R.id.notification_time);


        }
    }
}
