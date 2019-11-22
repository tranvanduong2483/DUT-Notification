package com.duong.pushnotification;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.duong.pushnotification.AsyncTask.AsyncTask_Login;

import static com.duong.pushnotification.R.color.common_google_signin_btn_text_dark_disabled;

public class LoginActivity extends AppCompatActivity {

    EditText mEdt_MaSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdt_MaSV=findViewById(R.id.edt_masv);
        Bundle bundle =getIntent().getExtras();
        if (bundle != null){
            String MSSV = bundle.getString("MSSV", "");
            mEdt_MaSV.setText(MSSV);
        }

        getSupportActionBar().hide();

    }

    public void click_Login(View view) {

        EditText tv_maSV = findViewById(R.id.edt_masv);
        EditText tv_mk = findViewById(R.id.edt_matkhau);
        String MaSV = tv_maSV.getText() + "";
        String MK= tv_mk.getText() +"";

        if (MaSV.isEmpty() || MK.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        final String url = getResources().getString(R.string.URL);
        new AsyncTask_Login(this).execute(url);
    }

}