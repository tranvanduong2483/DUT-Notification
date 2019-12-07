package com.duong.pushnotification.AsyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duong.pushnotification.MyFirebaseMessagingService;
import com.duong.pushnotification.R;
import com.duong.pushnotification.SessionManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AsyncTask_Logout extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private  ProgressBar mPg_logout;
    @SuppressLint("StaticFieldLeak")
    private  Activity activity;
    @SuppressLint("StaticFieldLeak")
    private  ImageView mMb_logout;
    @SuppressLint("StaticFieldLeak")
    private  OkHttpClient okHttpClient;

    public AsyncTask_Logout(Activity activity) {
        this.activity = activity;
        mPg_logout = activity.findViewById(R.id.pg_logout);
        mMb_logout = activity.findViewById(R.id.imglogout);
    }


    protected void onPreExecute() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        mMb_logout.setVisibility(ImageView.GONE);
        mPg_logout.setVisibility(ProgressBar.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Request request = (new Request.Builder().url(strings[0])).build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject json = new JSONObject(s);
            String status_logout = json.get("statuslogout") + "";
            if (status_logout.equals("1")) {
                SessionManager sessionManager = new SessionManager(activity);
                sessionManager.logout();
            }
        } catch (Exception ignored) {
            Toast.makeText(activity, "Lỗi kết nối",Toast.LENGTH_LONG).show();
        }
        finally {
            mMb_logout.setVisibility(ImageView.VISIBLE);
            mPg_logout.setVisibility(ProgressBar.GONE);
        }
        super.onPostExecute(s);
    }
}