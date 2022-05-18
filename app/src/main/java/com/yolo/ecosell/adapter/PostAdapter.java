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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.R;

import java.util.List;

import model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postLists;

    public PostAdapter(Context context, List<Post> postLists){
        this.context = context;
        this.postLists = postLists;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post post = postLists.get(position);
        Glide.with(holder.itemView.getContext())
                .load(post.getUserImageUrl()).into(holder.userImageView);

        Glide.with(holder.itemView.getContext())
                .load(post.getPostImageUrl()).into(holder.postImageView);

        holder.usernameTextView.setText(post.getUsername());
        holder.postDescriptionTextView.setText(post.getPostMessage());
        holder.likesTextView.setText("" + post.getLikes().size());

        holder.likeButton.setOnClickListener(view -> {
            // set likes
        });

    }


    @Override
    public int getItemCount() {
        return postLists.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView, postImageView;
        private TextView usernameTextView, postDescriptionTextView, likesTextView;
        private CardView postCardView;
        private ImageButton likeButton;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.post_usericon);
            usernameTextView = itemView.findViewById(R.id.post_username);
            postDescriptionTextView = itemView.findViewById(R.id.post_description);
            postImageView = itemView.findViewById(R.id.post_image);
            likesTextView = itemView.findViewById(R.id.post_like_text_view);
            likeButton = itemView.findViewById(R.id.post_like_image);

        }
    }
}
