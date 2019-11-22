package com.duong.pushnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailNotificationActivity extends AppCompatActivity {
    TextView mTv_tieude, mTv_noidung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);

        mTv_noidung = findViewById(R.id.tv_noidung);
        mTv_tieude = findViewById(R.id.tv_tieude);

        Notification tb = (Notification) getIntent().getSerializableExtra("Notification");
        if (tb != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTv_tieude.setText(Html.fromHtml(tb.title, Html.FROM_HTML_MODE_COMPACT));
                mTv_noidung.setText(Html.fromHtml(tb.content, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mTv_tieude.setText(Html.fromHtml(tb.title));
                mTv_noidung.setText(Html.fromHtml(tb.content));
            }

            SessionManager sessionManager = new SessionManager(getApplication());
            HashMap<String, String> user = sessionManager.getUserDetail();
            String MSSV = user.get(SessionManager.MSSV);
            if (MSSV == null) return;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference mData = database.getReference();
            mData.child(MSSV).child(tb.type).child(tb.id).child("hasSeen").setValue(true);
        }


        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("Chi tiết"); //Thiết lập tiêu đề nếu muốn
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

}
