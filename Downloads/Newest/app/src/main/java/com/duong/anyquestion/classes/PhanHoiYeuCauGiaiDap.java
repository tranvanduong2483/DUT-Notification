package com.duong.anyquestion.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class PhanHoiYeuCauGiaiDap {
    int question_id;
    String from;
    int cost;
    boolean agree;

    public PhanHoiYeuCauGiaiDap(int question_id, String from, int cost, boolean agree) {
        this.question_id = question_id;
        this.from = from;
        this.cost = cost;
        this.agree = agree;
    }

    public PhanHoiYeuCauGiaiDap() {
    }


    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("question_id", question_id);
            jsonObject.put("from", from);
            jsonObject.put("cost", cost);
            jsonObject.put("agree", agree);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


}
