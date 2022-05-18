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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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

public class CreatePostActivity extends AppCompatActivity {

    private String groupId;
    private EditText postDescription;
    private ImageButton postImageButton;
    private Button createPostButton;
    private UserViewModel userViewModel;
    private User user;

    private Uri imageUri = null;
    private Bitmap bmp = null;
    private byte[] imageCompressedData;

    // Progress Dialog
    private ProgressDialog progressBar;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Groups Collection
    private CollectionReference groupCollectionReference = db.collection("Groups");
    // Posts Collection
    private CollectionReference postCollectionReference = db.collection("Posts");
    // Group Document Reference
    private DocumentReference postDocumentReference = postCollectionReference.document();

    // Storage
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        postDescription = findViewById(R.id.post_description_edit_text);
        postImageButton = findViewById(R.id.add_post_photo);
        createPostButton = findViewById(R.id.create_post_button);

        // Dialogue
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Creating Post ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            user = users.get(0);
        });

        createPostButton.setOnClickListener(view -> {
            createPost();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getImage();
    }

    private void getImage() {
        postImageButton.setOnClickListener(view -> {
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
            postImageButton.setImageURI(imageUri);
        }
    }

    private void createPost() {
        String postMessage = postDescription.getText().toString().trim();
        if (TextUtils.isEmpty(postMessage) && imageCompressedData == null) {
            Toast.makeText(this, "Cannot submit empty post", Toast.LENGTH_LONG).show();
            return;
        }

        StorageReference filepath = storageReference
                .child("posts_photo")
                .child(new Date().toString());

        try {
            progressBar.show();
            createPostButton.setEnabled(false);
            filepath
                    .putBytes(imageCompressedData)
                    .addOnSuccessListener(taskSnapshot -> {
                        // successfully add photo
                        filepath
                                .getDownloadUrl().addOnSuccessListener(uri -> {

                            DocumentReference postDocRef = postCollectionReference.document();
                            Post post = new Post(postDocRef.getId(), groupId, user.getImageUrl(),
                                    user.getUsername(), uri.toString(), postMessage, Timestamp.now(), new ArrayList<>());

                            postDocRef
                                    .set(post)
                                    .addOnCompleteListener(task -> {
                                        progressBar.hide();
                                        Toast.makeText(this, "Successfully created post", Toast.LENGTH_LONG).show();
                                        finish();
                                    });

                        })
                                .addOnFailureListener(e -> progressBar.hide());
                    })
                    .addOnFailureListener(e -> progressBar.hide());

        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }

    }

}