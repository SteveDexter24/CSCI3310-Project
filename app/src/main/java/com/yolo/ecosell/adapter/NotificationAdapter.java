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

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Notification;
import model.Post;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context context;
    private List<Notification> notificationList;
    public NotificationAdapter(Context context, List<Notification> notificationList){
        this.context = context;
        this.notificationList = notificationList;
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
        Notification notification =  notificationList.get(position);
        PrettyTime p = new PrettyTime();
        Date date = notification.getSentTime().toDate();
        String ago = p.format(date);
        holder.notification_username.setText(notification.getSenderUsername());
        holder.notification_content.setText(notification.getMessage());
        holder.notification_time.setText(ago);
        Glide.with(holder.itemView.getContext())
                .load(notification.getImageUrl()).into(holder.notification_usericon);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ImageView notification_usericon;
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
