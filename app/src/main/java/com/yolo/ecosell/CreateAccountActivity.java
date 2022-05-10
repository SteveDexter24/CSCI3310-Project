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
        goToSignInActivity();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    // User is logged in
                }else{
                    // No user yet
                }
            }
        };

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(username) && TextUtils.isEmpty(location)){
                    createUserByEmailAddPassword(username, email, password, location);
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Some fields are missing", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        //firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void goToSignInActivity(){
        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }

    private void createUserByEmailAddPassword(String username, String email, String password, String location ){
        if (!TextUtils.isEmpty(email) && email.contains("@") && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(location)){
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // Take User to Home Screen
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                String userId = currentUser.getUid();
                                // Create User Map
                                Map<String, String> userObject = new HashMap<>();
                                userObject.put("userId", userId);
                                userObject.put("username", username);
                                userObject.put("email", currentUser.getEmail());
                                userObject.put("location", location);

                                // Add to db collection
                                // Put into another function
                                collectionReference.add(userObject)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.getResult().exists()){
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String username = task.getResult()
                                                                            .getString("username");
                                                                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                                                    intent.putExtra("username", username);
                                                                    intent.putExtra("userId", userObject.get(userId));
                                                                    startActivity(intent);

                                                                }else{

                                                                }
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                            }else{
                                // something went wrong
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else{

        }
    }

}