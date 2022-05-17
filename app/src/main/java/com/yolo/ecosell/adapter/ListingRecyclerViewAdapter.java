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
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yolo.ecosell.ProductDetailActivity;
import com.yolo.ecosell.R;

import java.util.List;

import model.Product;

public class ListingRecyclerViewAdapter extends RecyclerView.Adapter<ListingRecyclerViewAdapter.ListingsViewHolder> {

    private Context context;
    private List<Product> productlist;

    public ListingRecyclerViewAdapter(Context context, List<Product> productlist){
        this.context = context;
        this.productlist = productlist;
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
        Product product = productlist.get(position);

        holder.priceTextView.setText("HK$" + product.getProductPrice());
        holder.productNameTextView.setText(product.getProductName());
        holder.usageTextView.setText(product.getCondition());
        holder.sellerTextView.setText(product.getSellerUserName());
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrls().get(0)).into(holder.productImageView);

        Glide.with(holder.itemView.getContext())
                .load(product.getSellerImageUrl()).into(holder.sellerImageView);

        holder.listingCardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("itemAtIndex", position);
            intent.putExtra("title", product.getProductName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class ListingsViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImageView, sellerImageView;
        private TextView productNameTextView, priceTextView, usageTextView, sellerTextView;
        private CardView listingCardView;

        public ListingsViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.listing_imageView);
            sellerImageView = itemView.findViewById(R.id.listing_user_profile_imageView);

            productNameTextView = itemView.findViewById(R.id.listing_title_textView);
            priceTextView = itemView.findViewById(R.id.listing_price_textView);
            usageTextView = itemView.findViewById(R.id.listing_usage_textView);
            sellerTextView = itemView.findViewById(R.id.listing_user_textView);

            listingCardView = itemView.findViewById(R.id.my_listing_row_card_view);
        }
    }
}
