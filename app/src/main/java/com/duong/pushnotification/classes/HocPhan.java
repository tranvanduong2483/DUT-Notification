package com.duong.pushnotification.classes;

public class HocPhan {
    private String MaHocPhan;
    private String TenHocPhan;
    private String TenLopHocPhan;
    private String TinChi;
    private String GiangVien;
    private String LichHoc;
    private String TuanHoc;

    public HocPhan(String maHocPhan, String tenHocPhan, String tenLopHocPhan, String tinChi, String giangVien, String lichHoc, String tuanHoc) {
        MaHocPhan = maHocPhan;
        TenHocPhan = tenHocPhan;
        TenLopHocPhan = tenLopHocPhan;
        TinChi = tinChi;
        GiangVien = giangVien;
        LichHoc = lichHoc;
        TuanHoc = tuanHoc;
    }

    public HocPhan() {
    }

    public String getMaHocPhan() {
        return MaHocPhan;
    }

    public void setMaHocPhan(String maHocPhan) {
        MaHocPhan = maHocPhan;
    }

    public String getTenHocPhan() {
        return TenHocPhan;
    }

    public void setTenHocPhan(String tenHocPhan) {
        TenHocPhan = tenHocPhan;
    }

    public String getTenLopHocPhan() {
        return TenLopHocPhan;
    }

    public void setTenLopHocPhan(String tenLopHocPhan) {
        TenLopHocPhan = tenLopHocPhan;
    }

    public String getTinChi() {
        return TinChi;
    }

    public void setTinChi(String tinChi) {
        TinChi = tinChi;
    }

    public String getGiangVien() {
        return GiangVien;
    }

    public void setGiangVien(String giangVien) {
        GiangVien = giangVien;
    }

    public String getLichHoc() {
        return LichHoc;
    }

    public void setLichHoc(String lichHoc) {
        LichHoc = lichHoc;
    }

    public String getTuanHoc() {
        return TuanHoc;
    }

    public void setTuanHoc(String tuanHoc) {
        TuanHoc = tuanHoc;
    }
}
