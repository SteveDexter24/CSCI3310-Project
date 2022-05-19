package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.User;
import model.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button createAccount, signInButton, signInWithGoogleButton;
    private EditText emailEditText, passwordEditText;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private UserViewModel userViewModel;

    // Progress Bar
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        createAccount = findViewById(R.id.link_to_signup);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        signInButton = findViewById(R.id.email_sign_in_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(LoginActivity.this.getApplication())
                .create(UserViewModel.class);

        goToCreateAccountActivity();
        loginWithEmailAndPassword();
    }

    private void goToCreateAccountActivity() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });
    }

    private void loginWithEmailAndPassword() {
        signInButton.setOnClickListener(view -> {

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser loggedInUser = firebaseAuth.getCurrentUser();
                                assert loggedInUser != null;
                                final String currentUserId = loggedInUser.getUid();
                                getUserCollectionRef(currentUserId);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Dialogue failed to login
                            Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        });
            } else {
                // Show Dialog to tell users that email or password field is not valid

            }
        });
    }

    private void getUserCollectionRef(String currentUserId) {
        collectionReference
                .whereEqualTo("userId", currentUserId)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    assert queryDocumentSnapshots != null;
                    if (!queryDocumentSnapshots.isEmpty()) {

                        progressDialog.hide();

                        try {
                            User user = new User();
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                user = snapshot.toObject(User.class);
                            }
                            UserViewModel.insert(user);
                            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } catch (Exception exception) {
                            //Log.d("LoginActivity", exception.getMessage());
                        }
                    }
                });
    }
}