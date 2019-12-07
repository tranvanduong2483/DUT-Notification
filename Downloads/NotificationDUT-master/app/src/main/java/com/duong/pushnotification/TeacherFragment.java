package com.duong.pushnotification;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TeacherFragment extends Fragment {

    private ArrayList<Notification> list_thongbao;
    private  ListView mListView;
    private RelativeLayout relativeLayoutProgress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        SessionManager sessionManager = new SessionManager(view.getContext());

        relativeLayoutProgress = view.findViewById(R.id.rl_tb_thayco);
        mListView = view.findViewById(R.id.lv_TBThayCo);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), DetailNotificationActivity.class);
                Notification tb = (Notification) adapterView.getItemAtPosition(i);
                intent.putExtra("Notification", tb);
                startActivity(intent);
            }
        });

        list_thongbao = new ArrayList<>();
        final ThongBaoAdapter thongBaoAdapter =
                new ThongBaoAdapter(view.getContext(), R.layout.row_listview, list_thongbao);
        mListView.setAdapter(thongBaoAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mData = database.getReference();
        String MSSV = sessionManager.getMSSV();

        mData.child(MSSV).child(Notification.THONG_BAO_THAY_CO)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                        list_thongbao.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Notification tb =postSnapshot.getValue(Notification.class);
                            list_thongbao.add(tb);
                        }

                        if (list_thongbao.size()==0) return;



                        // Sorting
                        Collections.sort(list_thongbao, new Comparator<Notification>() {
                            @Override
                            public int compare(Notification notification1, Notification notification2)
                            {

                                String s = "<p class=\"MsoNormal\"><b><span style=\"font-size:13.0pt;line-height:80%;font-family:times new roman,serif;color:red\">";
                                String a = notification1.title.subSequence(s.length(), s.length()+10) +"";
                                String[] tmp = a.split("/");
                                a= tmp[2]+"/"+ tmp[1]+"/"+tmp[0];

                                String b = notification2.title.subSequence(s.length(), s.length()+10) +"";
                                String[] tmp2 = b.split("/");
                                b= tmp2[2]+"/"+ tmp2[1]+"/"+tmp2[0];
                                return  b.compareTo(a);
                            }
                        });

                        thongBaoAdapter.notifyDataSetChanged();
                        mListView.setVisibility(ListView.VISIBLE);
                        relativeLayoutProgress.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        ActionBar actionBar =  ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Thông báo thầy cô"); //Thiết lập tiêu đề nếu muốn

        return view;
    }
}
