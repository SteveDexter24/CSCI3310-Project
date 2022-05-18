package com.yolo.ecosell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicates;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yolo.ecosell.adapter.GroupAdapter;
import com.yolo.ecosell.adapter.ListingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import model.Group;
import model.GroupViewModel;
import model.Product;
import model.ProductViewModel;

public class SearchActivity extends AppCompatActivity {

    private final String TAG = "SearchActivity";
    private SearchView searchView;
    private String searchQuery = "";
    private String searchType = "none";
    private RecyclerView searchRecyclerView;
    private GroupAdapter filteredGroupAdapter;
    private List<Group> groupList;
    private List<Product> productsList;
    private ListingRecyclerViewAdapter filteredProductRecyclerViewAdapter;

    private GroupViewModel groupViewModel;
    private ProductViewModel productViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        Intent intent = getIntent();
        searchType = intent.getStringExtra("searchQueryType");
        Log.d(TAG, searchType);

        // Set up the search recycler view
        searchRecyclerView = findViewById(R.id.search_recyclerView);
        searchRecyclerView.setHasFixedSize(true);

        groupViewModel = new ViewModelProvider.AndroidViewModelFactory(SearchActivity.this.getApplication())
                .create(GroupViewModel.class);

        groupViewModel.getAllGroups().observe(this, groups -> {
            groupList = groups;
        });

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(SearchActivity.this.getApplication())
                .create(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            productsList = products;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupViewModel.getAllGroups().observe(this, groups -> {
            groupList = groups;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu_appbar_button);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Final " + query);
                Log.d(TAG, searchType);
                searchQuery = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, newText);
                List<Group> gList = new ArrayList<>();
                List<Product> pList = new ArrayList<>();
                String q = newText.toLowerCase();


                if (searchType.equals("groups") && !TextUtils.isEmpty(newText)) {
                    for (Group c : groupList) {
                        if (c.getGroupName().toLowerCase().contains(q) || c.getGroupDescription().toLowerCase().contains(q)) {
                            gList.add(c);
                        }
                    }
                    searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    filteredGroupAdapter = new GroupAdapter(SearchActivity.this, gList);
                    searchRecyclerView.setAdapter(filteredGroupAdapter);
                } else if (searchType.equals("explore") && !TextUtils.isEmpty(newText)) {
                    for (Product p : productsList) {
                        if (p.getSellerUserName().toLowerCase().contains(q)
                                || p.getProductName().toLowerCase().contains(q)
                                || p.getCondition().toLowerCase().contains(q)) {
                            pList.add(p);
                        }
                    }
                    searchRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    filteredProductRecyclerViewAdapter = new ListingRecyclerViewAdapter(SearchActivity.this, pList);
                    searchRecyclerView.setAdapter(filteredProductRecyclerViewAdapter);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}