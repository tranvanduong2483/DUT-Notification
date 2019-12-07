package com.duong.anyquestion.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.duong.anyquestion.ui_expert.ExpertMainActivity;
import com.duong.anyquestion.LoginActivity;
import com.duong.anyquestion.ui_user.UserMainActivity;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    //int PRIVATE_MODE=0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String ACCOUNT = "Account";
    public static final String EDUCATION = "Education";
    public static final String FIELD = "Field";
    public static final String FULLNAME = "Fullname";
    public static final String ADDRESS = "Address";
    public static final String EMAIL = "Email";
    public static final String MONEY = "Money";
    public static final String AVATAR = "Avatar";
    public static final String TYPE = "Type";


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Activity activity) {
        this.activity = activity;
        int PRIVATE_MODE = 0;
        sharedPreferences = activity.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(User user) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ACCOUNT, user.getAccount());
        editor.putString(FULLNAME, user.getFullName());
        editor.putString(ADDRESS, user.getAddress());
        editor.putString(EMAIL, user.getEmail());
        editor.putInt(MONEY, user.getMoney() );
        editor.putString(AVATAR, user.getAvatar());
        editor.putString(TYPE, "user");
        editor.apply();
    }



    public void createSession(Expert expert) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ACCOUNT, expert.getExpert_id());
        editor.putInt(EDUCATION, expert.getEducation_id());
        editor.putInt(FIELD, expert.getField_id());

        editor.putString(FULLNAME, expert.getFullName());
        editor.putString(ADDRESS, expert.getAddress());
        editor.putString(EMAIL, expert.getEmail());
        editor.putInt(MONEY, expert.getMoney() );
        editor.putString(AVATAR, expert.getAvatar());
        editor.putString(TYPE, "expert");
        editor.apply();
    }


    private boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }


    public void DieuHuong() {
        checkLogin();

        if ("user".equals(this.getType())) {
            Intent intent = new Intent(activity, UserMainActivity.class);
            activity.startActivity(intent);
            activity.finish();
            return;
        }

        if ("expert".equals(this.getType())) {
            Intent intent = new Intent(activity, ExpertMainActivity.class);
            activity.startActivity(intent);
            activity.finish();
            return;
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ACCOUNT, sharedPreferences.getString(ACCOUNT, null));
        user.put(FULLNAME, sharedPreferences.getString(FULLNAME, null));
        user.put(ADDRESS, sharedPreferences.getString(ADDRESS, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(MONEY, sharedPreferences.getInt(MONEY, 0) +"");
        user.put(AVATAR, sharedPreferences.getString(AVATAR, null));
        return user;
    }


    public User getUser() {
        User user = new User();
        user.setAccount(sharedPreferences.getString(ACCOUNT, null));
        user.setFullName( sharedPreferences.getString(FULLNAME, null));
        user.setAddress( sharedPreferences.getString(ADDRESS, null));
        user.setEmail( sharedPreferences.getString(EMAIL, null));
        user.setMoney(sharedPreferences.getInt(MONEY, 0) );

        String avatar = sharedPreferences.getString(AVATAR, null);
        if (avatar!=null)
            user.setAvatar(avatar);
        return user;
    }

    public Expert getExpert() {
        Expert user = new Expert();
        user.setExpert_id(sharedPreferences.getString(ACCOUNT, null));
        user.setFullName( sharedPreferences.getString(FULLNAME, null));
        user.setEducation_id(sharedPreferences.getInt(EDUCATION,-1));
        user.setField_id(sharedPreferences.getInt(FIELD, -1));
        user.setAddress( sharedPreferences.getString(ADDRESS, null));
        user.setEmail( sharedPreferences.getString(EMAIL, null));
        user.setMoney(sharedPreferences.getInt(MONEY, 0) );

        String avatar = sharedPreferences.getString(AVATAR, null);
        if (avatar!=null)
            user.setAvatar(avatar);
        return user;
    }



    public HashMap<String, String> getExpertDetail() {
        HashMap<String, String> expert = new HashMap<>();
        expert.put(ACCOUNT, sharedPreferences.getString(ACCOUNT, null));
        expert.put(EDUCATION, sharedPreferences.getString(EDUCATION, null));
        expert.put(FIELD, sharedPreferences.getString(FIELD, null));
        expert.put(FULLNAME, sharedPreferences.getString(FULLNAME, null));
        expert.put(ADDRESS, sharedPreferences.getString(ADDRESS, null));
        expert.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        return expert;
    }

    public String getAccount() {
        return sharedPreferences.getString(ACCOUNT, null);
    }

    public String getAvatar() {
        String avatar = sharedPreferences.getString(AVATAR, null);
        return  avatar;
    }

    public String getType() {
        return sharedPreferences.getString(TYPE, null);
    }

    public boolean isUser(){
        return "user".equals(getType());
    }


    public void logout() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putString("ACCOUNT", ACCOUNT);
        intent.putExtras(bundle);

        activity.startActivity(intent);
        activity.finish();
    }

}
