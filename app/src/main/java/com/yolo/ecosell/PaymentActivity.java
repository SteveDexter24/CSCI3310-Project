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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import model.Post;
import model.User;
import model.UserViewModel;

public class PaymentActivity extends AppCompatActivity {

    private ImageButton qrImageButton;
    private Button addQRButton;
    private String userId, qrImageUrl;

    private Uri imageUri = null;
    private Bitmap bmp = null;
    private byte[] imageCompressedData;
    private UserViewModel userViewModel;
    private User user;

    // Progress Dialog
    private ProgressDialog progressBar;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Users Collection
    private CollectionReference usersCollectionReference = db.collection("Users");

    // Storage
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Add QR Code");

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        qrImageUrl = intent.getStringExtra("qrImageUrl");

        // Firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        qrImageButton = findViewById(R.id.add_qr_photo);
        addQRButton = findViewById(R.id.create_qr_button);

        // Dialogue
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Adding QR Code ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        qrImageButton.setOnClickListener(view -> getImage());
        addQRButton.setOnClickListener(view -> uploadQRCode());

        Glide.with(this).load(qrImageUrl).into(qrImageButton);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            for (User u : users) {
                if (u.getUserId().equals(userId)) {
                    user = u;
                    break;
                }
            }
        });
    }

    private void getImage() {
        qrImageButton.setOnClickListener(view -> {
            Intent imageLibrary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageLibrary, 2);
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
            qrImageButton.setImageURI(imageUri);
        }
    }

    private void uploadQRCode() {
        if (imageCompressedData == null) {
            Toast.makeText(this, "Please choose a new QR Code", Toast.LENGTH_LONG).show();
            return;
        }
        StorageReference filepath = storageReference
                .child("qr_code")
                .child(new Date().toString());
        try {
            progressBar.show();
            addQRButton.setEnabled(false);

            filepath
                    .putBytes(imageCompressedData)
                    .addOnSuccessListener(taskSnapshot -> {
                        // successfully add photo
                        filepath
                                .getDownloadUrl().addOnSuccessListener(uri -> {
                            usersCollectionReference
                                    .document(userId)
                                    .update("qrImageUrl", uri.toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            user.setQrImageUrl(uri.toString());
                                            userViewModel.insert(user);
                                            progressBar.hide();
                                            Toast.makeText(this, "Successfully added QR Code", Toast.LENGTH_LONG).show();
                                            Glide.with(this).load(uri.toString()).into(qrImageButton);
                                        }

                                    })
                                    .addOnFailureListener(e -> progressBar.hide());

                        }).addOnFailureListener(e -> progressBar.hide());
                    })
                    .addOnFailureListener(e -> progressBar.hide());


        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }
}