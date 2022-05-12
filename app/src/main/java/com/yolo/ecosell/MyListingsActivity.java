package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

public class MyListingsActivity extends AppCompatActivity {

    private RecyclerView listingRecyclerView;
    private ListingRecyclerViewAdapter listingRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        getSupportActionBar().setTitle("My Listings");

        listingRecyclerView = findViewById(R.id.my_listing_recycler_view);
        listingRecyclerView.hasFixedSize();
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup adapter
        listingRecyclerViewAdapter = new ListingRecyclerViewAdapter(MyListingsActivity.this);

        listingRecyclerView.setAdapter(listingRecyclerViewAdapter);

        listingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}