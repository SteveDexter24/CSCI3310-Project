package com.yolo.ecosell;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yolo.ecosell.adapter.GroupAdapter;

public class GroupsFragment extends Fragment {

    private RecyclerView groupRecyclerView;
    private GroupAdapter groupRecyclerViewAdapter;

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

        groupRecyclerView = view.findViewById(R.id.group_recyclerview);
        groupRecyclerView.setHasFixedSize(true);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Setup adapter
        groupRecyclerViewAdapter = new GroupAdapter(view.getContext());
        groupRecyclerView.setAdapter(groupRecyclerViewAdapter);
        groupRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));

        return view;
    }
}