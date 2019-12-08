package com.duong.pushnotification.classes;

public class SinhVien {
    private String MaSinhVien;
    private String Ten;
    private String Khoa;
    private String Lop;
    private String Email;
    private String TC;
    private String T4;

    public SinhVien(String maSinhVien, String ten, String khoa, String lop, String email, String TC, String t4) {
        MaSinhVien = maSinhVien;
        Ten = ten;
        Khoa = khoa;
        Lop = lop;
        Email = email;
        this.TC = TC;
        T4 = t4;
    }

    public SinhVien() {
    }

    public String getMaSinhVien() {
        return MaSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        MaSinhVien = maSinhVien;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getKhoa() {
        return Khoa;
    }

    public void setKhoa(String khoa) {
        Khoa = khoa;
    }

    public String getLop() {
        return Lop;
    }

    public void setLop(String lop) {
        Lop = lop;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getT4() {
        return T4;
    }

    public void setT4(String t4) {
        T4 = t4;
    }
}
