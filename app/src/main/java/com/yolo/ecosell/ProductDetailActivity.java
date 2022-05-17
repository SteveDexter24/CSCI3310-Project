package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import model.Product;
import model.ProductViewModel;

public class ProductDetailActivity extends AppCompatActivity {

    private int itemAtIndex;
    private String title;
    private ProductViewModel productViewModel;
    private Product product;

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productSellerTextView,
            productConditionTextView, productCategoryTextView, productDescriptionTextView, deliveryMethodTextView;
    private Button chatButton, offerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        itemAtIndex = intent.getIntExtra("itemAtIndex", 0);
        title = intent.getStringExtra("title");

        getSupportActionBar().setTitle(title);

        // get component from ui
        productImageView = findViewById(R.id.productImage);
        productNameTextView = findViewById(R.id.productName);
        productPriceTextView = findViewById(R.id.productPrice);
        productSellerTextView = findViewById(R.id.productSeller);
        productConditionTextView = findViewById(R.id.productCondition);
        productCategoryTextView = findViewById(R.id.productCategory);
        productDescriptionTextView = findViewById(R.id.productDescription);
        deliveryMethodTextView = findViewById(R.id.productDeliveryMethod);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ProductViewModel.class);

        productViewModel.getAllProducts().observe(this, products -> {
            product = products.get(itemAtIndex);
            setDataToUI(product);
        });


    }

    private void setDataToUI(Product product){
        Glide.with(this)
                .load(product.getImageUrls().get(0)).into(productImageView);
        productNameTextView.setText(product.getProductName());
        productPriceTextView.setText("HK$: " + product.getProductPrice());
        productSellerTextView.setText(product.getSellerUserName());
        productConditionTextView.setText(product.getCondition());
        productCategoryTextView.setText(product.getProductCategory());
        productDescriptionTextView.setText(product.getProductDescription());
        deliveryMethodTextView.setText(product.getProductDeliveryMethod());
    }

    //offer button -> dialogue fragment -> create chat room with that text
}