package com.yolo.ecosell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import model.UserViewModel;

public class HomeActivity extends AppCompatActivity {

    private final String TAG = "HomeActivity";

    private UserViewModel userViewModel;
    private SearchView searchView;
    BottomNavigationView bottomNavigationView;
    ExploreFragment exploreFragment = new ExploreFragment();
    MeFragment meFragment = new MeFragment();
    GroupsFragment groupsFragment = new GroupsFragment();
    SellFragment sellFragment = new SellFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();

    FragmentTransaction transaction;

    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userViewModel = new ViewModelProvider.AndroidViewModelFactory(HomeActivity.this.getApplication())
                .create(UserViewModel.class);

        // Default Fragment: Explore Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, exploreFragment).commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, exploreFragment, "ExploreFragment").commit();
                    return true;
                case R.id.navigation_groups:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, groupsFragment, "GroupsFragment").commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, sellFragment, "SellFragment").commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, notificationsFragment, "NotificationsFragment").commit();
                    return true;
                case R.id.navigation_me:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, meFragment, "MeFragment").commit();
                    return true;
                default:
                    return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        //       MenuItem menuItem = menu.findItem(R.id.search_appbar_button);
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.clas;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.likes_appbar_button:
                goToLikesActivity();
                return true;
            case R.id.chat_appbar_button:
                goToChatActivity();
                return true;
            case R.id.search_appbar_button:
                goToSearchActivity();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSearchActivity() {
        GroupsFragment groupsFragment = (GroupsFragment) getSupportFragmentManager().findFragmentByTag("GroupsFragment");
//        ExploreFragment exploreFragment = (ExploreFragment) getSupportFragmentManager().findFragmentByTag("ExploreFragment");
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        if (groupsFragment != null && groupsFragment.isVisible()) {
            intent.putExtra("searchQueryType", "groups");
        }else{
            intent.putExtra("searchQueryType", "explore");
        }

        startActivity(intent);
    }

    private void goToLikesActivity() {
        startActivity(new Intent(HomeActivity.this, LikesActivity.class));
    }

    private void goToChatActivity() {
        startActivity(new Intent(HomeActivity.this, ChatListActivity.class));
    }
//    private void searchQuery(){
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                GroupsFragment groupsFragment = (GroupsFragment) getSupportFragmentManager().findFragmentByTag("GroupsFragment");
//                if (groupsFragment != null && groupsFragment.isVisible()) {
//                    // set query string to Group Fragment
//                    Log.d(TAG, "Line 118: " + query);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_fragment, groupsFragment, "GroupsFragment").commit();
//                    Bundle args = new Bundle();
//                    String a = searchView.getQuery().toString();
//                    args.putString("param1", a);
//                    groupsFragment.setArguments(args);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchQuery = newText;
//                return false;
//            }
//        });
//    }
}


