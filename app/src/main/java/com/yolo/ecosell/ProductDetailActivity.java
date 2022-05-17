package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.ChatRoom;
import model.Product;
import model.ProductViewModel;
import model.User;
import model.UserViewModel;

public class ProductDetailActivity extends AppCompatActivity {

    private final String TAG = "ProductDetailActivity";

    private int itemAtIndex;
    private String title;
    private ProductViewModel productViewModel;
    private UserViewModel userViewModel;
    private Product product;
    private User user;
    private String userId, productId;

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productSellerTextView,
            productConditionTextView, productCategoryTextView, productDescriptionTextView, deliveryMethodTextView;
    private ExtendedFloatingActionButton chatFloatingButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRoomsCollectionReference = db.collection("ChatRooms");
    private CollectionReference userCollectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        itemAtIndex = intent.getIntExtra("itemAtIndex", 0);
        title = intent.getStringExtra("title");

        getSupportActionBar().setTitle(title);

        // get component from ui
        productImageView = findViewById(R.id.productImage);
        productNameTextView = findViewById(R.id.productName);
        productPriceTextView = findViewById(R.id.productPrice);
        productSellerTextView = findViewById(R.id.productSeller);
        productConditionTextView = findViewById(R.id.productCondition);
        productCategoryTextView = findViewById(R.id.productCategory);
        productDescriptionTextView = findViewById(R.id.productDescription);
        deliveryMethodTextView = findViewById(R.id.productDeliveryMethod);

        // floating action buttons;
        chatFloatingButton = findViewById(R.id.product_detail_chat_floating_action);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        productViewModel.getAllProducts().observe(this, products -> {
            product = products.get(itemAtIndex);
            productId = product.getProductId();
            setDataToUI(product);
        });

        userViewModel.getAllUsers().observe(this, users -> {
            user = users.get(0);
            userId = user.getUserId();
        });

        chatFloatingButton.setOnClickListener(view -> {
            createChatRoom();
        });

    }

    private void setDataToUI(Product product) {
        Glide.with(this)
                .load(product.getImageUrls().get(0)).into(productImageView);
        productNameTextView.setText(product.getProductName());
        productPriceTextView.setText("HK$: " + product.getProductPrice());
        productSellerTextView.setText(product.getSellerUserName());
        productConditionTextView.setText(product.getCondition());
        productCategoryTextView.setText(product.getProductCategory());
        productDescriptionTextView.setText(product.getProductDescription());
        deliveryMethodTextView.setText(product.getProductDeliveryMethod());

    }

    private void createChatRoom() {
        //String chatRoomId = chatRoomsCollectionReference.document().getId();
        DocumentReference chatRoomDocRef = chatRoomsCollectionReference.document();
        List<String> users = new ArrayList<>();
        String userId = user.getUserId();
        String sellerId = product.getProductSeller();
        if (userId.equals(sellerId)) {
            Log.d(TAG, userId);
            Log.d(TAG, sellerId);
            Toast.makeText(getApplicationContext(), "Cannot create a chat with yourself", Toast.LENGTH_LONG).show();
            return;
        }
        users.add(user.getUserId());
        users.add(product.getProductSeller());
        String uniqueId = userId + sellerId;
        ChatRoom chatRoom = new ChatRoom(users, new ArrayList<>(), chatRoomDocRef.getId(), uniqueId, null);

        chatRoomsCollectionReference
                .whereEqualTo("uniqueChatRoomIdentifier", uniqueId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "You already have a chat room with this user", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, ChatActivity.class);
                        for (QueryDocumentSnapshot val : queryDocumentSnapshots) {
                            intent.putExtra("chatRoomId", val.getString("chatRoomId"));
                            intent.putExtra("profileImage", product.getSellerImageUrl());
                            intent.putExtra("username", product.getSellerUserName());
                            intent.putExtra("currentUserProfileImage", user.getImageUrl());
                            intent.putExtra("currentUserUsername", user.getUsername());
                        }
                        startActivity(intent);
                    } else {
                        chatRoomDocRef
                                .set(chatRoom)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(ProductDetailActivity.this, ChatListActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {

                });


    }

}