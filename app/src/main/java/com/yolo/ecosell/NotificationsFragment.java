package com.yolo.ecosell;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Chat;
import model.Notification;

public class NotificationsFragment extends Fragment {
    private final String TAG = "NotificationsFragment";
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationRecyclerViewAdapter;
    private List<Notification> mNotificationList = new ArrayList<>();

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notificationRoomsCollectionReference = db.collection("Notifications");

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
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG, firebaseAuth.getCurrentUser().getUid());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notifications");
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);


        notificationRecyclerView = view.findViewById(R.id.notification_recycler_view);
        notificationRecyclerView.setHasFixedSize(true);
        getNotifications();
        return view;
    }

    private void getNotifications(){
        //mNotificationList = new ArrayList<>();
        notificationRoomsCollectionReference
                .whereEqualTo("to", firebaseAuth.getCurrentUser().getUid())
                .orderBy("sentTime", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    Log.d(TAG, value.toString());

                    if (value != null && !value.isEmpty()) {
                        mNotificationList = new ArrayList<>();
                        for (QueryDocumentSnapshot notification : value) {
                            Notification n = notification.toObject(Notification.class);
                            mNotificationList.add(n);
                        }
                    }
                    notificationRecyclerViewAdapter = new NotificationAdapter(getContext(), mNotificationList);
                    notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
                    notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                });
    }
}