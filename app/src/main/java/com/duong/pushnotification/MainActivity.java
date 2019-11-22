package com.duong.pushnotification;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNav;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        bottomNav = findViewById(R.id.botom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PersonalFragment())
                .commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId()==bottomNav.getSelectedItemId()) return false;
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_person:
                selectedFragment = new PersonalFragment();
                break;
            case R.id.nav_university:
                selectedFragment = new UniversityFragment();
                break;
            case R.id.nav_teacher:
                selectedFragment = new TeacherFragment();
                break;
        }

        if (selectedFragment!=null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }
        return true;
    }

}





