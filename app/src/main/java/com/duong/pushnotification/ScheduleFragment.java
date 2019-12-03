package com.duong.pushnotification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleFragment extends Fragment {


    private ListView mlv_lichhoc;
    ArrayList<HocPhan> list_hocphan;
    LichHocAdapter lichHocAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_shedule, container, false);

        mlv_lichhoc = view.findViewById(R.id.lv_lichhoc);
        list_hocphan = new ArrayList<>();
        lichHocAdapter = new LichHocAdapter(view.getContext(), R.layout.row_hocphan_listview, list_hocphan);
        mlv_lichhoc.setAdapter(lichHocAdapter);


        list_hocphan.add(new HocPhan("1","Hình Họa","","Thứ 6: 1-3,E110A","Tuần 3-18"));
        list_hocphan.add(new HocPhan("1","Công nghệ di động","","Thứ 3: 4-3,E102A","Tuần 3-18"));
        list_hocphan.add(new HocPhan("1","Nguyên lí 1","","Thứ 3: 1-6,E110A","Tuần 3-18"));
        list_hocphan.add(new HocPhan("1","Công nghệ phần mềm","","Thứ 2: 5-6,E110A","Tuần 3-16"));
        list_hocphan.add(new HocPhan("1","Lập trình .NET","","Thứ 7: 1-3,E110A","Tuần 3-18"));
        list_hocphan.add(new HocPhan("1","Toán rời rạc","","Thứ 4: 7-10,E112A","Tuần 3-16"));

        lichHocAdapter.notifyDataSetChanged();

        ActionBar actionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Lịch học");
    return  view;
    }
}
