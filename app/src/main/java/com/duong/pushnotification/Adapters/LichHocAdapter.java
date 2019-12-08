package com.duong.pushnotification.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duong.pushnotification.R;
import com.duong.pushnotification.classes.HocPhan;

import java.util.ArrayList;

public class LichHocAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<HocPhan> list_hocphan;


    public LichHocAdapter(Context context, int layout, ArrayList<HocPhan> list_hocphan) {
        this.context = context;
        this.layout = layout;
        this.list_hocphan = list_hocphan;
    }

    @Override
    public int getCount() {
        return list_hocphan.size();
    }

    @Override
    public Object getItem(int i) {
        return list_hocphan.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View inflater = layoutInflater.inflate(layout, null, false);
        TextView tv_tenhocphan = inflater.findViewById(R.id.tv_tenhocphan);
        TextView tv_lichhoc = inflater.findViewById(R.id.tv_lichhoc);
        TextView tv_tuanhoc = inflater.findViewById(R.id.tv_tuanhoc);
        TextView tv_giangvien = inflater.findViewById(R.id.tv_giangvien);
        TextView tv_tinchi = inflater.findViewById(R.id.tv_tinchi);

        HocPhan HP = list_hocphan.get(i);

        tv_tenhocphan.setText(HP.getTenHocPhan());
        tv_lichhoc.setText(HP.getLichHoc().replace(";", "\nThứ ").replace(",", ", "));
        tv_tuanhoc.setText(HP.getTuanHoc());
        tv_giangvien.setText(HP.getGiangVien());
        tv_tinchi.setText(HP.getTinChi());
        return inflater;
    }
}
