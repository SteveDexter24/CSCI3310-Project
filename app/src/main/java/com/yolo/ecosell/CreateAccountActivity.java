package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    private Button goToSignIn, createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference collectionReference = db.collection("Users");

    private ImageButton profileImageButton;
    private EditText emailEditText, passwordEditText, usernameEditText, locationEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();

        profileImageButton = findViewById(R.id.sign_up_profile_image);
        //profileImageButton.
        getImage();
        progressBar = findViewById(R.id.login_progress);
        usernameEditText = findViewById(R.id.username_editText);
        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);
        locationEditText = findViewById(R.id.sign_up_location);
        createAccountButton = findViewById(R.id.sign_up_button);


        goToSignIn = findViewById(R.id.link_to_sign_in);

        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }
}