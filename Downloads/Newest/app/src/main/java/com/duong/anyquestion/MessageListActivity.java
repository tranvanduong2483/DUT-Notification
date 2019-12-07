package com.duong.anyquestion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.ConnectThread;
import com.duong.anyquestion.classes.Expert;
import com.duong.anyquestion.classes.MessageListAdapter;
import com.duong.anyquestion.classes.Message;
import com.duong.anyquestion.classes.SessionManager;
import com.duong.anyquestion.classes.ToastNew;
import com.duong.anyquestion.classes.User;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private final int REQUEST_TAKE_PHOTO = 123;
    private final int REQUEST_CHOOSE_PHOTO = 132;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private SessionManager sessionManager;
    private ImageButton btn_send, btn_camera, btn_picture;
    private EditText edt_message;
    private User user;
    private Expert expert;
    private Socket mSocket = ConnectThread.getInstance().getSocket();
    private Bundle bundle;

    private View mShadowView;
    private FloatingActionButton btn_close, btn_question;
    private FloatingActionsMenu floatingMenu;
    private int conversation_id;


    Bitmap bitmap_you;
    Bitmap bitmap_me;
    String question_json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        sessionManager = new SessionManager(this);

        AnhXa();
        initEventButton();

        if (sessionManager.isUser()) user = sessionManager.getUser();
        else expert = sessionManager.getExpert();


        bundle = getIntent().getExtras();
        if (bundle != null) {
            String stringAvatar = bundle.getString("stringAvatar");
            if (stringAvatar != null)
                bitmap_you = ToolSupport.convertStringBase64ToBitmap(stringAvatar);
            question_json = bundle.getString("question");
            conversation_id = bundle.getInt("conversation_id");
            ToastNew.showToast(MessageListActivity.this, conversation_id + "", Toast.LENGTH_LONG);
        }

        mSocket.on("server-send-message", callback_nhantinnhan);
        mSocket.on("user-ready-thao-luan", callback_gioithieu);

        mSocket.on("server-bao-nguoi-kia-da-roi-cuoc-thao-luan", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (conversation_id != (int) args[0]) return;

                        if (user != null) {
                            ToastNew.showToast(getApplication(), "Chuyên gia đã rời cuộc thảo luận!", Toast.LENGTH_LONG);
                            Intent intent = new Intent(MessageListActivity.this, RatingForExpertActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle = new Bundle();
                            bundle.putInt("conversation_id", conversation_id);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastNew.showToast(getApplication(), "Người thắc mắc đã rời cuộc thảo luận!", Toast.LENGTH_LONG);
                        }

                        finish();

                    }
                });
            }
        });


        mSocket.on("send-image-complete", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            int position = (int) args[0];
                            boolean status = (args[1] + "").equals("ok");

                            messageList.get(position).setStatus(status);

                            mMessageAdapter.notifyDataSetChanged();


                            ToastNew.showToast(getApplication(), position + "-" + args[1], Toast.LENGTH_LONG);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastNew.showToast(getApplication(), "Lỗi rồi", Toast.LENGTH_LONG);

                        }


                    }
                });
            }
        });


        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);


        if (user != null) {
            ToastNew.showToast(this, "Bạn và chuyên gia đã được kết nối với nhau!", Toast.LENGTH_LONG);
            mSocket.emit("user-ready-thao-luan");
        }

        if (expert != null) {
            ToastNew.showToast(this, "Bạn và người đặt câu hỏi đã được kết nối với nhau!", Toast.LENGTH_LONG);
            mSocket.emit("expert-ready-thao-luan");
        }


        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void AnhXa() {
        btn_picture = findViewById(R.id.btn_picture);
        btn_send = findViewById(R.id.btn_send);
        edt_message = findViewById(R.id.edt_message);
        btn_camera = findViewById(R.id.btn_camera);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mShadowView=findViewById(R.id.shadowView);
        btn_close = findViewById(R.id.btn_close);
        btn_question = findViewById(R.id.btn_question);
    }


    private void initEventButton() {
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSupport.choosePicture(MessageListActivity.this, REQUEST_CHOOSE_PHOTO);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edt_message.getText().toString();
                if (message.isEmpty()) return;

                Message message_new = new Message(conversation_id, user != null ? user.getAccount() : expert.getExpert_id(), message, false);
                sendMessage(message_new);
                edt_message.setText("");
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolSupport.take_picture(MessageListActivity.this, REQUEST_TAKE_PHOTO);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingMenu.collapseImmediately();
                onBackPressed();
            }
        });


        floatingMenu = findViewById(R.id.floatingMenu);

        floatingMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mShadowView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                mShadowView.setVisibility(View.GONE);
            }
        });

        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageListActivity.this, DetailQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("question", question_json);
                intent.putExtras(bundle);
                startActivity(intent);
                floatingMenu.collapseImmediately();
            }
        });
    }

    private void sendMessage(Message message_new) {
        messageList.add(message_new);
        mMessageAdapter.notifyItemInserted(messageList.size() - 1);
        mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
        mSocket.emit("client-send-message-to-other-people", message_new.toJSON(), messageList.size() - 1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        InputStream is = getContentResolver().openInputStream(uri);
                        Bitmap bm = BitmapFactory.decodeStream(is);


                        String image_message = ToolSupport.convertBitmapToStringBase64(bm);
                        Message message_new = new Message(conversation_id, user != null ? user.getAccount() : expert.getExpert_id(), image_message, true, false);
                        sendMessage(message_new);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastNew.showToast(getApplication(), "Xuất hiện lỗi nào đó!", Toast.LENGTH_LONG);
                    }
                } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                    if (data.getExtras() == null) return;

                    Bitmap bm = (Bitmap) data.getExtras().get("data");

                    String image_message = ToolSupport.convertBitmapToStringBase64(bm);
                    Message message_new = new Message(conversation_id, user != null ? user.getAccount() : expert.getExpert_id(), image_message, true, false);
                    sendMessage(message_new);
                }

            }
        });

    }

    private Emitter.Listener callback_nhantinnhan = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String message = data.getString("message");
                        Gson gson = new Gson();
                        Message message_new = gson.fromJson(message, Message.class);
                        messageList.add(message_new);
                        mMessageAdapter.notifyDataSetChanged();
                        mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount());
                    } catch (Exception e) {
                        ToastNew.showToast(MessageListActivity.this, "Xuất hiện lỗi nào đó!", Toast.LENGTH_LONG);
                    }
                }
            });
        }
    };


    private Emitter.Listener callback_gioithieu = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (bundle != null) {
                            String tinnhangioithieu = bundle.getString("tinnhangioithieu", "");
                            if (!tinnhangioithieu.isEmpty()) {
                                Message message = new Message(conversation_id, expert.getExpert_id(), tinnhangioithieu, false);
                                messageList.add(message);
                                mMessageAdapter.notifyDataSetChanged();
                                mSocket.emit("client-send-message-to-other-people", message.toJSON());
                                mSocket.off("user-ready-thao-luan");
                            }
                        }
                    } catch (Exception ignored) {
                    }

                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        ask();
    }


    public void ask() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn muốn kết thúc cuộc thảo luận này?")
                    .setCancelable(false)
                    .setPositiveButton("Vâng", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();


                            if (user != null) {
                                Intent intent = new Intent(MessageListActivity.this, RatingForExpertActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Bundle bundle = new Bundle();
                                bundle.putInt("conversation_id", conversation_id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            mSocket.emit("client-roi-cuoc-thao-luan", conversation_id);

                            finish();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
