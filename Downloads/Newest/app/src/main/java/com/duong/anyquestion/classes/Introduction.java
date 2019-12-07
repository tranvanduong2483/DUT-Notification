package com.duong.anyquestion.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Introduction {
    int introduction_id;
    String expert_id;
    String keywords;
    String introduction_message;

    public Introduction(int introduction_id, String expert_id, String keywords, String introduction_message) {
        this.introduction_id = introduction_id;
        this.expert_id = expert_id;
        this.keywords = keywords;
        this.introduction_message = introduction_message;
    }

    public Introduction(String expert_id, String keywords, String introduction_message) {
        this.expert_id = expert_id;
        this.keywords = keywords;
        this.introduction_message = introduction_message;
    }

    public Introduction() {

    }

    public int getIntroduction_id() {
        return introduction_id;
    }

    public void setIntroduction_id(int introduction_id) {
        this.introduction_id = introduction_id;
    }

    public String getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(String expert_id) {
        this.expert_id = expert_id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getIntroduction_message() {
        return introduction_message;
    }

    public void setIntroduction_message(String introduction_message) {
        this.introduction_message = introduction_message;
    }


    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("introduction_id", getIntroduction_id());
            jsonObject.put("expert_id", getExpert_id());
            jsonObject.put("keywords", getKeywords());
            jsonObject.put("introduction_message", getIntroduction_message());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }
}
