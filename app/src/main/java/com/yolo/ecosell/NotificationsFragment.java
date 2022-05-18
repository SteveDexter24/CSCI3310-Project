package com.yolo.ecosell;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yolo.ecosell.adapter.NotificationAdapter;

public class NotificationsFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationRecyclerViewAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);


        notificationRecyclerView = view.findViewById(R.id.notification_recycler_view);
        notificationRecyclerViewAdapter = new NotificationAdapter(view.getContext());

        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}