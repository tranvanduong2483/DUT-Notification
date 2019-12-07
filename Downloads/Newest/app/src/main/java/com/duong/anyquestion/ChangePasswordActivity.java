package com.duong.anyquestion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class ChangePasswordActivity extends AppCompatActivity {


    private Socket mSocket = ConnectThread.getInstance().getSocket();

    Button btn_change_password, btn_cancel;
    EditText edt_old_password, edt_new_password1, edt_new_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edt_new_password1 = findViewById(R.id.edt_new_password1);
        edt_new_password2 = findViewById(R.id.edt_new_password2);
        edt_old_password = findViewById(R.id.edt_old_password);

        btn_change_password = findViewById(R.id.btn_change_password);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = edt_new_password1.getText() + "";
                String pass2 = edt_new_password2.getText() + "";
                String oldpass = edt_old_password.getText() + "";

                if (pass1.isEmpty() || pass2.isEmpty() || oldpass.isEmpty()) {
                    ToastNew.showToast(ChangePasswordActivity.this, "Nhập thiếu!", Toast.LENGTH_LONG);
                    return;
                }

                if (!pass1.equals(pass2)) {
                    ToastNew.showToast(ChangePasswordActivity.this, "Mật khẩu mới không khớp nhau!", Toast.LENGTH_LONG);
                    return;
                }

                SessionManager sessionManager = new SessionManager(ChangePasswordActivity.this);

                String type = sessionManager.getType();
                String account =sessionManager.getAccount();

                mSocket.emit("client-request-update-password", type, account, oldpass, pass1);
                btn_change_password.setEnabled(false);


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSocket.on("server-sent-status-updating-password",callback_get_change_password);

    }



    private Emitter.Listener callback_get_change_password = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        int status = (int) args[0];
                        if (status ==1 ){
                            ToastNew.showToast(ChangePasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG);
                            finish();
                        }else {
                            ToastNew.showToast(ChangePasswordActivity.this, "Không thành công", Toast.LENGTH_LONG);
                        }

                        btn_change_password.setEnabled(true);
                    } catch (Exception ignored) {

                        ToastNew.showToast(ChangePasswordActivity.this, "Lỗi", Toast.LENGTH_LONG);

                    }

                }
            });
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
