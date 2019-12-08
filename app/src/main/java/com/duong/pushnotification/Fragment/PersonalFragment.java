package com.duong.pushnotification.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duong.pushnotification.MyFirebaseMessagingService;
import com.duong.pushnotification.R;
import com.duong.pushnotification.classes.SessionManager;
import com.duong.pushnotification.classes.ConnectThread;
import com.duong.pushnotification.classes.SinhVien;
import com.duong.pushnotification.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class PersonalFragment extends Fragment {
    static String title = "Cá nhân";
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private SessionManager sessionManager;
    private TextView mTv_Ten, mTv_MSSV, mTv_Lop, mTv_Khoa, mTv_Email, mTv_TinChi, mTv_T4;
    private ImageView mImv_Thoat;
    private ProgressBar mPg_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        Init(view);
        ShowThongTinSinhVien();
        setEvent();

        return view;
    }

    private void Init(View view) {
        sessionManager = new SessionManager(view.getContext());
        mTv_Ten = view.findViewById(R.id.tv_ten);
        mTv_MSSV = view.findViewById(R.id.tv_mssv);
        mTv_Lop = view.findViewById(R.id.tv_lop);
        mTv_Khoa = view.findViewById(R.id.tv_khoa);
        mTv_Email = view.findViewById(R.id.tv_email);
        mTv_TinChi = view.findViewById(R.id.tv_tinchi);
        mTv_T4 = view.findViewById(R.id.tv_t4);
        mImv_Thoat = view.findViewById(R.id.imv_thoat);
        mPg_logout = view.findViewById(R.id.pg_logout);
    }

    private void ShowThongTinSinhVien() {
        SinhVien sv = sessionManager.getSinhVien();
        mTv_Ten.setText(sv.getTen());
        mTv_Lop.setText(sv.getLop());
        mTv_Khoa.setText(sv.getKhoa());
        mTv_Email.setText(sv.getEmail());
        mTv_MSSV.setText(sv.getMaSinhVien());
        mTv_TinChi.setText(sv.getTC());
        mTv_T4.setText(sv.getT4());
    }

    private void setEvent() {
        mImv_Thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MaSinhVien = sessionManager.getSinhVien().getMaSinhVien();
                String Token = MyFirebaseMessagingService.getToken(view.getContext());

                if (!mSocket.connected()) {
                    ToastNew.showToast(getActivity(), "Không có kết nối", Toast.LENGTH_LONG);
                    return;
                }

                mSocket.emit("user-request-logout", MaSinhVien, Token);
                mSocket.once("server-sent-logout-status", callback_logout);

                mImv_Thoat.setVisibility(ImageView.GONE);
                mPg_logout.setVisibility(ProgressBar.VISIBLE);
            }
        });
    }

    private Emitter.Listener callback_logout = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final Activity activity = getActivity();
            if (activity == null) return;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((int) args[0] == 1)
                            sessionManager.logout();
                        ToastNew.showToast(activity, "Đã đăng xuất!", Toast.LENGTH_LONG);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastNew.showToast(activity, "Chưa đăng xuất được!", Toast.LENGTH_LONG);
                    }
                    mImv_Thoat.setVisibility(ImageView.VISIBLE);
                    mPg_logout.setVisibility(ProgressBar.GONE);
                }
            });
        }
    };
}