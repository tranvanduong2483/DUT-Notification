package com.duong.pushnotification;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.duong.pushnotification.AsyncTask.AsyncTask_Logout;
import java.util.HashMap;

public class PersonalFragment extends Fragment {
    private  TextView mTv_mssv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        SessionManager sessionManager = new SessionManager(view.getContext());
        sessionManager.checkLogin();

        TextView mTv_name = view.findViewById(R.id.tv_name);
        mTv_mssv = view.findViewById(R.id.tv_Mssv);
        TextView mTv_Lop = view.findViewById(R.id.tv_Lop);
        TextView mTv_khoa = view.findViewById(R.id.tv_Khoa);
        TextView mTv_email = view.findViewById(R.id.tv_Email);
        TextView mTv_sotinchi = view.findViewById(R.id.tv_sotinchi);
        TextView mTv_diemtichluy = view.findViewById(R.id.tv_diemtichluy);
        ImageView mMb_logout = view.findViewById(R.id.imglogout);

        HashMap<String, String> user = sessionManager.getUserDetail();
        mTv_name.setText(user.get(SessionManager.TEN));
        mTv_Lop.setText(user.get(SessionManager.LOP));
        mTv_khoa.setText(user.get(SessionManager.KHOA));
        mTv_email.setText(user.get(SessionManager.EMAIL));
        mTv_mssv.setText(user.get(SessionManager.MSSV));
        mTv_sotinchi.setText(user.get(SessionManager.TONGTC));
        mTv_diemtichluy.setText(user.get(SessionManager.TICHLUY));



        mMb_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Token = MyFirebaseMessagingService.getToken(view.getContext());
                final String url = getResources().getString(R.string.URL) + "/logout.php?Token=" + Token;
                new AsyncTask_Logout((Activity) view.getContext()).execute(url);
            }
        });


        ActionBar actionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Cá nhân"); //Thiết lập tiêu đề nếu muốn

        return view;
    }

}
