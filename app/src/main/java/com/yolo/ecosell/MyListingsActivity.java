package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.User;
import model.UserViewModel;

public class MyListingsActivity extends AppCompatActivity {
    private final String TAG = "MyListingActivity";

    private RecyclerView listingRecyclerView;
    private ListingRecyclerViewAdapter listingRecyclerViewAdapter;
    private UserViewModel userViewModel;
    private String userId, notCurrentUserName;
    private User user;
    private List<Product> productList = new ArrayList<>();

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference productsCollectionReference = db.collection("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        notCurrentUserName = intent.getStringExtra("notCurrentUser");

        if (notCurrentUserName != null){
            getSupportActionBar().setTitle(notCurrentUserName + "'s listing(s)");
        }else{
            getSupportActionBar().setTitle("My Listings");
        }

        listingRecyclerView = findViewById(R.id.my_listing_recycler_view);
        listingRecyclerView.hasFixedSize();
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (productList.isEmpty()){
            getUserProducts();
        }

    }

    private void getUserProducts(){
        Query docQuery = productsCollectionReference.orderBy("timeAdded", Query.Direction.DESCENDING);
        try {
            docQuery
                    .whereEqualTo("productSeller", userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot val: queryDocumentSnapshots){
                                Product product = val.toObject(Product.class);
                                Log.d(TAG, product.getProductName());
                                productList.add(product);
                            }

                            // Setup adapter
                            listingRecyclerViewAdapter = new ListingRecyclerViewAdapter(MyListingsActivity.this, productList);
                            listingRecyclerView.setAdapter(listingRecyclerViewAdapter);
                            listingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                        }
                    })
                    .addOnFailureListener(e -> {

                    });


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}