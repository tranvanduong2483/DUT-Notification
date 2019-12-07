
package com.duong.anyquestion.ui_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.duong.anyquestion.MessageListActivity;
import com.duong.anyquestion.R;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.History;
import com.duong.anyquestion.classes.HistoryAdapter;
import com.duong.anyquestion.classes.Message;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HistoryFragment extends Fragment {


    Button btn_refesh;
    View view;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private ArrayList<History> list_history;
    private HistoryAdapter historyAdapter;
    private ListView lv_history;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        list_history = new ArrayList<>();
        lv_history = view.findViewById(R.id.lv_history);


        historyAdapter = new HistoryAdapter(view.getContext(), R.layout.item_question_list, list_history);
        lv_history.setAdapter(historyAdapter);


        btn_refesh = view.findViewById(R.id.btn_refesh);

        btn_refesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("get-list-history");
            }
        });


        mSocket.on("server-sent-list-history", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray data = (JSONArray) args[0];

                                    list_history.clear();
                                    for (int i = 0; i < data.length(); i++) {

                                        String noidung = data.get(i).toString();
                                        Gson gson = new Gson();
                                        History history = gson.fromJson(noidung, History.class);
                                        list_history.add(0, history);
                                    }
                                    historyAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  ToastNew.showToast(getActivity(), "Xuất hiện lỗi nào đó!", Toast.LENGTH_LONG);
                                }
                            }
                        });
                    }
                }
        );


        setGetHistory();

        return view;
    }


    private void setGetHistory() {
        mSocket.emit("get-list-history");

        TimerTask timertaks = new TimerTask() {
            @Override
            public void run() {
                if (list_history.isEmpty()) {
                    if (mSocket.connected())
                        mSocket.emit("get-list-history");
                }
            }
        };

        long delay = 3000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timertaks, 0, delay);
    }
}