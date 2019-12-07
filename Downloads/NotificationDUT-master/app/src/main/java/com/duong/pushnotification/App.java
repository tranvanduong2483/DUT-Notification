package com.duong.pushnotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    public static final String CHANNEL_ThayCo_ID="ThongBaoTuThayCo";
    public static final String CHANNEL_Truong_ID="ThongBaoChung";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1= new NotificationChannel(
                    CHANNEL_ThayCo_ID,"Thông báo từ thầy cô",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Đây là thông báo từ thầy cô về lớp học phần mà bạn đang học");

            NotificationChannel channel2= new NotificationChannel(
                    CHANNEL_Truong_ID,"Thông báo chung",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel2.setDescription("Đây là thông báo chung từ trang sv.dut.udn.vn");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel1);
                manager.createNotificationChannel(channel2);
            }


        }
    }
}
