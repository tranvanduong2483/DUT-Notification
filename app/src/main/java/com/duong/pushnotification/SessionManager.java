package com.duong.pushnotification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    //int PRIVATE_MODE=0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    static final String MSSV = "MSSV";
    static final String TEN = "TEN";
    static final String KHOA = "KHOA";
    static final String LOP = "LOP";

    static final String EMAIL = "EMAIL";
    static final String TONGTC = "TONGTINCHI";
    static final String TICHLUY = "TICHLUY";


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        int PRIVATE_MODE=0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String Mssv, String Ten, String Khoa, String Lop, String Email, String TongTC, String TichLuy) {
        editor.putBoolean(LOGIN, true);
        editor.putString(MSSV, Mssv);
        editor.putString(TEN, Ten);
        editor.putString(KHOA, Khoa);
        editor.putString(LOP, Lop);
        editor.putString(EMAIL, Email);
        editor.putString(TONGTC, TongTC);
        editor.putString(TICHLUY, TichLuy);
        editor.apply();
    }


    private boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    void checkLogin() {
        if (!this.isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(MSSV, sharedPreferences.getString(MSSV, null));
        user.put(TEN, sharedPreferences.getString(TEN, null));
        user.put(KHOA, sharedPreferences.getString(KHOA, null));
        user.put(LOP, sharedPreferences.getString(LOP, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(TONGTC, sharedPreferences.getString(TONGTC, null));
        user.put(TICHLUY, sharedPreferences.getString(TICHLUY, null));
        return user;
    }

    String getMSSV(){
        return sharedPreferences.getString(MSSV,null);
    }

    public void logout() {
        String MSSV = getMSSV();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("MSSV", MSSV);
        intent.putExtras(bundle);

        context.startActivity(intent);
        ((MainActivity) context).finish();
    }

}
