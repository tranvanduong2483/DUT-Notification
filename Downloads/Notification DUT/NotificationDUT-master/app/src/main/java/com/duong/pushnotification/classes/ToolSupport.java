package com.duong.pushnotification.classes;

public class ToolSupport {

    public static int getNumber(String str) {
        try {
            String[] tmp = str.substring(115, 115+10).split("/");
            return Integer.parseInt(tmp[2]+tmp[1] + tmp[0]);
        }catch (Exception e) {
            return -1;
        }
    }
}