package com.yolo.ecosell;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import model.User;
import model.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, phoneNumberEditText, locationEditText;
    private Button updateProfileButton;
    private ImageView profileImageView;
    private UserViewModel userViewModel;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Profile");
        setContentView(R.layout.activity_edit_profile);
        usernameEditText = findViewById(R.id.edit_username);
        emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        phoneNumberEditText = findViewById(R.id.edit_phone);
        locationEditText = findViewById(R.id.edit_location);
        profileImageView = findViewById(R.id.edit_profile_profile_image);
        updateProfileButton = findViewById(R.id.edit_profile_button);

        getImage();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            User user = users.get(0);
            Glide.with(this).load(user.getImageUrl()).into(profileImageView);
            usernameEditText.setText(user.getUsername());
            emailEditText.setText(user.getEmail());
            phoneNumberEditText.setText(user.getMobile());
            locationEditText.setText(user.getLocation());
        });

        updateProfileButton.setOnClickListener(view -> {
            // Update User
        });
    }

    private void getImage() {
        profileImageView.setOnClickListener(view -> {
            Intent imageLibrary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageLibrary, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
}