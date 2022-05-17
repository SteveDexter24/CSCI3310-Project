package com.yolo.ecosell;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.ExploreAdapter;
import com.yolo.ecosell.adapter.GroupAdapter;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Group;
import model.GroupViewModel;
import model.Product;
import model.ProductViewModel;

public class ExploreFragment extends Fragment {

    private final String TAG = "ExploreFragment";

    private RecyclerView exploreRecyclerView;
    private ListingRecyclerViewAdapter exploreRecyclerViewAdapter;
    private List<Product> productList;
    private SearchView searchView;
    private ProductViewModel productViewModel;

    // FireStore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Product Collection
    private CollectionReference productsCollectionReference = db.collection("Products");

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
       // productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        exploreRecyclerView = view.findViewById(R.id.explore_recyclerview);
        exploreRecyclerView.setHasFixedSize(true);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        getAllProducts("");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    private void filterText(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Product Found", Toast.LENGTH_SHORT).show();
        } else {
//            ExploreAdapter.setFilteredList(filteredList);
            Toast.makeText(getContext(), "Here is a filtered list:)", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllProducts(String query) {
        Task<QuerySnapshot> searchDocRef;
        Query docQuery = productsCollectionReference.orderBy("timeAdded", Query.Direction.DESCENDING);
        if (query.isEmpty()) {
            searchDocRef = docQuery.get();
        } else {
            searchDocRef = docQuery.whereEqualTo("groupName", query).get();
        }
        try {
            searchDocRef
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            productList = new ArrayList<>();
                            for (QueryDocumentSnapshot products : task.getResult()) {
                                Product product = products.toObject(Product.class);
                                productList.add(product);
                                productViewModel.insertProduct(product);
                            }

                            // Setup adapter
                            Log.d(TAG, productList.size() + "");
                            exploreRecyclerViewAdapter = new ListingRecyclerViewAdapter(getContext(), productList);
                            exploreRecyclerView.setAdapter(exploreRecyclerViewAdapter);
                            exploreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}