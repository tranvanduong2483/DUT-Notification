package com.duong.anyquestion.classes;

import org.json.JSONException;
import org.json.JSONObject;

//
public class Expert {
    ////
    private String expert_id;
    private String Password;
    private String FullName;
    private String avatar;
    private int education_id;
    private int field_id;
    private String Address;
    private String Email;
    private int money;

    public Expert(String expert_id, String password, String fullName, String avatar, int education_id, int field_id, String address, String email, int money) {
        this.expert_id = expert_id;
        Password = password;
        FullName = fullName;
        this.avatar = avatar;
        this.education_id = education_id;
        this.field_id = field_id;
        Address = address;
        Email = email;
        this.money = money;
    }

    public String getExpert_id() {
        return expert_id;
    }

    public void setExpert_id(String expert_id) {
        this.expert_id = expert_id;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getEducation_id() {
        return education_id;
    }

    public void setEducation_id(int education_id) {
        this.education_id = education_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("expert_id", getExpert_id());
            jsonObject.put("education_id", getEducation_id());
            jsonObject.put("avatar", getAvatar());
            jsonObject.put("field_id", getField_id());
            jsonObject.put("Password", getPassword());
            jsonObject.put("FullName", getFullName());
            jsonObject.put("Address", getAddress());
            jsonObject.put("Email", getEmail());
            jsonObject.put("money", getMoney());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public Expert() {
    }


}
