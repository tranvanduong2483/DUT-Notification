package com.duong.anyquestion.classes;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.duong.anyquestion.MessageHistoryActivity;
import com.duong.anyquestion.MessageListActivity;
import com.duong.anyquestion.R;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<History> listHisory;


    public HistoryAdapter(Context context, int layout, ArrayList<History> listNotification) {
        this.context = context;
        this.layout = layout;
        this.listHisory = listNotification;
    }

    @Override
    public int getCount() {
        return listHisory.size();
    }

    @Override
    public Object getItem(int i) {
        return listHisory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View inflater = layoutInflater.inflate(layout, null, false);
        TextView tv_tittle = inflater.findViewById(R.id.tv_tittle);
        TextView tv_field = inflater.findViewById(R.id.tv_field);
        RatingBar rb_danhgia = inflater.findViewById(R.id.rb_danhgia);


        String title = listHisory.get(i).title;
        String field = listHisory.get(i).name;
        float star = listHisory.get(i).star;
        final int conversation_id = listHisory.get(i).conversation_id;
        final String id_user = listHisory.get(i).id_user;
        final String id_expert = listHisory.get(i).id_expert;



        inflater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("conversation_id", conversation_id);
                bundle.putString("id_expert", id_expert);
                bundle.putString("id_user", id_user);

                ToastNew.showToast(context, conversation_id + "", Toast.LENGTH_LONG);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        tv_tittle.setText(title);
        tv_field.setText(field);
        rb_danhgia.setRating(star);


        return inflater;
    }
}
