package com.example.techniefind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_customer);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout1,
                new CustomerHomePage()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()){
                case R.id.customer_home_bottom:
                    selected = new CustomerHomePage();
                    break;

                case R.id.customer_search_bottom:
                    selected = new SearchTechnician();
                    break;

                case R.id.customer_profile_bottom:
                    selected = new CustomerProfile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout1,
                    selected).commit();
            return true;
        }
    };

}