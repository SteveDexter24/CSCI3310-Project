package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import model.User;
import model.UserViewModel;

public class CreateAccountActivity extends AppCompatActivity {
    private Button goToSignIn, createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Bitmap bmp = null;
    private byte[] imageCompressedData;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference collectionReference = db.collection("Users");

    // Progress Bar
    private ProgressDialog progressDialog;


    private ImageView profileImageButton;
    private EditText emailEditText, passwordEditText, usernameEditText, locationEditText, phoneNumberEditText;
    private ProgressBar progressBar;
    private Uri imageUri;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileImageButton = findViewById(R.id.sign_up_profile_image);
        getImage();
        progressBar = findViewById(R.id.login_progress);
        usernameEditText = findViewById(R.id.username_editText);
        emailEditText = findViewById(R.id.sign_up_email);
        phoneNumberEditText = findViewById(R.id.sign_up_phone_number);
        passwordEditText = findViewById(R.id.sign_up_password);
        locationEditText = findViewById(R.id.sign_up_location);
        createAccountButton = findViewById(R.id.sign_up_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(CreateAccountActivity.this.getApplication())
                .create(UserViewModel.class);


        goToSignIn = findViewById(R.id.link_to_sign_in);
        goToSignInActivity();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    // User is logged in
                } else {
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
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(location) && imageUri != null && !TextUtils.isEmpty(phoneNumber)) {
                    createUserByEmailAddPassword(username, email, password, location, phoneNumber);
                } else {
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

    private void goToSignInActivity() {
        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }

    private void getImage() {
        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageLibrary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageLibrary, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                imageCompressedData = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImageButton.setImageURI(imageUri);
        }
    }

    private void getDeviceId(){


    }


    private void createUserByEmailAddPassword(String username, String email, String password, String location, String phoneNumber) {
        if (!TextUtils.isEmpty(email) && email.contains("@") && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(location)) {
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Take User to Home Screen
                            currentUser = firebaseAuth.getCurrentUser();
                            assert currentUser != null;
                            String userId = currentUser.getUid();

                            StorageReference filepath = storageReference
                                    .child("profile_images")
                                    .child(userId);

                            filepath.putBytes(imageCompressedData)
                                    .addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                FirebaseMessaging
                                                        .getInstance()
                                                        .getToken()
                                                        .addOnCompleteListener(task1 -> {
                                                            if (!task1.isSuccessful()){
                                                                return;
                                                            }
                                                            User user = new User();
                                                            Log.d("createAcc", task1.getResult());
                                                            user.setDeviceToken(task1.getResult());
                                                            user.setUserId(userId);
                                                            user.setUsername(username);
                                                            user.setEmail(email);
                                                            user.setLocation(location);
                                                            user.setMobile(phoneNumber);
                                                            user.setProducts(new ArrayList<>());
                                                            user.setChatRooms(new ArrayList<>());
                                                            user.setLikes(new ArrayList<>());
                                                            user.setGroups(new ArrayList<>());
                                                            user.setImageUrl(uri.toString());
                                                            user.setTimeAdded(new Timestamp(new Date()).toString());
                                                            UserViewModel.insert(user);
                                                            createUserObjectEntry(user, userId);
                                                        });
                                            }))
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.hide();
                                        }
                                    });
                        } else {
                            // something went wrong
                            progressDialog.hide();
                        }
                    })
                    .addOnFailureListener(e -> progressDialog.hide());
        } else {
            progressDialog.hide();
        }
    }

    private void createUserObjectEntry(User user, String userId) {
        collectionReference
                .document(userId)
                .set(user, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.hide();
                        Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("userId", user.getUserId());
                        startActivity(intent);
                    } else {
                        progressDialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}