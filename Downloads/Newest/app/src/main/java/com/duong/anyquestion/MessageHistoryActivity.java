package com.duong.anyquestion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.History;
import com.duong.anyquestion.classes.Message;
import com.duong.anyquestion.classes.MessageListAdapter;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MessageHistoryActivity extends AppCompatActivity {
    Button btn_cancel;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    MessageListAdapter mMessageAdapter;
    RecyclerView mMessageRecycler;
    ArrayList<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_history);

        int conversation_id = 1;
        String id_expert = "", id_user = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            conversation_id = bundle.getInt("conversation_id");
            id_user = bundle.getString("id_user");
            id_expert = bundle.getString("id_expert");
        }

        messageList = new ArrayList<>();

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList, id_user, id_expert);

        ToastNew.showToast(this, id_expert + "-" + id_user, Toast.LENGTH_LONG);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        btn_cancel = findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSocket.emit("get-conversation-history", conversation_id);
        mSocket.on("server-sent-conversation-history", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject data = (JSONObject) args[0];

                    String noidung = data.toString();
                    Gson gson = new Gson();
                    Message message = gson.fromJson(noidung, Message.class);
                    message.setStatus(true);
                    messageList.add(message);

                    Collections.sort(messageList, new Comparator<Message>() {
                        @Override
                        public int compare(Message message1, Message message2) {
                            int time1 = message1.getMessage_id();
                            int time2 = message2.getMessage_id();
                            return time1 - time2;
                        }
                    });


                    mMessageRecycler.smoothScrollToPosition(0);
                    mMessageAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    //ToastNew.showToast(MessageHistoryActivity.this, "Xuất hiện lỗi nào đó!", Toast.LENGTH_LONG);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
