package com.yolo.ecosell.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.GroupDetailActivity;
import com.yolo.ecosell.R;

import java.util.List;

import model.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private List<Group> groupList;
    public GroupAdapter(Context context,List<Group> groupList ){
        this.context = context;
        this.groupList = groupList;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.group_item_name.setText(group.getGroupName());
        holder.group_item_description.setText(group.getGroupDescription());
        Glide.with(holder.itemView.getContext())
                .load(group.getGroupImageUrl()).into(holder.group_item_image);

        // Intent to group detailed view
        holder.groupCardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, GroupDetailActivity.class);
            intent.putExtra("itemAtIndex", position);
            intent.putExtra("title", group.getGroupName());
            intent.putExtra("groupId", group.getGroupId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        private ImageView group_item_image;
        private TextView group_item_name, group_item_description;
        private CardView groupCardView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            group_item_image = itemView.findViewById(R.id.group_item_image);
            group_item_name = itemView.findViewById(R.id.group_item_name);
            group_item_description = itemView.findViewById(R.id.group_item_description);
            groupCardView = itemView.findViewById(R.id.group_item_card_view);

        }


    }
}
