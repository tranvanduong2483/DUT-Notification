package com.duong.anyquestion.ui_user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duong.anyquestion.LoadingSearchExpertActivity;
import com.duong.anyquestion.LoginActivity;
import com.duong.anyquestion.MessageListActivity;
import com.duong.anyquestion.R;
import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Field;
import com.duong.anyquestion.classes.Question;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.duong.anyquestion.register.UserRegisterActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchExpertFragment extends Fragment {


    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private EditText edt_tille, edt_note;
    private TextView tv_money;
    private final int REQUEST_TAKE_PHOTO = 123;
    private final int REQUEST_CHOOSE_PHOTO = 132;
    private View view;
    private ImageView iv_image;
    private String avatarString = null;
    private ArrayList<Field> array_field;
    private ArrayAdapter<Field> adapter_field;
    private Spinner spn_field;
    private ProgressBar pb_loading_field;
    private   Button btn_search_expert;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_expert, container, false);

        edt_tille = view.findViewById(R.id.edt_tittle);
        edt_note = view.findViewById(R.id.edt_note);
        tv_money = view.findViewById(R.id.tv_money);
        iv_image = view.findViewById(R.id.iv_image);
        spn_field =view.findViewById(R.id.spn_field);
        pb_loading_field= view.findViewById(R.id.pb_loading_field);
        spn_field.setVisibility(View.GONE);


        array_field=new ArrayList<>();
        array_field.add(new Field(-1, "Chọn lĩnh vực"));

        adapter_field = new ArrayAdapter<Field>(view.getContext(), android.R.layout.simple_spinner_item, array_field){
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
        spn_field.setAdapter(adapter_field);

         btn_search_expert = view.findViewById(R.id.btn_search_expert);
        btn_search_expert.setEnabled(false);
        btn_search_expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tittle = edt_tille.getText() + "";
                int field_id = array_field.get(spn_field.getSelectedItemPosition()).getField_id();
                String imageString = avatarString;
                String note = edt_note.getText() + "";
                int money = Integer.parseInt(tv_money.getText().toString());


                SessionManager sessionManager = new SessionManager(getActivity());

                Question question = new Question(1, tittle, field_id, imageString, note, money, sessionManager.getAccount());

                if ( avatarString ==null) {
                    ToastNew.showToast(getActivity(), "Thiếu ảnh", Toast.LENGTH_SHORT);
                    return;
                }
                if (tittle.isEmpty() ||spn_field.getSelectedItemPosition()==0 ) {
                    ToastNew.showToast(getActivity(), "Thiếu thông tin", Toast.LENGTH_SHORT);
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("question", question.toJSON());
                Intent intent_loading_search_expert = new Intent(view.getContext(), LoadingSearchExpertActivity.class);
                intent_loading_search_expert.putExtras(bundle);
                startActivity(intent_loading_search_expert);
            }
        });

        Button btn_clear = view.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_tille.setText("");
                edt_note.setText("");
                iv_image.setImageResource(R.drawable.math_example);
                avatarString = null;
                spn_field.setSelection(0);
            }
        });

        Button btn_upload = view.findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);

            }
        });

        Button btn_camera = view.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });

        mSocket.on("server-sent-field",callback_get_field);
        setGetFiled();
        return view;
    }


    private void setGetFiled() {
        mSocket.emit("client-get-field", "client-get-field *****");

        TimerTask timertaks = new TimerTask() {
            @Override
            public void run() {
                if (array_field.isEmpty() || array_field.size()==1) {

                    if (mSocket.connected())
                        mSocket.emit("client-get-field", "client-get-field *****");
                }
            }
        };

        long delay = 3000L;
        Timer timer = new Timer("Timer");
        timer.schedule(timertaks, 0, delay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData();
                InputStream is = view.getContext().getContentResolver().openInputStream(uri);

                Bitmap avatar_bitmap = BitmapFactory.decodeStream(is);
                avatar_bitmap = ToolSupport.resize(avatar_bitmap, 300, 300);

                iv_image.setImageBitmap(avatar_bitmap);
                avatarString = ToolSupport.convertBitmapToStringBase64(avatar_bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                ToastNew.showToast(view.getContext(), "Lỗi", Toast.LENGTH_SHORT);
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data.getExtras() == null) return;

            Bitmap avatar_bitmap = (Bitmap) data.getExtras().get("data");
            avatar_bitmap = ToolSupport.resize(avatar_bitmap, 300, 300);

            iv_image.setImageBitmap(avatar_bitmap);
            avatarString = ToolSupport.convertBitmapToStringBase64(avatar_bitmap);
        }



    }


    private Emitter.Listener callback_get_field = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Activity activity = getActivity();
            if (activity==null) return;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        array_field.clear();
                        array_field.add(new Field(-1, "Chọn lĩnh vực"));
                        JSONArray jsonArray = (JSONArray) args[0];
                        Gson gson = new Gson();
                        for (int i=0; i<jsonArray.length(); i++){
                            Field field = gson.fromJson( jsonArray.get(i).toString(),Field.class);
                            array_field.add(field);
                        }
                        adapter_field.notifyDataSetChanged();

                        if (array_field.size()!=1 && array_field.size()!=0){
                            pb_loading_field.setVisibility(View.GONE);
                            spn_field.setVisibility(View.VISIBLE);
                            btn_search_expert.setEnabled(true);
                        }

                    } catch (Exception ignored) { }

                }
            });
        }
    };
}

