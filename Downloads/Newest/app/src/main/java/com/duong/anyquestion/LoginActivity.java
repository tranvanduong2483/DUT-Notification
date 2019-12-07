package com.duong.anyquestion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;

import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Expert;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ThongTinDangNhap;
import com.duong.anyquestion.classes.ToastNew;
import com.duong.anyquestion.classes.User;
import com.duong.anyquestion.register.ExpertRegisterActivity;
import com.duong.anyquestion.register.UserRegisterActivity;
import com.duong.anyquestion.ui_expert.ExpertMainActivity;
import com.duong.anyquestion.ui_user.UserMainActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private Button btn_login;
    private EditText edt_username, edt_password;
    private ProgressBar pb_loading_login;
    private TextView tv_forget_password, tv_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnhXa();
        ButtonEvent();
        LangNgheServer();
    }

    private void ButtonEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSocket.connected()){
                    ToastNew.showToast(getApplication(), "Máy chủ ngắt kết nối!", Toast.LENGTH_LONG);
                    return;
                }

                String Username = edt_username.getText() + "";
                String Password = edt_password.getText() + "";

                if (Username.isEmpty() || Password.isEmpty()) {
                    ToastNew.showToast(getApplication(), "Thiếu thông tin", Toast.LENGTH_LONG);
                    return;
                }

                ThongTinDangNhap thongTinDangNhap = new ThongTinDangNhap(Username, Password);

                mSocket.emit("client-dang-nhap", thongTinDangNhap.toJSON());
                btn_login.setVisibility(View.GONE);
                pb_loading_login.setVisibility(View.VISIBLE);
            }
        });


        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("Câu hỏi");
                dialog.setMessage("Bạn muốn đăng ký trở thành?");
                dialog.setCancelable(true);
                dialog.setPositiveButton(
                        "Chuyên gia",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent_register_expert = new Intent(LoginActivity.this, ExpertRegisterActivity.class);
                                startActivity(intent_register_expert);
                            }
                        });

                dialog.setNegativeButton(
                        "Cá nhân",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent_register_user = new Intent(LoginActivity.this, UserRegisterActivity.class);
                                startActivity(intent_register_user);
                            }
                        });

                AlertDialog alert = dialog.create();
                alert.show();
            }
        });


        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordctivity.class);
                startActivity(intent);
            }
        });
    }

    private void LangNgheServer() {
        mSocket.on("disconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
               btn_login.setVisibility(View.VISIBLE);
               pb_loading_login.setVisibility(View.GONE);
            }
        });

        mSocket.on("ket-qua-dang-nhap", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_login.setVisibility(View.VISIBLE);
                                pb_loading_login.setVisibility(View.GONE);
                                try {
                                    JSONObject data = (JSONObject) args[0];
                                    String NoiDung = data.getString("ketqua");

                                    if (NoiDung.equals("INCORRECT")) {
                                        ToastNew.showToast(getApplication(), "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT);
                                        return;
                                    }

                                    String type = data.getString("type");
                                    String avatar = null, name;
                                    Gson gson = new Gson();
                                    Intent intent_main;

                                    if (args.length != 1) {
                                        byte[] bytes = (byte[]) args[1];
                                        if (bytes != null) {
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            avatar = ToolSupport.saveToInternalStorage(bitmap, LoginActivity.this);
                                        }
                                    }

                                    SessionManager sessionManager = new SessionManager(LoginActivity.this);

                                    if (type.equals("user")) {
                                        User user = gson.fromJson(NoiDung, User.class);
                                        user.setAvatar(avatar);
                                        name = user.getFullName();
                                        sessionManager.createSession(user);
                                        intent_main = new Intent(LoginActivity.this, UserMainActivity.class);
                                    } else {
                                        Expert expert = gson.fromJson(NoiDung, Expert.class);
                                        expert.setAvatar(avatar);
                                        name = expert.getFullName();
                                        sessionManager.createSession(expert);
                                        intent_main = new Intent(LoginActivity.this, ExpertMainActivity.class);
                                    }


                                    intent_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent_main);
                                    ToastNew.showToast(getApplication(), name + ", bạn đã đăng nhập thành công!", Toast.LENGTH_SHORT);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ToastNew.showToast(LoginActivity.this, "Xuất hiện lỗi!", Toast.LENGTH_LONG);
                                }


                            }
                        });
                    }
                }
        );
    }

    private void AnhXa() {
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        pb_loading_login = findViewById(R.id.pb_loading_login);
    }



}
