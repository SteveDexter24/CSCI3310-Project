package com.yolo.ecosell;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import model.User;
import model.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {
    final private String TAG = "MeFragment";
    private ImageView userProfileImage;
    private TextView usernameTextView, emailTextView, mobileTextView, locationTextView;
    private UserViewModel userViewModel;
    private LinearLayout listingLinearLayout, paymentLinearLayout, settingsLinearLayout;
    private Button editProfileButton, editPasswordButton;

    public MeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfileImage = view.findViewById(R.id.profile_imageView);
        usernameTextView = view.findViewById(R.id.me_username_text_view);
        emailTextView = view.findViewById(R.id.me_email_text_view);
        mobileTextView = view.findViewById(R.id.me_mobile_phone_number);
        locationTextView = view.findViewById(R.id.me_location);

        listingLinearLayout = view.findViewById(R.id.me_listing_layout);
        paymentLinearLayout = view.findViewById(R.id.me_payment_layout);
        settingsLinearLayout = view.findViewById(R.id.settings_button_linear_layout);

        editProfileButton = view.findViewById(R.id.goto_edit_profile);
        editPasswordButton = view.findViewById(R.id.goto_edit_password);

        goToMyListingScreen();
        goToPaymentScreen();
        goToSettingScreen();
        goToEditProfileScreen();
        goToEditPasswordScreen();

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(MeFragment.this.getActivity().getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            User user = users.get(0);
            Glide.with(this).load(user.getImageUrl()).into(userProfileImage);
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            mobileTextView.setText(user.getMobile());
            locationTextView.setText(user.getLocation());
        });
    }

    private void goToMyListingScreen() {
        listingLinearLayout.setOnClickListener(view -> {
            startActivity(new Intent(MeFragment.this.getActivity(), MyListingsActivity.class));
        });
    }

    private void goToPaymentScreen() {
        paymentLinearLayout.setOnClickListener(view -> {
            Log.d(TAG, "paymentClicked");
        });
    }

    private void goToSettingScreen() {
        settingsLinearLayout.setOnClickListener(view -> {
            Log.d(TAG, "settingsClicked");
        });
    }

    private void goToEditProfileScreen() {
        editProfileButton.setOnClickListener(view -> {
            startActivity(new Intent(MeFragment.this.getActivity(), EditProfileActivity.class));
        });
    }

    private void goToEditPasswordScreen(){
        editPasswordButton.setOnClickListener(view -> {
            startActivity(new Intent(MeFragment.this.getActivity(), EditPasswordActivity.class));
        });
    }
}

