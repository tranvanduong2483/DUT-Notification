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
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ForgetPasswordctivity extends AppCompatActivity {

    private Socket mSocket = ConnectThread.getInstance().getSocket();


    Button btn_cancel, btn_send;
    EditText edt_account, edt_answer, edt_password1, edt_password2;

    private ArrayList<SecurityQuestion> array_security_question;
    private ArrayAdapter<SecurityQuestion> adapter_field;
    Spinner spn_security_question;
    private ProgressBar pb_loading_question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwordctivity);

        edt_account = findViewById(R.id.edt_account);
        edt_answer = findViewById(R.id.edt_answer);
        edt_password1 =findViewById(R.id.edt_password1);
        edt_password2 =findViewById(R.id.edt_password2);
        pb_loading_question = findViewById(R.id.pb_loading_question);

        spn_security_question = findViewById(R.id.spn_security_question);
        spn_security_question.setVisibility(View.GONE);


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

        btn_cancel =findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSocket.on("server-send-security-question", callback_get_security_question);
        setGetSecurityQuestion();


        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account =edt_account.getText() + "";
                int security_question_id = array_security_question.get(spn_security_question.getSelectedItemPosition()).getSecurity_question_id();
                String answer = edt_answer.getText() + "";
                String password1 = edt_password1.getText() + "";
                String password2 = edt_password2.getText() + "";

                if (account.isEmpty() || security_question_id==0| answer.isEmpty() || password1.isEmpty() || password2.isEmpty()){
                    ToastNew.showToast(ForgetPasswordctivity.this, "Thiếu thông tin", Toast.LENGTH_LONG);
                    return;
                }

                if (!password1.equals(password2)){
                    ToastNew.showToast(ForgetPasswordctivity.this, "Mật khẩu mới chưa khớp nhau!", Toast.LENGTH_LONG);
                    return;
                }

                mSocket.emit("client-request-forget-password",account,security_question_id,answer,password1);
                btn_send.setEnabled(false);
            }
        });


        mSocket.on("server-sent-status-forgeting-password", callback_get_change_security_question);


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

                        if (array_security_question.size()!=1 && array_security_question.size()!=0){
                            spn_security_question.setVisibility(View.VISIBLE);
                            pb_loading_question.setVisibility(View.GONE);
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
                            ToastNew.showToast(ForgetPasswordctivity.this, "Khôi phục mật khẩu thành công", Toast.LENGTH_LONG);
                            finish();
                        }else {
                            ToastNew.showToast(ForgetPasswordctivity.this, "Không thể xác minh", Toast.LENGTH_LONG);
                        }

                        btn_send.setEnabled(true);
                    } catch (Exception ignored) {

                        ToastNew.showToast(ForgetPasswordctivity.this, "Lỗi", Toast.LENGTH_LONG);

                    }

                }
            });
        }
    };




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
