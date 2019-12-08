package com.duong.pushnotification.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.duong.pushnotification.R;
import com.duong.pushnotification.classes.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView mBottomNav;
    SessionManager mSessionManager;
    ActionBar actionBar;
    PersonalFragment personalFragment = new PersonalFragment();
    UniversityFragment universityFragment = new UniversityFragment();
    TeacherFragment teacherFragment = new TeacherFragment();
    ScheduleFragment scheduleFragment = new ScheduleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        setEvent();
        mSessionManager.checkLogin();
        mBottomNav.setSelectedItemId(R.id.nav_person);
    }

    private void Init() {
        actionBar = getSupportActionBar();
        mSessionManager = new SessionManager(this);
        mBottomNav = findViewById(R.id.botom_navigation);
    }

    private void setEvent() {
        mBottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment;

        switch (menuItem.getItemId()) {
            case R.id.nav_person:
                actionBar.setTitle(PersonalFragment.title);
                selectedFragment = personalFragment;
                break;
            case R.id.nav_university:
                actionBar.setTitle(UniversityFragment.title);
                selectedFragment = universityFragment;
                break;
            case R.id.nav_teacher:
                actionBar.setTitle(TeacherFragment.title);
                selectedFragment = teacherFragment;
                break;
            case R.id.nav_schedule:
                actionBar.setTitle(ScheduleFragment.title);
                selectedFragment = scheduleFragment;
                break;
            default:
                return false;
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
        return true;
    }
}





