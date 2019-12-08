package com.duong.pushnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.duong.pushnotification.classes.ThongBao;
import com.duong.pushnotification.classes.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailNotificationActivity extends AppCompatActivity {
    private TextView mTv_tieude, mTv_noidung;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mData = database.getReference();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);

        Init();

        ThongBao tb = (ThongBao) getIntent().getSerializableExtra("ThongBao");
        if (tb != null) {
            HienThiThongBao(tb);
            LuuTrangThaiDaXem(tb);
        }
    }

    private void Init() {
        mTv_noidung = findViewById(R.id.tv_noidung);
        mTv_tieude = findViewById(R.id.tv_tieude);
        sessionManager = new SessionManager(getApplication());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void HienThiThongBao(ThongBao tb){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTv_tieude.setText(Html.fromHtml(tb.getTieuDe(), Html.FROM_HTML_MODE_COMPACT));
            mTv_noidung.setText(Html.fromHtml(tb.getNoiDung(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTv_tieude.setText(Html.fromHtml(tb.getTieuDe()));
            mTv_noidung.setText(Html.fromHtml(tb.getNoiDung()));
        }
    }

    private void LuuTrangThaiDaXem(ThongBao tb) {
        String MSSV = sessionManager.getMSSV();
        mData.child(MSSV).child(tb.getLoai()).child(tb.getID()).child("DaXem").setValue(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
