package com.duong.anyquestion.classes;

import android.accounts.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class User {


    String user_id;
    String Password;
    String FullName;
    String avatar;
    String Address;
    String Email;
    int money;


    public User(String account, String fullName) {
        user_id = account;
        FullName = fullName;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setAccount(String account) {
        user_id = account;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAccount() {
        return user_id;
    }

    public String getPassword() {
        return Password;
    }

    public String getFullName() {
        return FullName;
    }

    public String getAddress() {
        return Address;
    }

    public String getEmail() {
        return Email;
    }

    public User(String account, String password, String fullName, String address, String email) {
        user_id = account;
        Password = password;
        FullName = fullName;
        Address = address;
        Email = email;
    }


    public User() {
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", getAccount());
            jsonObject.put("Password", getPassword());
            jsonObject.put("FullName", getFullName());
            jsonObject.put("Address", getAddress());
            jsonObject.put("Email", getEmail());
            jsonObject.put("money", getMoney());
            jsonObject.put("avatar", getAvatar());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }
}
