package com.duong.pushnotification.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.duong.pushnotification.Fragment.MainActivity;
import com.duong.pushnotification.LoginActivity;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE=0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String MSSV = "MSSV";
    private static final String TEN = "TEN";
    private static final String KHOA = "KHOA";
    private static final String LOP = "LOP";
    private static final String EMAIL = "EMAIL";
    private static final String TONGTC = "TONGTINCHI";
    private static final String TICHLUY = "TICHLUY";
    private static final String LINKAVATAR = "LINKAVATAR";


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(SinhVien sv) {
        editor.putBoolean(LOGIN, true);
        editor.putString(MSSV, sv.getMaSinhVien());
        editor.putString(TEN, sv.getTen());
        editor.putString(KHOA, sv.getKhoa());
        editor.putString(LOP, sv.getLop());
        editor.putString(EMAIL, sv.getEmail());
        editor.putString(TONGTC, sv.getTC());
        editor.putString(TICHLUY, sv.getT4());
        editor.putString(LINKAVATAR, sv.getLinkAvatar());
        editor.apply();
    }


    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    public SinhVien getSinhVien() {
        SinhVien sv = new SinhVien();
        sv.setMaSinhVien(sharedPreferences.getString(MSSV, null));
        sv.setTen(sharedPreferences.getString(TEN, null));
        sv.setKhoa(sharedPreferences.getString(KHOA, null));
        sv.setLop(sharedPreferences.getString(LOP, null));
        sv.setEmail(sharedPreferences.getString(EMAIL, null));
        sv.setTC(sharedPreferences.getString(TONGTC, null));
        sv.setT4(sharedPreferences.getString(TICHLUY, null));
        sv.setLinkAvatar(sharedPreferences.getString(LINKAVATAR, null));
        return sv;
    }


    public String getMSSV() {
        return sharedPreferences.getString(MSSV, null);
    }

    public String getLinkAvatar() {
        return sharedPreferences.getString(LINKAVATAR, null);
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
