package com.duong.pushnotification.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duong.pushnotification.Adapters.LichHocAdapter;
import com.duong.pushnotification.R;
import com.duong.pushnotification.classes.HocPhan;
import com.duong.pushnotification.classes.SessionManager;
import com.duong.pushnotification.classes.ToastNew;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleFragment extends Fragment {
    static String title ="Lịch học";
    private ArrayList<HocPhan> list_hocphan;
    private  ListView mLv_LichHoc;
    private RelativeLayout relativeLayoutProgress;
    private SessionManager sessionManager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mData = database.getReference();
    private LichHocAdapter lichHocAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shedule, container, false);

        Init(view);
        Event();

        return  view;
    }

    private void Event() {
        mLv_LichHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HocPhan HP = (HocPhan) adapterView.getItemAtPosition(i);
                ToastNew.showToast(getActivity(), HP.getTenHocPhan(), Toast.LENGTH_LONG);
            }
        });

        String MSSV = sessionManager.getMSSV();
        mData.child(MSSV).child("HocPhan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_hocphan.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HocPhan HP =postSnapshot.getValue(HocPhan.class);
                    list_hocphan.add(HP);
                }

                if (list_hocphan.size()==0){
                    relativeLayoutProgress.setVisibility(View.VISIBLE);
                    lichHocAdapter.notifyDataSetChanged();
                    return;
                }

                Collections.sort(list_hocphan, new Comparator<HocPhan>() {
                    @Override
                    public int compare(HocPhan hocPhan1, HocPhan hocPhan2) {
                        return 0;
                    }
                });

                lichHocAdapter.notifyDataSetChanged();
                relativeLayoutProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void Init(View view) {
        list_hocphan = new ArrayList<>();
        lichHocAdapter = new LichHocAdapter(view.getContext(), R.layout.row_hocphan_listview, list_hocphan);
        sessionManager =new SessionManager(view.getContext());
        mLv_LichHoc = view.findViewById(R.id.lv_lichhoc);
        relativeLayoutProgress = view.findViewById(R.id.rl_tb_lichhoc);
        mLv_LichHoc.setAdapter(lichHocAdapter);
    }
}
