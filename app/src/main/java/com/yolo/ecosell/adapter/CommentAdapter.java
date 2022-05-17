package com.yolo.ecosell.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private Context context;
    public CommentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.comment_username.setText("dummyUsername");
        holder.comment_content.setText("this is a test comment");
        holder.comment_time.setText("13:31:23 14/05/21");

    }



    @Override
    public int getItemCount() {
        return 3;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView comment_usericon;
        private TextView comment_username, comment_content, comment_time;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_usericon = itemView.findViewById(R.id.comment_usericon);
            comment_username = itemView.findViewById(R.id.comment_username);
            comment_content = itemView.findViewById(R.id.comment_content);
            comment_time = itemView.findViewById(R.id.comment_time);
        }
    }
}
