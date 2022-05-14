package com.yolo.ecosell;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yolo.ecosell.adapter.ExploreAdapter;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ExploreFragment extends Fragment {
    private RecyclerView exploreRecyclerView;
    private ListingRecyclerViewAdapter exploreRecyclerViewAdapter;
    private List<Product> productlist;
    private SearchView searchView;



    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Explore");

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        exploreRecyclerView = view.findViewById(R.id.explore_recyclerview);
        exploreRecyclerView.setHasFixedSize(true);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Setup adapter
        exploreRecyclerViewAdapter = new ListingRecyclerViewAdapter(view.getContext());

        exploreRecyclerView.setAdapter(exploreRecyclerViewAdapter);

        exploreRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        searchView = getView().findViewById(R.id.explore_search);
//        searchView.clearFocus();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterText(newText);
//                return true;
//            }
//        });
//
//        recyclerView = getView().findViewById(R.id.explore_recyclerview);


    }

    private void filterText(String text) {
        List<Product> filteredList = new ArrayList<>();
        for(Product product: productlist){
            if(product.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(product);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(getContext(),"No Product Found",Toast.LENGTH_SHORT).show();
        }else{
//            ExploreAdapter.setFilteredList(filteredList);
            Toast.makeText(getContext(),"Here is a filtered list:)", Toast.LENGTH_SHORT).show();
        }
    }


}