package com.duong.anyquestion.ui_expert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.duong.anyquestion.R;
import com.duong.anyquestion.classes.BxhAdapter;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.History;
import com.duong.anyquestion.classes.Item_XepHang;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RatingFragment extends Fragment {


    private Socket mSocket = ConnectThread.getInstance().getSocket();

    ListView lv_list_bxh;
    BxhAdapter bxhAdapter;
    ArrayList<Item_XepHang> item_xepHangs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_xephang, container, false);

        item_xepHangs = new ArrayList<>();
        lv_list_bxh = view.findViewById(R.id.lv_list_bxh);
        bxhAdapter = new BxhAdapter(view.getContext(), R.layout.item_expert_rating, item_xepHangs);
        lv_list_bxh.setAdapter(bxhAdapter);


        mSocket.on("server-sent-list-bxh", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray data = (JSONArray) args[0];

                                    if (data.length() == 0) return;
                                    item_xepHangs.clear();
                                    for (int i = 0; i < data.length(); i++) {

                                        String noidung = data.get(i).toString();
                                        Gson gson = new Gson();
                                        Item_XepHang item_xepHang = gson.fromJson(noidung, Item_XepHang.class);
                                        item_xepHangs.add(0, item_xepHang);
                                    }
                                    bxhAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  ToastNew.showToast(getActivity(), "Xuất hiện lỗi nào đó!", Toast.LENGTH_LONG);
                                }
                            }
                        });
                    }
                }
        );
        setGetBxh();
        return view;
    }


    private void setGetBxh() {
        mSocket.emit("get-list-bxh", "get-list-bxh");
        TimerTask timertaks = new TimerTask() {
            @Override
            public void run() {
                if (mSocket.connected())
                    mSocket.emit("get-list-bxh", "get-list-bxh");
            }
        };


        long delay = 30000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timertaks, 0, delay);
    }
}