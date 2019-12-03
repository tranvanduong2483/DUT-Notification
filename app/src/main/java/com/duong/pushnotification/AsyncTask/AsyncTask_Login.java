package com.duong.pushnotification.AsyncTask;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.duong.pushnotification.MainActivity;
import com.duong.pushnotification.MyFirebaseMessagingService;
import com.duong.pushnotification.R;
import com.duong.pushnotification.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public  class AsyncTask_Login extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private  Activity activity;
    @SuppressLint("StaticFieldLeak")
    private  Button btn_login;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar mPg_login;
    private OkHttpClient okHttpClient;
    private String MSSV, MK, Token;

    public AsyncTask_Login(Activity activity)
    {
        this.activity=activity;
        EditText mEdt_MaSV = activity.findViewById(R.id.edt_masv);
        EditText mEdt_MatKhau = activity.findViewById(R.id.edt_matkhau);
        btn_login = activity.findViewById(R.id.btn_dangnhap);
        mPg_login = activity.findViewById(R.id.pg_login);
        MSSV = mEdt_MaSV.getText() +"";
        MK = mEdt_MatKhau.getText() +"";
        Token = MyFirebaseMessagingService.getToken(activity);
    }

    protected void onPreExecute() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        mPg_login.setVisibility(ProgressBar.VISIBLE);
        btn_login.setVisibility(Button.GONE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url=strings[0]+ "/login.php?MaSinhVien=" + MSSV + "&MatKhau=" + MK + "&Token=" + Token;
            Request request = (new Request.Builder().url(url)).build();

            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject json = new JSONObject(s);

            SessionManager sessionManager = new SessionManager(activity);
            sessionManager.createSession(
                    (String) json.get("id"),
                    (String) json.get("name"),
                    (String) json.get("faculty"),
                    (String) json.get("classs"),
                    (String) json.get("email"),
                    (String) json.get("TC"),
                    (String) json.get("T4")
            );

            String ten = (String) json.get("name");

            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);

        } catch (Exception e) {

            Snackbar.make(mPg_login, s +"", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            //Toast.makeText(activity, s + "",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }finally {
            mPg_login.setVisibility(ProgressBar.GONE);
            btn_login.setVisibility(Button.VISIBLE);
        }
        super.onPostExecute(s);
    }
}