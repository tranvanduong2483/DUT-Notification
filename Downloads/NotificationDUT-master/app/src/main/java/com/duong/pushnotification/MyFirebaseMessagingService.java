package com.duong.pushnotification;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public MyFirebaseMessagingService() {
    }


    @Override
    public void onNewToken(String token) {
        // Log.d("Test", "Refreshed token: " + token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
        //sendRegistrationToServer(token);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }


    //private void sendRegistrationToServer(String token){

    //}
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.d("Test", "From: " + remoteMessage.getFrom());
        sendNotification(remoteMessage.getData());
    }


    private void SaveDatabase(Notification notification) {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String MSSV = user.get(SessionManager.MSSV);
        if (MSSV ==null) return;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mData = database.getReference();
        mData.child(MSSV).child(notification.type).child(notification.id).setValue(notification);
    }

    private void sendNotification(Map data) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String id = data.get("id") + "";
        String type = data.get("type") + "";
        String title = data.get("title") + "";
        String content = data.get("content") + "";
        Boolean hasSeen = Boolean.parseBoolean(data.get("hasSeen") +"");

        String from = type.equals(Notification.THONG_BAO_THAY_CO) ? "Thông báo từ thầy cô" : "Thông báo chung";
        String Channel = type.equals(Notification.THONG_BAO_CHUNG) ? App.CHANNEL_ThayCo_ID : App.CHANNEL_Truong_ID;

        Intent intent = new Intent(this, DetailNotificationActivity.class);
        Notification notification_from_server = new Notification(id, type, title, content, hasSeen);
        intent.putExtra("Notification", notification_from_server);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SaveDatabase(notification_from_server);


        String conten_notification_push;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conten_notification_push = Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT) +"";
        } else {
            conten_notification_push = Html.fromHtml(title) + "";
        }

        android.app.Notification notification = new NotificationCompat.Builder(this, Channel)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setContentTitle(from)
                .setContentText(conten_notification_push)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(id,1, notification);
    }
}
