package com.yolo.ecosell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.R;

public class ListingRecyclerViewAdapter extends RecyclerView.Adapter<ListingRecyclerViewAdapter.ListingsViewHolder> {

    //private LiveData<List<>>
    private Context context;

    public ListingRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ListingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_listing_row, parent, false);
        return new ListingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingsViewHolder holder, int position) {
        holder.priceTextView.setText("HK$100");
        holder.productNameTextView.setText("Pants");
        holder.usageTextView.setText("90% New");
        holder.sellerTextView.setText(("DummyUser1234"));
        Glide.with(holder.itemView.getContext())
                .load("https://images.lululemon.com/is/image/lululemon/LM5ADRS_0001_1?size=800,800").into(holder.productImageView);
//        Glide.with(holder.itemView.getContext())
//                .load("https://firebasestorage.googleapis.com/v0/b/ecosell.appspot.com/o/profile_images%2FhKasy7xBUAgxvQJLznGTtYGVsaA2?alt=media&token=3b53f2d1-d1b0-4555-a0a2-31feb50edd1e").into(holder.sellerImageView);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ListingsViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImageView, sellerImageView;
        private TextView productNameTextView, priceTextView, usageTextView, sellerTextView;

        public ListingsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.listing_imageView);
            sellerImageView = itemView.findViewById(R.id.listing_user_profile_imageView);

            productNameTextView = itemView.findViewById(R.id.listing_title_textView);
            priceTextView = itemView.findViewById(R.id.listing_price_textView);
            usageTextView = itemView.findViewById(R.id.listing_usage_textView);
            sellerTextView = itemView.findViewById(R.id.listing_user_textView);
        }
    }
}
