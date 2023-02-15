package com.example.barberbooking;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.barberbooking.databinding.ActivityNavBinding;

public class NavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calling the superclass's `onCreate` method to ensure proper setup of the Activity

        setContentView(R.layout.activity_nav); // Setting the layout of the Activity to the one specified by the `R.layout.activity_nav` resource

        BottomNavigationView navView = findViewById(R.id.nav_view); // Finding the `BottomNavigationView` in the layout by its ID, and storing a reference to it in a variable

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_meetings, R.id.navigation_dashboard, R.id.navigation_notifications) // Specifying the IDs of the top level navigation destinations
                .build(); // Building the `AppBarConfiguration` object

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_nev); // Finding the `NavController` that is responsible for managing the navigation between destinations in the app

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); // Setting up the action bar with the `NavController` and `AppBarConfiguration`

        NavigationUI.setupWithNavController(navView, navController); // Setting up the `BottomNavigationView` with the `NavController`

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() { // Adding a listener to the `NavController` that gets notified whenever the destination changes
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.loginFragment || navDestination.getId() == R.id.registerFragment) // Checking if the destination is either the `loginFragment` or the `registerFragment`
                    navView.setVisibility(View.INVISIBLE); // If it is, making the `BottomNavigationView` invisible
                else
                    navView.setVisibility(View.VISIBLE); // If it's not, making the `BottomNavigationView` visible
            }
        });
    }
}