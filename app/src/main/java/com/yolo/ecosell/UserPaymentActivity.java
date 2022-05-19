package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserPaymentActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView userQrTextView;
    private String username, qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        qrCodeImageView = findViewById(R.id.user_qr_photo);
        userQrTextView = findViewById(R.id.user_QRTitle);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        qrCode = intent.getStringExtra("qrCode");

        Glide.with(this).load(qrCode).into(qrCodeImageView);
        userQrTextView.setText(username + "'s QR Code");
        getSupportActionBar().setTitle(username);

    }
}