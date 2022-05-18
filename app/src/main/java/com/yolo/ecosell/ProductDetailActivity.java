package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    private String userId, productId, productPrice, condition, productCategory, productDescription, productDeliveryMethod;
    private String productSeller, productBuyer, imageUrl;
    private ImageButton likeButton;
    private int heartNotLiked, heartLiked;
    private boolean likeClicked = false;

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productSellerTextView,
            productConditionTextView, productCategoryTextView, productDescriptionTextView, deliveryMethodTextView;
    private ExtendedFloatingActionButton chatFloatingButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRoomsCollectionReference = db.collection("ChatRooms");
    private CollectionReference userCollectionReference = db.collection("Users");
    private CollectionReference productCollectionReference = db.collection("Products");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        itemAtIndex = intent.getIntExtra("itemAtIndex", 0);
        title = intent.getStringExtra("title");
        productId = intent.getStringExtra("productId");

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
        likeButton = findViewById(R.id.likeButton);

        heartLiked = R.drawable.ic_baseline_favorite_24_red;
        heartNotLiked = R.drawable.ic_outline_favorite_border_24;

        // floating action buttons;
        chatFloatingButton = findViewById(R.id.product_detail_chat_floating_action);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);

        productViewModel.getAllProducts().observe(this, products -> {
            for (Product p : products) {
                if (p.getProductId().equals(productId)) {
                    product = p;
                    likeClicked = product.getLikes().contains(userId);

                    if (likeClicked) {
                        likeButton.setImageResource(heartLiked);
                    } else {

                        likeButton.setImageResource(heartNotLiked);
                    }
                    setDataToUI(product);
                    break;
                }
            }
        });


        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);


        userViewModel.getAllUsers().observe(this, users -> {
            user = users.get(0);
            userId = user.getUserId();
        });

        chatFloatingButton.setOnClickListener(view -> {
            createChatRoom();
        });

        likeButton.setOnClickListener(view -> {

            if (likeClicked) {
                likeButton.setImageResource(heartNotLiked);
                likeClicked = false;
                // pull from list
                removeLikes();
            } else {
                likeButton.setImageResource(heartLiked);
                likeClicked = true;
                // insert to list
                addLikes();
            }
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

    private void addLikes() {
        productCollectionReference
                .document(productId)
                .update("likes", FieldValue.arrayUnion(userId))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        userCollectionReference
                                .document(userId)
                                .update("likes", FieldValue.arrayUnion(productId))
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(this, "Added to likes", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void removeLikes() {
        productCollectionReference
                .document(productId)
                .update("likes", FieldValue.arrayRemove(userId))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        userCollectionReference
                                .document(userId)
                                .update("likes", FieldValue.arrayRemove(productId))
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(this, "Removed from likes", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {

                });
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
        String uniqueId1 = userId + sellerId;
        String uniqueId2 = sellerId + userId;
        ChatRoom chatRoom = new ChatRoom(users, new ArrayList<>(), chatRoomDocRef.getId(), uniqueId1, uniqueId2, null);

        chatRoomsCollectionReference
                .whereNotIn("users", Arrays.asList(userId, sellerId))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        chatRoomDocRef
                                .set(chatRoom, SetOptions.merge())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(ProductDetailActivity.this, ChatListActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        Toast.makeText(this, "You already have a chat room with this user", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, ChatListActivity.class);
                        startActivity(intent);
                    }
                });

    }

}