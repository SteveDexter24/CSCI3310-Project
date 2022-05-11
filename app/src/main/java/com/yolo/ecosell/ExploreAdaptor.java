package com.yolo.ecosell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>{
    private String[] localDataSet;
    private static final String TAG = "ExploreAdaptor";
    private Context context;
    private LayoutInflater mInflater;

    class ExploreViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageItemView;
        public TextView nameItemView;
        final ExploreAdapter mAdapter;

        public ExploreViewHolder(View view, ExploreAdapter adapter) {
            super(view);
            imageItemView = view.findViewById(R.id.explore_image);
            nameItemView = view.findViewById(R.id.explore_text);
            this.mAdapter = adapter;
            // Define click listener for the ViewHolder's View

        }
    }

    @Override
    public ExploreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_item, parent, false);
        return new ExploreViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ExploreViewHolder viewHolder,int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }






}

