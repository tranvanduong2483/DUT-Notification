package com.duong.anyquestion.ui_expert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.duong.anyquestion.ChangePasswordActivity;
import com.duong.anyquestion.R;
import com.duong.anyquestion.RutTienActivity;
import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Expert;
import com.duong.anyquestion.SecurityActivity;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.EditInfomationActivity;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {


    private SessionManager sessionManager;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private View view;
    private Bundle bundle_update;
    private TextView tv_fullname, tv_education,tv_field,tv_email,tv_address,tv_money;
    private ImageView im_avatar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account_expert, container, false);

        sessionManager = new SessionManager((Activity) view.getContext());
        sessionManager.checkLogin();
        Expert expert = sessionManager.getExpert();

        im_avatar = view.findViewById(R.id.im_avatar);
        tv_fullname = view.findViewById(R.id.tv_fullname);
        tv_education = view.findViewById(R.id.tv_education);
        tv_field = view.findViewById(R.id.tv_field);
        tv_email = view.findViewById(R.id.tv_email);
        tv_address = view.findViewById(R.id.tv_address);
        tv_money = view.findViewById(R.id.tv_money);

        if (sessionManager.getAvatar() != null) {
            Bitmap bitmap = ToolSupport.loadImageFromStorage(sessionManager.getAvatar());
            im_avatar.setImageBitmap(bitmap);
        }

        tv_fullname.setText(expert.getFullName());
        tv_education.setText(expert.getEducation_id() + "");
        tv_field.setText(expert.getField_id() + "");
        tv_email.setText(expert.getEmail());
        tv_address.setText(expert.getAddress());
        tv_money.setText(expert.getMoney() + " VND");

        ImageButton btn_dangxuat = view.findViewById(R.id.btn_dangxuat);
        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSocket.connected()) {
                    mSocket.emit("logout", "expert");
                } else {
                    ToastNew.showToast(getActivity(), "Máy chủ ngắt kết nối!", Toast.LENGTH_LONG);
                }
            }
        });

        Button btn_change_password = view.findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(inten);
            }
        });

        Button btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", sessionManager.getAvatar());
                bundle.putString("name", tv_fullname.getText() + "");
                bundle.putString("email", tv_email.getText() + "");
                bundle.putString("address", tv_address.getText() + "");
                Intent intent = new Intent(getContext(), EditInfomationActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_EDIT_INFORMATION);
            }
        });

        Button btn_security =view.findViewById(R.id.btn_security);
        btn_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), SecurityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_INFORMATION);

            }
        });


        Button btn_rut_tien = view.findViewById(R.id.btn_rut_tien);
        btn_rut_tien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", sessionManager.getAvatar());
                bundle.putString("name", tv_fullname.getText() + "");
                bundle.putString("email", tv_email.getText() + "");
                bundle.putString("address", tv_address.getText() + "");
                Intent intent = new Intent(getContext(), RutTienActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mSocket.on("ketqua-logout", callback_logout);
        mSocket.on("server-to-update-status", callback_update_information);
        return view;
    }

    private int REQUEST_CODE_EDIT_INFORMATION = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_INFORMATION) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bundle_update = data.getExtras();
                    JSONObject json_update = new JSONObject();
                    json_update.put("account", sessionManager.getAccount());
                    json_update.put("name", bundle_update.getString("name"));
                    json_update.put("avatar", bundle_update.getString("avatar"));
                    json_update.put("address", bundle_update.getString("address"));
                    json_update.put("email", bundle_update.getString("email"));

                    byte[] byte_image = ToolSupport.getByteArrayFromImagePath(bundle_update.getString("avatar"));
                    mSocket.emit("client-to-update-data", json_update.toString(), byte_image,sessionManager.getType());
                    ToastNew.showToast(getActivity(), "Đang lưu trên máy chủ ....", Toast.LENGTH_LONG);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastNew.showToast(getActivity(), "Lỗi xuất hiện", Toast.LENGTH_LONG);
                }

            }
        }
    }

    private Emitter.Listener callback_logout = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String NoiDung = data.getString("ketqua");
                        if ("true".equals(NoiDung)) {
                            sessionManager.logout();
                        } else {
                            ToastNew.showToast(getActivity(), "Đăng xuất thất bại!", Toast.LENGTH_LONG);
                        }

                    } catch (JSONException ignored) { }

                }
            });
        }
    };


    private Emitter.Listener callback_update_information = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String NoiDung;
                        NoiDung = data.getString("status");
                        if ("1".equals(NoiDung)) {
                            if (bundle_update == null) return;

                            String fullname = bundle_update.getString("name", "");
                            String address = bundle_update.getString("address", "");
                            String email = bundle_update.getString("email", "");
                            String avatar = bundle_update.getString("avatar", "");
                            Bitmap bitmap = ToolSupport.loadImageFromStorage(avatar);

                            if (bitmap!=null)
                                im_avatar.setImageBitmap(bitmap);
                            tv_address.setText(address);
                            tv_email.setText(email);
                            tv_fullname.setText(fullname);

                            Expert update_expert = sessionManager.getExpert();
                            update_expert.setFullName(fullname);
                            update_expert.setEmail(email);
                            update_expert.setAddress(address);
                            update_expert.setAvatar(avatar);

                            sessionManager.createSession(update_expert);
                            ToastNew.showToast(view.getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_LONG);
                        } else {
                            ToastNew.showToast(view.getContext(), "Cập nhật thông tin thất bại", Toast.LENGTH_LONG);
                        }

                    } catch (JSONException ignored) { }

                }
            });
        }
    };



}