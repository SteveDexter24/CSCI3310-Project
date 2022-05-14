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

    private UserViewModel userViewModel;
    BottomNavigationView bottomNavigationView;
    ExploreFragment exploreFragment = new ExploreFragment();
    MeFragment meFragment = new MeFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    SellFragment sellFragment = new SellFragment();
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
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, exploreFragment).commit();
                        return true;
                    case R.id.navigation_groups:
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, groupsFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, sellFragment).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, notificationsFragment).commit();
                        return true;
                    case R.id.navigation_me:
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, meFragment).commit();
                        return true;
                    default:
                        return false;
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}


