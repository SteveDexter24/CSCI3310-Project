package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import model.User;

public class UserActivity extends AppCompatActivity {
    private final String TAG = "UserActivity";
    private String userId;
    private ImageView profileImageView;
    private TextView usernameTextView, emailTextView, mobileTextView, locationTextView, listingTextView;
    private LinearLayout listingsLinearLayout, paymentLinearLayout;
    private User user;


    // Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        userId = intent.getStringExtra("sellerId");
        getSupportActionBar().setTitle(intent.getStringExtra("username"));
        Log.d(TAG, userId);

        profileImageView = findViewById(R.id.user_profile_imageView);
        usernameTextView = findViewById(R.id.user_username_text_view);
        emailTextView = findViewById(R.id.user_email_text_view);
        mobileTextView = findViewById(R.id.user_mobile_phone_number);
        locationTextView = findViewById(R.id.user_location);
        listingTextView = findViewById(R.id.user_listing_count);
        listingsLinearLayout = findViewById(R.id.user_listing_layout);
        paymentLinearLayout = findViewById(R.id.user_payment_layout);

        listingsLinearLayout.setOnClickListener(view -> {
            goToListings();
        });

        paymentLinearLayout.setOnClickListener(view -> {
            goToPayment();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUser();
    }

    private void getUser() {
        userCollectionReference
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    assert queryDocumentSnapshots != null;
                    try {
                        for (QueryDocumentSnapshot val : queryDocumentSnapshots) {
                            user = val.toObject(User.class);
                        }

                        Glide.with(this).load(user.getImageUrl()).into(profileImageView);
                        usernameTextView.setText(user.getUsername());
                        emailTextView.setText(user.getEmail());
                        mobileTextView.setText(user.getMobile());
                        locationTextView.setText(user.getLocation());
                        listingTextView.setText(user.getProducts().size() + " listing(s)");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void goToListings(){
        Intent intent = new Intent(UserActivity.this, MyListingsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("notCurrentUser", usernameTextView.getText().toString());
        startActivity(intent);
    }

    private void goToPayment(){
        Intent intent = new Intent(UserActivity.this, UserPaymentActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("username", usernameTextView.getText().toString());
        intent.putExtra("qrCode", user.getQrImageUrl());
        startActivity(intent);
    }
}