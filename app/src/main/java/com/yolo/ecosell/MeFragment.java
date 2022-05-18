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

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private TextView usernameTextView, emailTextView, mobileTextView, locationTextView, listingCountTextView, dateJoinedTextView;
    private UserViewModel userViewModel;
    private LinearLayout listingLinearLayout, paymentLinearLayout, settingsLinearLayout;
    private String userId;
    private String username;

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

        View view  = inflater.inflate(R.layout.fragment_me, container, false);
        userProfileImage = view.findViewById(R.id.profile_imageView);
        usernameTextView = view.findViewById(R.id.me_username_text_view);
        emailTextView = view.findViewById(R.id.me_email_text_view);
        mobileTextView = view.findViewById(R.id.me_mobile_phone_number);
        locationTextView = view.findViewById(R.id.me_location);
        listingCountTextView = view.findViewById(R.id.me_listing_count);
        //dateJoinedTextView = view.findViewById(R.id.date_joined);

        listingLinearLayout = view.findViewById(R.id.me_listing_layout);
        paymentLinearLayout = view.findViewById(R.id.me_payment_layout);
        settingsLinearLayout = view.findViewById(R.id.settings_button_linear_layout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goToMyListingScreen();
        goToPaymentScreen();
        goToSettingScreen();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            User user = users.get(0);
            Glide.with(this).load(user.getImageUrl()).into(userProfileImage);
            userId = user.getUserId();
            username = user.getUsername();
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            mobileTextView.setText(user.getMobile());
            locationTextView.setText(user.getLocation());
            listingCountTextView.setText(user.getProducts().size() + " listing(s)");
        });
    }

    private void goToMyListingScreen() {
        listingLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MeFragment.this.getActivity(), MyListingsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void goToPaymentScreen() {
        paymentLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MeFragment.this.getActivity(), PaymentActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void goToSettingScreen() {
        settingsLinearLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MeFragment.this.getActivity(), SettingsActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

}

