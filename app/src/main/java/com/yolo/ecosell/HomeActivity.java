package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import model.UserViewModel;

public class HomeActivity extends AppCompatActivity {

//    ListView listView;
//    String[] items = {"Outers", "Tops", "Dresses","Skirts","Shorts", "Pants","Shoes","Bags","Accessories"};
//    ArrayAdapter<String> arrayAdapter;

    private UserViewModel userViewModel;

    BottomNavigationView bottomNavigationView;
    MeFragment meFragment = new MeFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    DashboardFragment dashboardFragment = new DashboardFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(HomeActivity.this.getApplication())
                .create(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            try{
                Log.d("HomeActivity", "onCreate: " + users.get(0).getUsername());
            }catch (Exception e){
                Log.d("HomeActivity", e.toString());
            }

            bottomNavigationView = findViewById(R.id.bottom_nav);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_groups:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, groupsFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, dashboardFragment).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, notificationsFragment).commit();
                        return true;
                    case R.id.navigation_me:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, meFragment).commit();
                        return true;
                    default:
                        return false;
                }
            });
        });
        //search bar
//                listView = findViewById(R.id.listview);
//                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//                listView.setAdapter(arrayAdapter);

//        recycleview for explore
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_explore, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    //search bar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.explore_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                arrayAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
}


