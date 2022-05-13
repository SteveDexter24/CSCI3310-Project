package com.yolo.ecosell;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Product;

public class ExploreFragment extends Fragment {
    private RecyclerView recyclerView;
    private ExploreAdapter exploreAdapter;
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
            exploreAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}