package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.ProductViewModel;
import model.User;
import model.UserViewModel;

public class LikesActivity extends AppCompatActivity {
    private final String TAG = "LikesActivity";

    private RecyclerView likesRecyclerView;
    private ListingRecyclerViewAdapter liesRecyclerViewAdapter;
    private List<Product> productList = new ArrayList<>();
    private ProductViewModel productViewModel;
    private UserViewModel userViewModel;
    private User user;
    private String userId;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference productsCollectionReference = db.collection("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        getSupportActionBar().setTitle("My Favourite Products");

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);
        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            user = users.get(0);
            userId = user.getUserId();
            Log.d(TAG, user.getUserId());
        });

        likesRecyclerView = findViewById(R.id.likes_recyclerView);
        likesRecyclerView.setHasFixedSize(true);
        likesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        productList.clear();
        if (productList.isEmpty()) {
            userViewModel.getAllUsers().observe(this, users -> {
                user = users.get(0);
                userId = user.getUserId();
                getlikedProducts(userId);
                Log.d(TAG, user.getUserId());
            });
        }

    }

    private void getlikedProducts(String id) {
        try {
            Log.d(TAG, "userId: " + id);
            productsCollectionReference
                    .whereArrayContains("likes", id)
                    .orderBy("timeAdded", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot products : task.getResult()) {
                                Product product = products.toObject(Product.class);
                                productList.add(product);
                                productViewModel.insertProduct(product);
                            }

                            // Setup adapter
                            Log.d(TAG, productList.size() + "");
                            liesRecyclerViewAdapter = new ListingRecyclerViewAdapter(this, productList);
                            likesRecyclerView.setAdapter(liesRecyclerViewAdapter);
                            likesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}