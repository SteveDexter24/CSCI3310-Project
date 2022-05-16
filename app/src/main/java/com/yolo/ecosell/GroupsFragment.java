package com.yolo.ecosell;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private SearchView searchView;
    private String searchQuery = "";

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference groupsCollectionReference = db.collection("Groups");

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Groups");
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        // Action button
        createGroupButton = view.findViewById(R.id.floatingActionButton2);
        // Search View
        searchView = (SearchView) view.findViewById(R.id.group_searchView);

        groupRecyclerView = view.findViewById(R.id.group_recyclerview);
        groupRecyclerView.setHasFixedSize(true);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Button onClick
        createGroupButton.setOnClickListener(view1 -> {
            goToCreateGroupScreen();
        });
        // get query from search bar
        getQuery();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllGroups(searchQuery);
    }

    private void getQuery() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "submit: " + s);
                searchQuery = s;
                getAllGroups(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, s);
                return true;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);

            }
        });
    }


    private void goToCreateGroupScreen() {
        Log.d(TAG, "create group pressed");
        startActivity(new Intent(getActivity(), CreateGroupActivity.class));
    }

    private void getAllGroups(String query) {
        Task<QuerySnapshot> searchDocRef;
        if (query.isEmpty()) {
            searchDocRef = groupsCollectionReference.get();
        } else {
            searchDocRef = groupsCollectionReference.whereEqualTo("groupName", query).get();
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