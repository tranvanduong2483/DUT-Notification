package com.duong.anyquestion.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class ThongTinDangNhap {
    private String username;
    private String password;

    public ThongTinDangNhap(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }
}
