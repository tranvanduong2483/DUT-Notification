package com.duong.anyquestion.ui_user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.duong.anyquestion.R;
import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Expert;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.duong.anyquestion.classes.User;
import com.duong.anyquestion.ui_user.AccountFragment;
import com.duong.anyquestion.ui_user.HistoryFragment;
import com.duong.anyquestion.ui_user.SearchExpertFragment;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

public class UserMainActivity extends AppCompatActivity {


    SessionManager sessionManager;
    private Socket mSocket = ConnectThread.getInstance().getSocket();

    final Fragment fragment1 = new AccountFragment();
    final Fragment fragment2 = new SearchExpertFragment();
    final Fragment fragment3 = new HistoryFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        mSocket.emit("people-online", sessionManager.getAccount() + "-" + sessionManager.getType());

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mSocket.on("server-request-logout-because-same-login", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      ToastNew.showToast(getApplication(), "Bị đăng xuất do người khác đăng nhập!", Toast.LENGTH_LONG);
                                      mSocket.emit("logout", "user");
                                  }
                              }
                );

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;

                    return true;

                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    if (mSocket.connected())
                        mSocket.emit("client-get-field", "client-get-field *****");
                    return true;

                case R.id.navigation_notifications:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;

                    if (mSocket.connected())
                        mSocket.emit("get-list-history");
                    return true;
            }
            return false;
        }
    };
}