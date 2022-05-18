package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import model.UserViewModel;

public class SettingsActivity extends AppCompatActivity {

    private Button logoutButton;
    private UserViewModel userViewModel;
    private FirebaseAuth auth;
    private Button editProfileButton, editPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Settings");

        setContentView(R.layout.activity_settings);
        logoutButton = findViewById(R.id.logout_button);
        auth = FirebaseAuth.getInstance();

        editProfileButton = findViewById(R.id.goto_edit_profile);
        editPasswordButton = findViewById(R.id.go_to_edit_password_button);

        Intent intent = getIntent();

        goToEditPasswordScreen();
        goToEditProfileScreen();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        logoutButton.setOnClickListener(view -> {
            try {
                auth.signOut();
                UserViewModel.deleteAllUsers();
                finishAffinity();

                Toast.makeText(this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finishAffinity();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void goToEditProfileScreen() {
        editProfileButton.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
        });
    }

    private void goToEditPasswordScreen() {
        editPasswordButton.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, EditPasswordActivity.class));
        });
    }
}