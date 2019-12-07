package com.duong.anyquestion.ui_expert;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.duong.anyquestion.R;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Introduction;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class HelpFragment extends Fragment {
    private SessionManager sessionManager;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private EditText edt_keywords, edt_gioithieu;
    private TextView tv_note;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        sessionManager = new SessionManager((Activity) view.getContext());


        edt_gioithieu = view.findViewById(R.id.edt_gioithieu);
        edt_keywords = view.findViewById(R.id.edt_keywords);
        tv_note = view.findViewById(R.id.tv_note);

        edt_keywords.setEnabled(false);
        edt_gioithieu.setEnabled(false);



        final Switch sw_expert_ready = view.findViewById(R.id.sw_expert_ready);

        sw_expert_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_expert_ready.isChecked()) {
                    if (!mSocket.connected()) {
                        ToastNew.showToast(getContext(), "Lỗi kết nối, thử lại sau!", Toast.LENGTH_LONG);
                        sw_expert_ready.setChecked(false);
                        return;
                    }

                    String gioithieu = edt_gioithieu.getText() + "";
                    String keywords = edt_keywords.getText() + "";

                    if (gioithieu.isEmpty() || keywords.isEmpty()) {
                        ToastNew.showToast(getContext(), "Thiếu thông tin", Toast.LENGTH_LONG);
                        sw_expert_ready.setChecked(false);
                        return;
                    }


                    Introduction introduction = new Introduction(sessionManager.getAccount(), keywords, gioithieu);
                    mSocket.emit("expert-send-ready", true, introduction.toJSON());

                    edt_gioithieu.setEnabled(false);
                    edt_keywords.setEnabled(false);
                    tv_note.setText("Hãy tắt sẳn sàng nếu bạn muốn sửa thông tin");

                } else {
                    edt_gioithieu.setEnabled(true);
                    edt_keywords.setEnabled(true);
                    tv_note.setText("Nếu bạn rãnh hãy bấm sẳn sàng");
                    mSocket.emit("expert-send-ready", false);

                }


            }
        });


        mSocket.on("disconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sw_expert_ready.setChecked(false);
                    }
                });
            }
        });


        mSocket.once("server-sent-introdution-expert", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject data = (JSONObject) args[0];
                            String NoiDung = data.getString("ketqua");

                            Gson gson = new Gson();

                            Introduction introduction = gson.fromJson(NoiDung, Introduction.class);


                            if (edt_keywords.getText().toString().isEmpty()) {
                                edt_keywords.setText(introduction.getKeywords());
                            }

                            if (edt_gioithieu.getText().toString().isEmpty()) {
                                edt_gioithieu.setText(introduction.getIntroduction_message());
                            }

                        } catch (Exception e) {
                            // ToastNew.showToast(getActivity(), "Lỗi", Toast.LENGTH_SHORT);

                        }

                        edt_keywords.setEnabled(true);
                        edt_gioithieu.setEnabled(true);
                        off_get_introduce = true;
                    }
                });
            }
        });

        setGetFiled();
        return view;
    }


    private boolean off_get_introduce = false;

    private void setGetFiled() {
        mSocket.emit("get-introdution-expert", sessionManager.getAccount());

        TimerTask timertaks = new TimerTask() {
            @Override
            public void run() {
                if (!off_get_introduce) {
                    if (mSocket.connected())
                        mSocket.emit("get-introdution-expert", sessionManager.getAccount());
                }
            }
        };

        long delay = 3000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timertaks, 0, delay);
    }




}