package com.duong.anyquestion;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.SessionManager;

public class SplashActivity extends AppCompatActivity {


    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( ConnectThread.getInstance().getState() == Thread.State.NEW)
            ConnectThread.getInstance().start();

        sessionManager = new SessionManager(this);
        sessionManager.DieuHuong();
    }
}