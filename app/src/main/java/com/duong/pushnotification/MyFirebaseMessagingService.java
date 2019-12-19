package com.duong.pushnotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.duong.pushnotification.classes.ThongBao;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }






    @Override
    public void onNewToken(String token) {
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
        Log.d("Token", token);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM","Có nhận thông báo");
        setPushNotification(remoteMessage.getData());
    }



    private void setPushNotification(Map data) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        ThongBao TB = new ThongBao(
                data.get("ID") + "",
                data.get("TieuDe") + "",
                data.get("NoiDung") + "",
                data.get("Loai") + "",
                Boolean.parseBoolean(data.get("DaXem") + "")
        );

        String from = TB.getLoai().equals(ThongBao.THONG_BAO_THAY_CO) ? "Thông báo từ thầy cô" : "Thông báo chung";
        String Channel = TB.getLoai().equals(ThongBao.THONG_BAO_CHUNG) ? App.CHANNEL_ThayCo_ID : App.CHANNEL_Truong_ID;

        Intent intent = new Intent(this, DetailNotificationActivity.class);
        intent.putExtra("ThongBao", TB);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, Channel)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(from +"")
                .setContentText((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        ? Html.fromHtml(TB.getTieuDe(), Html.FROM_HTML_MODE_COMPACT)
                        : Html.fromHtml(TB.getTieuDe())) + "")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(TB.getTieuDe(), 1, notification);

        Log.d("FCM","End");

    }



}
