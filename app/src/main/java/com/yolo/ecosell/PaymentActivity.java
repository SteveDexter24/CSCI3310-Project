package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Add QR Code");
    }
}