package com.example.weatherapp.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.weatherapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends FragmentActivity {

    private static final String TAG = "MainFragment";
    private Fragment detFragment = new DetailFragment();
    private Fragment listFragment = new ListFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.main_fragment_layout);
        setBottomNavBar();
        prepareView();
    }

    private void prepareView(){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.list_fragment_container, listFragment)
                    //.addToBackStack(null)
                    .commit();
        } else {
            // In portrait
            setFragment(detFragment);
        }
    }

    private void setBottomNavBar(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home: {
                        fragment = detFragment;
                        break;
                    }
                    case R.id.navigation_cities: {
                        fragment = listFragment;
                        break;
                    }
                }
                setFragment( fragment);

                return true;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        if(fragment == null )return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
