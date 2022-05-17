package com.yolo.ecosell;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Group;

public class GroupsFragment extends Fragment {

    private final String TAG = "GroupsFragment";

    private RecyclerView groupRecyclerView;
    private GroupAdapter groupRecyclerViewAdapter;
    private FloatingActionButton createGroupButton;
    private List<Group> groupList;
    private androidx.appcompat.widget.SearchView searchView;
    private String searchQuery = "";
    private androidx.appcompat.widget.SearchView.OnQueryTextListener queryTextListener;

    private String mParam1;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference groupsCollectionReference = db.collection("Groups");

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance(String param1) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d(TAG, "line 77: " + getArguments().getString("param1"));
            mParam1 = getArguments().getString("param1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groups");
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        // Action button
        createGroupButton = view.findViewById(R.id.floatingActionButton2);
        // Search View
        //searchView = (androidx.appcompat.widget.SearchView) view.findViewById(R.id.group_searchView);

        groupRecyclerView = view.findViewById(R.id.group_recyclerview);
        groupRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        ///linearLayoutManager.
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Button onClick
        createGroupButton.setOnClickListener(view1 -> {
            goToCreateGroupScreen();
        });
        // get query from search bar

        //getAllGroups(mParam1);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllGroups(searchQuery);
    }

    private void goToCreateGroupScreen() {
        Log.d(TAG, "create group pressed");
        startActivity(new Intent(getActivity(), CreateGroupActivity.class));
    }

    private void getAllGroups(String query) {
        Task<QuerySnapshot> searchDocRef;
        Query docQuery = groupsCollectionReference.orderBy("createdTime", Query.Direction.DESCENDING);
        if (query.isEmpty()) {
            searchDocRef = docQuery.get();
        } else {
            searchDocRef = docQuery.whereEqualTo("groupName", query).get();
        }
        try {
            searchDocRef
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            groupList = new ArrayList<>();
                            for (QueryDocumentSnapshot groups : task.getResult()) {
                                Group group = groups.toObject(Group.class);
                                groupList.add(group);
                            }
                            // Setup adapter
                            Log.d(TAG, "group size" + groupList.size());
                            groupRecyclerViewAdapter = new GroupAdapter(getContext(), groupList);
                            groupRecyclerView.setAdapter(groupRecyclerViewAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}