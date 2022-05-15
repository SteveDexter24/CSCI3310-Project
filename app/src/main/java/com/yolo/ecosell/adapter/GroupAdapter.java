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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    public GroupAdapter(Context context){
        this.context = context;
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
        holder.group_item_name.setText("Share/Transaction Group");
        holder.group_item_description.setText("Exchange here and save our planet!");
        Glide.with(holder.itemView.getContext())
                .load("https://images.lululemon.com/is/image/lululemon/LM5ADRS_0001_1?size=800,800").into(holder.group_item_image);

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        private ImageView group_item_image;
        private TextView group_item_name, group_item_description;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            group_item_image = itemView.findViewById(R.id.group_item_image);
            group_item_name = itemView.findViewById(R.id.group_item_name);
            group_item_description = itemView.findViewById(R.id.group_item_description);

        }


    }
}
