package com.duong.pushnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duong.pushnotification.Fragment.MainActivity;
import com.duong.pushnotification.classes.ConnectThread;
import com.duong.pushnotification.classes.SessionManager;
import com.duong.pushnotification.classes.SinhVien;
import com.duong.pushnotification.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private EditText mEdt_MaSV, mEdt_MatKhau;
    private Button mBtn_DangNhap;
    private ProgressBar mPg_login;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();
        SuKien();
    }

    private void Init() {
        sessionManager = new SessionManager(this);
        mEdt_MaSV = findViewById(R.id.edt_masv);
        mEdt_MatKhau = findViewById(R.id.edt_matkhau);
        mBtn_DangNhap = findViewById(R.id.btn_dangnhap);
        mPg_login = findViewById(R.id.pg_login);
    }

    private void SuKien() {
        mBtn_DangNhap.setOnClickListener(callback_login);

        mSocket.on("disconnect", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBtn_DangNhap.setVisibility(View.VISIBLE);
                                mPg_login.setVisibility(View.GONE);
                            }


                        });
                    }
                }
        );
    }


    View.OnClickListener callback_login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String MaSinhVien = mEdt_MaSV.getText().toString();
            String MatKhau = mEdt_MatKhau.getText().toString();
            String Token = MyFirebaseMessagingService.getToken(getApplication());

            if (!mSocket.connected()) {
                ToastNew.showToast(getApplication(), "Không có kết nối!", Toast.LENGTH_LONG);
                return;
            }

            if (MaSinhVien.isEmpty() || MatKhau.isEmpty()) {
                Toast.makeText(getApplication(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                return;
            }

            mBtn_DangNhap.setVisibility(View.GONE);
            mPg_login.setVisibility(View.VISIBLE);

            mSocket.emit("user-request-login", MaSinhVien, MatKhau, Token);
            mSocket.once("server-sent-login-reponse", new Emitter.Listener() {
                        @Override
                        public void call(final Object... args) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String reponse = args[0] + "";
                                        Gson gson = new Gson();
                                        SinhVien SV = gson.fromJson(reponse + "", SinhVien.class);
                                        sessionManager.createSession(SV);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {
                                        ToastNew.showToast(LoginActivity.this, "Lỗi: " + args[0], Toast.LENGTH_LONG);
                                    }

                                    mBtn_DangNhap.setVisibility(View.VISIBLE);
                                    mPg_login.setVisibility(View.GONE);
                                }


                            });
                        }
                    }
            );

        }
    };

}