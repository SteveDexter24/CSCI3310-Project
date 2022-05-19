package com.yolo.ecosell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.User;
import model.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private final String TAG = "EditProfileActivity";
    private EditText usernameEditText, emailEditText, phoneNumberEditText, locationEditText;
    private Button updateProfileButton;
    private ImageView profileImageView;
    private UserViewModel userViewModel;
    private Uri imageUri;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String oldEmail;
    private User mUser;
    private String new_image_url;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Profile");
        setContentView(R.layout.activity_edit_profile);
        usernameEditText = findViewById(R.id.edit_username);
        emailEditText = findViewById(R.id.edit_email);
        phoneNumberEditText = findViewById(R.id.edit_phone);
        locationEditText = findViewById(R.id.edit_location);
        profileImageView = findViewById(R.id.edit_profile_profile_image);
        updateProfileButton = findViewById(R.id.edit_user_profile_button);
        progressBar = findViewById(R.id.edit_progress);

        getImage();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            mUser = users.get(0);
            Glide.with(this).load(mUser.getImageUrl()).into(profileImageView);
            usernameEditText.setText(mUser.getUsername());
            emailEditText.setText(mUser.getEmail());
            oldEmail = mUser.getEmail();
            phoneNumberEditText.setText(mUser.getMobile());
            locationEditText.setText(mUser.getLocation());
        });

        updateProfileButton.setOnClickListener(view -> {
            // Update User
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();
            String mobileNum = phoneNumberEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(email) &&
                    !TextUtils.isEmpty(username) &&
                    !TextUtils.isEmpty(location) &&
                    !TextUtils.isEmpty(mobileNum)) {
                updateEmailAddress(username, email, location, mobileNum);
            }
        });
    }

    private void updateEmailAddress(String username, String email, String location, String mobileNum){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        progressBar.setVisibility(View.VISIBLE);
        firebaseUser.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User email address updated.");
                        if (imageUri != null) {
                            uploadImage(mUser.getUserId(), imageUri);
                        }
                        mUser.setMobile(mobileNum);
                        mUser.setUsername(username);
                        mUser.setEmail(email);
                        mUser.setLocation(location);
                        updateUserCollection(mUser);
                    }
                })
                .addOnFailureListener(e -> progressBar.setVisibility(View.GONE));
    }

    private void uploadImage(String userId, Uri updated_image_uri) {
        StorageReference filepath = storageReference
                .child("profile_images")
                .child(userId);

        filepath
                .putFile(updated_image_uri)
                .addOnSuccessListener(taskSnapshot -> filepath
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                           mUser.setImageUrl(uri.toString());
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_LONG).show();
                        }));
    }

    private void updateUserCollection(User user) {
        Map<String,Object> updates = new HashMap<>();
        updates.put("imageUrl" ,user.getImageUrl());
        updates.put("username", user.getUsername());
        updates.put("email", user.getEmail());
        updates.put("mobile", user.getMobile());
        updates.put("location", user.getLocation());
        collectionReference
                .document(mUser.getUserId())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    collectionReference
                            .whereEqualTo("userId", user.getUserId())
                            .addSnapshotListener((value, error) -> {
                                assert value != null;
                                Log.d(TAG, "Successfully update user");
                                progressBar.setVisibility(View.GONE);
                                if (!value.isEmpty()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(this, "Successfully updated your profile", Toast.LENGTH_LONG).show();
                                    for (QueryDocumentSnapshot snapshot : value) {
                                        user.setEmail(snapshot.getString("email"));
                                        user.setUsername(snapshot.getString("username"));
                                        user.setLocation(snapshot.getString("location"));
                                        user.setMobile(snapshot.getString("mobile"));
                                        String imageUrl = snapshot.getString("imageUrl");
                                        user.setImageUrl(imageUrl);
                                    }
                                    // By default replaces as long as same primary key
                                    userViewModel.editUser(user);
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to update your profile", Toast.LENGTH_LONG).show();
                });
    }

    private void getImage() {
        profileImageView.setOnClickListener(view -> {
            Intent imageLibrary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageLibrary, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
}