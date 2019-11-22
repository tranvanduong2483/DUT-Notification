package com.duong.pushnotification;


import java.io.Serializable;

public class Notification implements Serializable {
    public static final String THONG_BAO_THAY_CO = "ThayCo";
    public static final String THONG_BAO_CHUNG = "ThongBaoChung";

    public String id;
    public String type;
    public String title;
    public String content;
    public Boolean hasSeen;

    public Notification() {
    }

    public Notification(String id, String type, String title, String content, Boolean hasSeen) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.hasSeen = hasSeen;
    }
}
