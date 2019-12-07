package com.duong.anyquestion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.SecurityQuestion;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SecurityActivity extends AppCompatActivity {





    private Socket mSocket = ConnectThread.getInstance().getSocket();
    Button btn_cancel,btn_save;
    SessionManager sessionManager;
    EditText edt_password,edt_answer;

    ProgressBar pb_loading_question;


    private ArrayList<SecurityQuestion> array_security_question;
    private ArrayAdapter<SecurityQuestion> adapter_field;
    Spinner spn_security_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);





        pb_loading_question = findViewById(R.id.pb_loading_question);
        edt_answer = findViewById(R.id.edt_answer);
        spn_security_question = findViewById(R.id.spn_security_question);
        array_security_question=new ArrayList<>();
        array_security_question.add(new SecurityQuestion(-1, "Chọn câu hỏi bảo mật?"));


        adapter_field = new ArrayAdapter<SecurityQuestion>(this, android.R.layout.simple_spinner_item, array_security_question){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spn_security_question.setAdapter(adapter_field);

        sessionManager = new SessionManager(this);


        edt_password = findViewById(R.id.edt_password);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = sessionManager.getType();
                String account = sessionManager.getAccount();
                String password = edt_password.getText() +"";
                String answer = edt_answer.getText() + "";
                int security_question_id = array_security_question.get(spn_security_question.getSelectedItemPosition()).getSecurity_question_id();

                if (spn_security_question.getSelectedItemPosition()==0 ||password.isEmpty() ) {
                    ToastNew.showToast(getApplication(), "Thiếu thông tin", Toast.LENGTH_LONG);
                    return;
                }

                btn_save.setEnabled(false);


                mSocket.emit("client-change-security-question",type,account, password,security_question_id,answer);
            }
        });

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSocket.on("server-send-security-question", callback_get_security_question);
        setGetSecurityQuestion();

        mSocket.on("server-sent-status-updating-security-question", callback_get_change_security_question);


        spn_security_question.setVisibility(View.GONE);
        edt_answer.setVisibility(View.GONE);
        edt_password.setVisibility(View.GONE);
        btn_save.setEnabled(false);
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


    private Emitter.Listener callback_get_security_question = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        array_security_question.clear();
                        array_security_question.add(new SecurityQuestion(-1, "Chọn câu hỏi bảo mật?"));
                        JSONArray jsonArray = (JSONArray) args[0];
                        Gson gson = new Gson();
                        for (int i=0; i<jsonArray.length(); i++){
                            SecurityQuestion securityQuestion = gson.fromJson( jsonArray.get(i).toString(),SecurityQuestion.class);
                            array_security_question.add(securityQuestion);
                        }
                        adapter_field.notifyDataSetChanged();

                        if (array_security_question.size() != 0 && array_security_question.size()!=1){
                            pb_loading_question.setVisibility(View.GONE);
                            spn_security_question.setVisibility(View.VISIBLE);
                            edt_answer.setVisibility(View.VISIBLE);
                            edt_password.setVisibility(View.VISIBLE);
                            btn_save.setEnabled(true);

                        }

                    } catch (Exception ignored) { }

                }
            });
        }
    };

    private Emitter.Listener callback_get_change_security_question = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        int status = (int) args[0];
                        if (status ==1 ){
                            ToastNew.showToast(SecurityActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG);
                            finish();
                        }else {
                            ToastNew.showToast(SecurityActivity.this, "Không thành công", Toast.LENGTH_LONG);
                        }

                        btn_save.setEnabled(true);
                    } catch (Exception ignored) {

                        ToastNew.showToast(SecurityActivity.this, "Lỗi", Toast.LENGTH_LONG);

                    }



                }
            });
        }
    };



    private void setGetSecurityQuestion() {
        mSocket.emit("client-get-security-question","get_security_quesiton");

        TimerTask timertaks = new TimerTask() {
            @Override
            public void run() {
                if (array_security_question.isEmpty() || array_security_question.size()==1) {
                    if (mSocket.connected())
                        mSocket.emit("client-get-security-question", "get_security_quesiton");
                }
            }
        };


        long delay = 3000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timertaks, 0, delay);

    }
}
