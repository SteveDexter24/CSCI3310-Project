package com.yolo.ecosell;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Product;
import model.User;
import model.UserViewModel;

import static android.app.Activity.RESULT_OK;

public class SellFragment extends Fragment {

    private final String TAG = "SellFragment";

    private LinkedList<ImageButton> imageButtons = new LinkedList<>();
    private EditText categoryEditText, listingTitleEditText, priceEditText, deliveryEditText, descriptionEditText;
    private LinkedList<Chip> chips = new LinkedList<>();
    private ChipGroup chipGroup;
    private Button listItButton;
    private int imageIndex = 0;
    private int chipSelectedId = View.NO_ID;
    private String condition;
    private LinkedList<byte[]> bytesCompressImages = new LinkedList<>();
    private List<String> imageUrls = new LinkedList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference collectionReference = db.collection("Products");
    // User Collection
    private CollectionReference userCollectionReference = db.collection("Users");

    private UserViewModel userViewModel;
    private User user;

    private Bitmap bmp = null;
    private byte[] imageCompressedData;


    public SellFragment() {
        // Required empty public constructor
    }

    public static SellFragment newInstance(String param1, String param2) {
        SellFragment fragment = new SellFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create a new listing");
        return inflater.inflate(R.layout.fragment_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this.getActivity(), users -> {
            user = users.get(0);
        });

//        for (int i = 0; i < 4; i++){
//            imageUris.add(new Byte.);
//        }

        imageButtons.add(view.findViewById(R.id.add_listing_photo_1));
        imageButtons.add(view.findViewById(R.id.add_listing_photo_2));
        imageButtons.add(view.findViewById(R.id.add_listing_photo_3));
        imageButtons.add(view.findViewById(R.id.add_listing_photo_4));

        categoryEditText = view.findViewById(R.id.add_listing_category_edit_text);
        listingTitleEditText = view.findViewById(R.id.add_listing_name_edit_text);
        priceEditText = view.findViewById(R.id.add_listing_price_edit_text);
        deliveryEditText = view.findViewById(R.id.add_listing_delivery_edit_text);
        descriptionEditText = view.findViewById(R.id.add_listing_description_editText_view);

        // For testing purposes
        categoryEditText.setText("Trip");
        listingTitleEditText.setText("HK Trip");
        priceEditText.setText("2580");
        deliveryEditText.setText("N/A");
        descriptionEditText.setText("fake description");

        chipGroup = view.findViewById(R.id.add_listing_chipGroup);

        chips.add(view.findViewById(R.id.chip_brand_new));
        chips.add(view.findViewById(R.id.chip_like_new));
        chips.add(view.findViewById(R.id.chip_well_used));
        chips.add(view.findViewById(R.id.chip_heavily_used));

        chips.get(0).setChecked(true);
        getChipValue();

        listItButton = view.findViewById(R.id.add_listing_button);

        chooseImageButtons();

        listItButton.setOnClickListener(view1 -> createNewListing());
    }

    private void chooseImageButtons(){
        for (int i = 0; i < 4; i++) {
            int num = i;
            imageButtons.get(num).setOnClickListener(view1 -> {
                getImage(num);
                Log.d(TAG, "image button at:" + num);
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chooseImageButtons();
    }

    @Override
    public void onResume() {
        super.onResume();
        chooseImageButtons();
    }

    private void getImage(int index) {
        Intent imageLibrary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageIndex = index;
        startActivityForResult(imageLibrary, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Log.d(TAG, "" + bytesCompressImages.size());
            imageButtons.get(imageIndex).setImageURI(imageUri);
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                imageCompressedData = baos.toByteArray();
                bytesCompressImages.add(imageCompressedData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Boolean validateFields(String category, String title, String description, String price, String condition, String delivery) {
        return !TextUtils.isEmpty(category) &&
                !TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(description) &&
                !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(condition) &&
                !TextUtils.isEmpty(delivery);
    }

    private void getChipValue() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == View.NO_ID) {
                // User tried to uncheck, make sure to keep the chip checked
                group.check(chipSelectedId);
            }
            for (int i = 0; i < 4; i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    condition = chip.getText().toString();
                    Log.d(TAG, condition);
                }
            }
        });
    }

    private void createNewListing() {
        String category = categoryEditText.getText().toString().trim();
        String title = listingTitleEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String delivery = deliveryEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();


        if (!validateFields(category, title, description, price, condition, delivery)) {
            Toast.makeText(getContext(), "Incomplete fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (bytesCompressImages.size() == 0) {
            Toast.makeText(getContext(), "Please add at least one image", Toast.LENGTH_LONG).show();
            return;
        }


        listItButton.setEnabled(false);
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userId = currentUser.getUid();

        Product newListing = new Product();

        for (int i = 0; i < bytesCompressImages.size(); i++) {
            // Upload Images to storage
            StorageReference filePath = storageReference
                    .child("listing_images")
                    .child(userId + "_" + new Timestamp(new Date()).toString() + "_" + i);

            int finalI = i;
            filePath.putBytes(bytesCompressImages.get(i))
                    .addOnSuccessListener(taskSnapshot -> {
                        filePath.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    imageUrls.add(uri.toString());
                                    if(finalI == bytesCompressImages.size() - 1){
                                        String productId = collectionReference.getId();
                                        newListing.setImageUrls(imageUrls);
                                        newListing.setProductSeller(userId);
                                        newListing.setProductName(title);
                                        newListing.setProductCategory(category);
                                        newListing.setProductPrice(price);
                                        newListing.setProductDeliveryMethod(delivery);
                                        newListing.setCondition(condition);
                                        newListing.setProductDescription(description);
                                        newListing.setTimeAdded(new Timestamp(new Date()).toString());
                                        newListing.setSellerImageUrl(user.getImageUrl());
                                        newListing.setSellerUserName(user.getUsername());
                                        newListing.setProductId(productId);

                                        collectionReference.add(newListing)
                                                .addOnSuccessListener(documentReference -> {
                                                    // update user collection
                                                    List<String> newProductId = new ArrayList<String>();
                                                    newProductId = user.getProducts();
                                                    newProductId.add(documentReference.getId());
                                                    user.setProducts(newProductId);
                                                    updateUserCollection(user);
                                                    listItButton.setEnabled(true);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), "Failed to create a new listing", Toast.LENGTH_LONG).show();
                                                    listItButton.setEnabled(true);
                                                });
                                    }
                                });
                    });
        }




    }

    private void updateUserCollection(User user) {
        userCollectionReference.document(user.getUserId())
                .set(user, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Successfully created a new listing", Toast.LENGTH_LONG).show();
                    // finish the fragment
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Failed to append listing to user");
                });
    }
}