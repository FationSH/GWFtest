package com.toni.gwftest.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.toni.gwftest.R;
import com.toni.gwftest.utils.NetworkChangeReceiver;

import org.buffer.adaptablebottomnavigation.view.AdaptableBottomNavigationView;
import org.buffer.adaptablebottomnavigation.view.ViewSwapper;


public class MainActivity extends AppCompatActivity {

    private static final int METERS = 0;
    private static final int MAPS = 1;
    private static final int PROFILE = 2;

    protected AdaptableBottomNavigationView bottomNavigationView;
    private ViewSwapperAdapter viewSwapperAdapter;
    protected ViewSwapper viewSwapper;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(this);
        networkChangeReceiver.registerNetworkCallback();

        // Create BottomNavBar with Swapper && Adapter ---------------------------------------------
        bottomNavigationView = (AdaptableBottomNavigationView)
                findViewById(R.id.view_bottom_navigation);
        viewSwapper = (ViewSwapper) findViewById(R.id.view_swapper);

        viewSwapperAdapter = new ViewSwapperAdapter(getSupportFragmentManager());
        viewSwapper.setAdapter(viewSwapperAdapter);
        bottomNavigationView.setupWithViewSwapper(viewSwapper);

        final Menu menu = (Menu) bottomNavigationView.getMenu();
        // BottomNavBar Listener
        // Disable the possibility to check again the already selected fragment
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedPosition = viewSwapper.getCurrentItem();

                switch (selectedPosition) {
                    case METERS:
                        menu.getItem(1).setEnabled(true);
                        menu.getItem(2).setEnabled(true);
                        item.setEnabled(false);

                        return true;
                    case MAPS:
                        menu.getItem(0).setEnabled(true);
                        menu.getItem(2).setEnabled(true);
                        item.setEnabled(false);

                        return true;
                    case PROFILE:
                        menu.getItem(0).setEnabled(true);
                        menu.getItem(1).setEnabled(true);
                        item.setEnabled(false);

                        return true;
                }
                return false;
            }
        } );

    }
}