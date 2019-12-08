package com.duong.pushnotification.Adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duong.pushnotification.classes.ThongBao;
import com.duong.pushnotification.R;

import java.util.ArrayList;

public class ThongBaoAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<ThongBao> listThongBao;

    public ThongBaoAdapter(Context context, int layout, ArrayList<ThongBao> listThongBao) {
        this.context = context;
        this.layout = layout;
        this.listThongBao = listThongBao;
    }

    @Override
    public int getCount() {
        return listThongBao.size();
    }

    @Override
    public Object getItem(int i) {
        return listThongBao.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View inflater = layoutInflater.inflate(layout, null, false);
        TextView tv_tieude = inflater.findViewById(R.id.tv_tieude);
        TextView tv_noidung = inflater.findViewById(R.id.tv_noidungtb);
        LinearLayout ln_thongbao = inflater.findViewById(R.id.ln_thongbao);

        String TieuDe = listThongBao.get(i).getTieuDe();
        String NoiDung = listThongBao.get(i).getNoiDung();
        Boolean DaXem = listThongBao.get(i).getDaXem();

        if (DaXem) {
            ln_thongbao.setBackground(inflater.getResources().getDrawable(R.drawable.layout_bg_daxem));
        } else {
            ln_thongbao.setBackground(inflater.getResources().getDrawable(R.drawable.layout_bg_chuaxem));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_tieude.setText(Html.fromHtml(TieuDe, Html.FROM_HTML_MODE_COMPACT));
            tv_noidung.setText(Html.fromHtml(NoiDung, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_tieude.setText(Html.fromHtml(TieuDe));
            tv_noidung.setText(Html.fromHtml(NoiDung));
        }

        return inflater;
    }
}
