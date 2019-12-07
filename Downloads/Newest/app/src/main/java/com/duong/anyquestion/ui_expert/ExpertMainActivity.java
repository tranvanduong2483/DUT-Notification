package com.duong.anyquestion.ui_expert;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.duong.anyquestion.MessageListActivity;
import com.duong.anyquestion.R;
import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.PhanHoiYeuCauGiaiDap;
import com.duong.anyquestion.classes.Question;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import org.json.JSONObject;

public class ExpertMainActivity extends AppCompatActivity {

    private Socket mSocket = ConnectThread.getInstance().getSocket();;
    SessionManager sessionManager;

    final Fragment fragment1 = new AccountFragment();
    final Fragment fragment2 = new HelpFragment();
    final Fragment fragment3 = new RatingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    Question question_nhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_expert);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        mSocket.on("send-question-to-expert", callback_question);

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSocket.on("server-request-logout-because-same-login", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      ToastNew.showToast(getApplication(), "Bị đăng xuất do người khác đăng nhập!", Toast.LENGTH_LONG);
                                      mSocket.emit("logout", "expert");
                                  }
                              }
                );

            }
        });


        mSocket.on("server-send-ghep-doi-khong-thanh-cong", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastNew.showToast(getApplication(), "Người thắc mắc đã hủy, ghép không thành công!", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }


    private Emitter.Listener callback_thaoluan = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.off("bat dau cuoc thao luan");
                    try {
                        int conversation_id = (int) args[0];

                        EditText edt_gioithieu = fragment2.getActivity().findViewById(R.id.edt_gioithieu);
                        String gioithieu = edt_gioithieu.getText() + "";
                        Bundle bundle = new Bundle();
                        bundle.putString("tinnhangioithieu", gioithieu);
                        bundle.putString("question", question_nhan.toJSON());
                        bundle.putInt("conversation_id", conversation_id);

                        Intent intent_nhantin = new Intent(ExpertMainActivity.this, MessageListActivity.class);
                        intent_nhantin.putExtras(bundle);
                        startActivity(intent_nhantin);


                    } catch (Exception e) {

                        ToastNew.showToast(getApplication(), "Lỗi", Toast.LENGTH_LONG);
                    }
                }
            });
        }
    };





    private Emitter.Listener callback_question = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    final String noidung, account;
                    try {
                        noidung = data.getString("question");
                        Gson gson = new Gson();
                        question_nhan = gson.fromJson(noidung, Question.class);

                        Bitmap question_bitmap = ToolSupport.convertStringBase64ToBitmap(question_nhan.getImageString());

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ExpertMainActivity.this);
                        builder.setMessage(question_nhan.getTittle())
                                .setTitle("Câu hỏi: " + question_nhan.getMoney() + " VND")
                                .setCancelable(false)
                                .setIcon(new BitmapDrawable(getResources(), question_bitmap))
                                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        PhanHoiYeuCauGiaiDap phanHoiYeuCauGiaiDap = new PhanHoiYeuCauGiaiDap(question_nhan.getId(), question_nhan.getFrom(), question_nhan.getMoney(), true);

                                        mSocket.emit("expert-phanhoi", phanHoiYeuCauGiaiDap.toJSON());
                                        mSocket.once("bat dau cuoc thao luan", callback_thaoluan);
                                        Switch sw_expert_ready = fragment2.getActivity().findViewById(R.id.sw_expert_ready);
                                        sw_expert_ready.setChecked(false);
                                        dialog.cancel();

                                    }
                                })
                                .setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        PhanHoiYeuCauGiaiDap phanHoiYeuCauGiaiDap = new PhanHoiYeuCauGiaiDap(question_nhan.getId(), question_nhan.getFrom(), question_nhan.getMoney(), false);
                                        mSocket.emit("expert-phanhoi", phanHoiYeuCauGiaiDap.toJSON());
                                        mSocket.connect();
                                        dialog.cancel();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                    } catch (Exception e) {

                        ToastNew.showToast(getApplication(), "Lỗi", Toast.LENGTH_LONG);
                    }
                }
            });
        }
    };




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;

                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    if (mSocket.connected())
                        mSocket.emit("get-introdution-expert", sessionManager.getAccount());
                    return true;

                case R.id.navigation_notifications:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    if (mSocket.connected())
                        mSocket.emit("get-list-bxh", "get-list-bxh");
                    return true;
            }
            return false;
        }
    };


}
