package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import model.Group;

public class CreateGroupActivity extends AppCompatActivity {

    private final String TAG = "CreateGroupActivity";

    private EditText groupTitleEditText, groupDescriptionEditText;
    private ImageButton groupPhotoImageView;
    private Button createGroupButton;
    private String currentUserId;

    private Uri imageUri = null;
    private Bitmap bmp = null;
    private byte[] imageCompressedData;

    // Progress Dialog
    private ProgressDialog progressBar;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference userCollectionReference = db.collection("Users");
    // Group Collection
    private CollectionReference groupCollectionReference = db.collection("Groups");
    // Group Document Reference
    private DocumentReference groupDocumentReference = groupCollectionReference.document();
    // Auth
    private FirebaseAuth firebaseAuth;
    // Storage
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Firebase get current user
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();

        // Firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Find views in xml
        groupTitleEditText = findViewById(R.id.add_group_name_edit_text);
        groupDescriptionEditText = findViewById(R.id.add_listing_description_editText_view);
        groupPhotoImageView = findViewById(R.id.add_group_photo);
        createGroupButton = findViewById(R.id.create_group_button);

        // Set Action bar title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Group");

        // Dialogue
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Creating Group ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        createGroupButton.setOnClickListener(view -> createGroup());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getImage();
    }

    private void getImage() {
        groupPhotoImageView.setOnClickListener(view -> {
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
            groupPhotoImageView.setImageURI(imageUri);
        }
    }

    private Boolean areFieldsComplete(String title, String description, Uri imageUri) {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null;
    }

    private void createGroup() {
        String title = groupTitleEditText.getText().toString().trim();
        String description = groupDescriptionEditText.getText().toString().trim();
        if (!areFieldsComplete(title, description, imageUri)) return;

        StorageReference filepath = storageReference
                .child("group_photo")
                .child(new Date().toString());

        try {
            progressBar.show();
            createGroupButton.setEnabled(false);
            filepath
                    .putBytes(imageCompressedData)
                    .addOnSuccessListener(taskSnapshot -> {
                        // successfully add photo
                        filepath
                                .getDownloadUrl().addOnSuccessListener(uri -> {
                                    List <String> groupUsers = new ArrayList<>();
                                    groupUsers.add(currentUserId);

                            Group group = new Group(Timestamp.now(), title,
                                    description, currentUserId,
                                    uri.toString(), groupUsers, groupDocumentReference.getId());

                            groupCollectionReference
                                    .add(group)
                                    .addOnSuccessListener(documentReference -> userCollectionReference
                                            .document(currentUserId)
                                            .update("groups", FieldValue.arrayUnion(documentReference.toString()))
                                            .addOnCompleteListener(task -> {
                                                Log.d(TAG, "add group to user");
                                                progressBar.hide();
                                                createGroupButton.setEnabled(true);
                                                Toast.makeText(getApplicationContext(),
                                                        "Successfully created a new group", Toast.LENGTH_LONG).show();
                                                groupTitleEditText.setText("");
                                                groupDescriptionEditText.setText("");
                                            }));

                        }).addOnFailureListener(e -> progressBar.hide());
                    })
                    .addOnFailureListener(e -> progressBar.hide());

        } catch (Exception e) {
            e.printStackTrace();
            progressBar.hide();
        }
    }


}