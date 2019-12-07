package com.duong.anyquestion;
//1235678999

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class EditInfomationActivity extends AppCompatActivity {
    private Button btn_save, btn_cancel, btn_chonanhtuthuvien, btn_chupanh;
    private ImageView im_avatar;
    private EditText edt_fullname, edt_email, edt_address;
    String avatar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_infomation);

        im_avatar = findViewById(R.id.im_avatar);
        btn_chonanhtuthuvien = findViewById(R.id.btn_chonanhtuthuvien);
        btn_chupanh = findViewById(R.id.btn_chupanh);
        edt_address = findViewById(R.id.edt_address);
        edt_email = findViewById(R.id.edt_email);
        edt_fullname = findViewById(R.id.edt_fullname);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            avatar = bundle.getString("avatar");
            Bitmap bitmap = ToolSupport.loadImageFromStorage(avatar);
            if (bitmap != null) {
                im_avatar.setImageBitmap(bitmap);
            }
            edt_fullname.setText(bundle.getString("name", ""));
            edt_email.setText(bundle.getString("email", ""));
            edt_address.setText(bundle.getString("address", ""));
        }

        btn_chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSupport.take_picture(EditInfomationActivity.this, REQUEST_TAKE_PHOTO);
            }
        });

        btn_chonanhtuthuvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSupport.choosePicture(EditInfomationActivity.this, REQUEST_CHOOSE_PHOTO);
            }
        });


        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("avatar", avatar);
                bundle.putString("name", edt_fullname.getText() + "");
                bundle.putString("address", edt_address.getText() + "");
                bundle.putString("email", edt_email.getText() + "");

                final Intent data = new Intent();
                data.putExtras(bundle);

                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private final int REQUEST_TAKE_PHOTO = 123;
    private final int REQUEST_CHOOSE_PHOTO = 132;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap avatar_bitmap = BitmapFactory.decodeStream(is);
                avatar_bitmap = ToolSupport.resize(avatar_bitmap, 300, 300);
                im_avatar.setImageBitmap(avatar_bitmap);


                avatar = ToolSupport.saveToInternalStorage(avatar_bitmap, this);
            } catch (Exception e) {
                e.printStackTrace();
                ToastNew.showToast(this, "Lỗi", Toast.LENGTH_SHORT);
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (data.getExtras() == null) return;
            Bitmap avatar_bitmap = (Bitmap) data.getExtras().get("data");
            avatar_bitmap = ToolSupport.resize(avatar_bitmap, 300, 300);

            im_avatar.setImageBitmap(avatar_bitmap);
            avatar = ToolSupport.saveToInternalStorage(avatar_bitmap, this);
        }
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
