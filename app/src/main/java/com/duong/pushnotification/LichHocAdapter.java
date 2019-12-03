package com.duong.pushnotification;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LichHocAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<HocPhan> list_hocphan;


    public LichHocAdapter(Context context, int layout, ArrayList<HocPhan> list_hocphan){
        this.context = context;
        this.layout=layout;
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

        View inflater = layoutInflater.inflate(layout,null,false);
        TextView tv_tenhocphan = inflater.findViewById(R.id.tv_tenhocphan);
        TextView tv_lichhoc = inflater.findViewById(R.id.tv_lichhoc);
        TextView tv_tuanhoc =inflater.findViewById(R.id.tv_tuanhoc);

        String TenHocPhan = list_hocphan.get(i).getTenHocPhan();
        String LichHoc = list_hocphan.get(i).getLichHoc();
        String TuanHoc = list_hocphan.get(i).getTuanHoc();

        tv_tenhocphan.setText(TenHocPhan);
        tv_lichhoc.setText(LichHoc);
        tv_tuanhoc.setText(TuanHoc);

        return inflater;
    }
}
