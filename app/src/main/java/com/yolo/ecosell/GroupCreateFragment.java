package com.yolo.ecosell;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupCreateFragment extends Fragment{

    public GroupCreateFragment(){
        // Required empty public constructor
    }

    public static GroupCreateFragment newInstance(String param1, String param2) {
        GroupCreateFragment fragment = new GroupCreateFragment();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create a new group");
        View view = inflater.inflate(R.layout.fragment_groupcreate, container, false);
        return view;
    }

}
