package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.User;
import model.UserViewModel;

public class EditPasswordActivity extends AppCompatActivity {

    private String TAG = "EditPasswordActivity";

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button updatePasswordButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String email;
    private User mUser;
    private UserViewModel userViewModel;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // User Collection
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            mUser = users.get(0);
            email = mUser.getEmail();
        });

        setContentView(R.layout.activity_edit_password);
        oldPasswordEditText = findViewById(R.id.edit_old_password);
        newPasswordEditText = findViewById(R.id.edit_password);
        updatePasswordButton = findViewById(R.id.edit_password_button);
        progressBar = findViewById(R.id.edit_password_progress);
        updatePasswordButtonOnClick();
    }

    private void updatePasswordButtonOnClick(){
        updatePasswordButton.setOnClickListener(view -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            if (TextUtils.isEmpty(oldPassword) && TextUtils.isEmpty(newPassword)) return;
            progressBar.setVisibility(View.VISIBLE);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, oldPassword
                    );
            firebaseUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) return;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(task1 -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(this, "Successfully update password", Toast.LENGTH_LONG).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(this, "Failed update password", Toast.LENGTH_LONG).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Incorrect credential
                        Toast.makeText(this, "Previous password is wrong", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    });

        });
    }
}