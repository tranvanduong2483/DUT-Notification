package com.duong.anyquestion.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class Question {
    int id;
    String tittle;
    int field_id;
    String imageString;
    String note;
    int money;
    String from;

    public Question(int id, String tittle, int field_id, String imageString, String note, int money, String from) {
        this.id = id;
        this.tittle = tittle;
        this.field_id = field_id;
        this.imageString = imageString;
        this.note = note;
        this.money = money;
        this.from = from;
    }

    public Question() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("tittle", tittle);
            jsonObject.put("field_id", field_id);
            jsonObject.put("imageString", imageString);
            jsonObject.put("note", note);
            jsonObject.put("money", money);
            jsonObject.put("from", from);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}


