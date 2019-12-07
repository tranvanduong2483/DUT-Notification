package com.duong.anyquestion.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duong.anyquestion.R;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegisterActivity extends AppCompatActivity {


    Button btn_login, btn_register, btn_clear;
    EditText edt_account,edt_mail, edt_address, edt_password1,edt_password2, edt_fullname;
    Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        mSocket = ConnectThread.getInstance().getSocket();
        mSocket.on("ket-qua-dang-ki-user",callback_dangky_user );

        edt_account = findViewById(R.id.edt_account);
        edt_address = findViewById(R.id.edt_address);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_password1=  findViewById(R.id.edt_password1);
        edt_password2 = findViewById(R.id.edt_password2);
        edt_mail = findViewById(R.id.edt_email);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Account = edt_account.getText() +"";
                String FullName = edt_fullname.getText() +"";
                String Password1 =  edt_password1.getText() +"";
                String Password2 =  edt_password2.getText() +"";
                String Mail = edt_mail.getText() +"";
                String Address = edt_address.getText() +"";


                if (Account.isEmpty() || FullName.isEmpty() || Password1.isEmpty()
                    || Address.isEmpty() || Mail.isEmpty() || Password2.isEmpty()){
                    Toast.makeText(getApplication(),"Thiếu thông tin", Toast.LENGTH_LONG).show();
                    return;
                }

                if (Password1.compareTo(Password2) !=0){
                    Toast.makeText(getApplication(),"Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User(Account, Password1, FullName,Address,Mail);
                mSocket.emit("client-dang-ki-user",user.toJSON());
            }
        });


        btn_clear = findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_address.setText("");
                edt_fullname.setText("");
                edt_mail.setText("");
                edt_account.setText("");
                edt_password1.setText("");
                edt_password2.setText("");
            }
        });





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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





    private Emitter.Listener callback_dangky_user = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String noidung;
                    try {
                        noidung = data.getString("ketqua");
                        if (noidung=="true"){
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }else{
                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại!",Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };
}