package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import model.User;
import model.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button createAccount, signInButton, signInWithGoogleButton;
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        createAccount = findViewById(R.id.link_to_signup);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);
        signInButton = findViewById(R.id.email_sign_in_button);

        emailEditText.setText("dummy2@gmail.com");
        passwordEditText.setText("12345678");

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(LoginActivity.this.getApplication())
                .create(UserViewModel.class);

        goToCreateAccountActivity();
        loginWithEmailAndPassword();
    }
    private void goToCreateAccountActivity(){
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });
    }
    private void loginWithEmailAndPassword(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  if (task.isSuccessful()){
                                      FirebaseUser loggedInUser = firebaseAuth.getCurrentUser();
                                      assert loggedInUser != null;
                                      final String currentUserId = loggedInUser.getUid();
                                      getUserCollectionRef(currentUserId);
                                  }
                              }
                          })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Dialogue failed to login
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                }else{
                    // Show Dialog to tell users that email or password field is not valid
                }
            }
        });
    }
    private void getUserCollectionRef(String currentUserId){
        collectionReference
                .whereEqualTo("userId", currentUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {

                            progressBar.setVisibility(View.INVISIBLE);
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                User user = new User();
                                user.setEmail(snapshot.getString("email"));
                                user.setUsername(snapshot.getString("username"));
                                user.setUserId(snapshot.getString("userId"));
                                user.setLocation(snapshot.getString("location"));
                                user.setImageUrl(snapshot.getString("imageUrl"));
                                user.setTimeAdded(snapshot.getData().get("timeAdded").toString());
                                UserViewModel.insert(user);

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }

                        }
                    }
                });
    }
}