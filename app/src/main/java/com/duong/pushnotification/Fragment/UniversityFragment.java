package com.duong.pushnotification.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duong.pushnotification.DetailNotificationActivity;
import com.duong.pushnotification.classes.SessionManager;
import com.duong.pushnotification.classes.ThongBao;
import com.duong.pushnotification.R;
import com.duong.pushnotification.Adapters.ThongBaoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UniversityFragment extends Fragment {
    static String title ="Thông báo chung";
    private ArrayList<ThongBao> list_thongbao;
    private ListView mListView;
    private ThongBaoAdapter thongBaoAdapter;
    private RelativeLayout relativeLayoutProgress;
    private SessionManager sessionManager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mData = database.getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_university, container, false);

        Init(view);
        setEvent();

        return view;
    }

    private void Init(View view) {
        sessionManager = new SessionManager(view.getContext());
        list_thongbao = new ArrayList<>();
        thongBaoAdapter = new ThongBaoAdapter(view.getContext(), R.layout.row_thongbao_listview, list_thongbao);
        relativeLayoutProgress = view.findViewById(R.id.rl_tb_thongbaochung);
        mListView = view.findViewById(R.id.lv_TBdaotao);
        mListView.setAdapter(thongBaoAdapter);
    }

    private void setEvent() {
        String MSSV = sessionManager.getMSSV();
        mData.child(MSSV).child(ThongBao.THONG_BAO_CHUNG)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list_thongbao.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ThongBao tb = postSnapshot.getValue(ThongBao.class);
                            list_thongbao.add(tb);
                        }

                        if (list_thongbao.size()==0){
                            relativeLayoutProgress.setVisibility(View.VISIBLE);
                            thongBaoAdapter.notifyDataSetChanged();
                            return;
                        }


                        Collections.sort(list_thongbao, new Comparator<ThongBao>() {
                            @Override
                            public int compare(ThongBao thongBao1, ThongBao thongBao2) {
                                return 0;
                            }
                        });

                        thongBaoAdapter.notifyDataSetChanged();
                        relativeLayoutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), DetailNotificationActivity.class);
                ThongBao tb = (ThongBao) adapterView.getItemAtPosition(i);
                intent.putExtra("ThongBao", tb);
                startActivity(intent);
            }
        });

    }
}
