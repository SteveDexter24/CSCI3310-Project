package com.yolo.ecosell;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import model.User;
import model.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private String TAG = "Edit Profile Activity";
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

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setTitle("Update Profile");
        setContentView(R.layout.activity_edit_profile);
        usernameEditText = findViewById(R.id.edit_username);
        emailEditText = findViewById(R.id.edit_email);
        phoneNumberEditText = findViewById(R.id.edit_phone);
        locationEditText = findViewById(R.id.edit_location);
        profileImageView = findViewById(R.id.edit_profile_profile_image);
        updateProfileButton = findViewById(R.id.edit_profile_button);
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
            if (TextUtils.isEmpty(email) &&
                    TextUtils.isEmpty(username) &&
                    TextUtils.isEmpty(location)) return;

            progressBar.setVisibility(View.VISIBLE);

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseUser.updateEmail(email)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            User user = mUser;
                            user.setUsername(username);
                            user.setEmail(email);
                            user.setLocation(location);
                            updateUserCollection(user);
                        }
                    }).addOnFailureListener(e -> {

            });
        });

    }

    private void updateUserCollection(User user) {
        collectionReference.document(mUser.getUserId())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully update user");
                    collectionReference
                            .whereEqualTo("userId", user.getUserId())
                            .addSnapshotListener((value, error) -> {
                                assert value != null;
                                if (!value.isEmpty()) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    for (QueryDocumentSnapshot snapshot : value) {
                                        user.setEmail(snapshot.getString("email"));
                                        user.setUsername(snapshot.getString("username"));
                                        user.setLocation(snapshot.getString("location"));
                                    }
                                }
                            });
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