package com.duong.pushnotification;

public class HocPhan {
    private String MaHocPhan;
    private String TenHocPhan;
    private String TenLopHocPhan;
    private String LichHoc;
    private String TuanHoc;

    public HocPhan(String maHocPhan, String tenHocPhan, String tenLopHocPhan, String lichHoc, String tuanHoc) {
        MaHocPhan = maHocPhan;
        TenHocPhan = tenHocPhan;
        TenLopHocPhan = tenLopHocPhan;
        LichHoc = lichHoc;
        TuanHoc = tuanHoc;
    }

    public void setMaHocPhan(String maHocPhan) {
        MaHocPhan = maHocPhan;
    }

    public void setTenHocPhan(String tenHocPhan) {
        TenHocPhan = tenHocPhan;
    }

    public void setTenLopHocPhan(String tenLopHocPhan) {
        TenLopHocPhan = tenLopHocPhan;
    }

    public void setLichHoc(String lichHoc) {
        LichHoc = lichHoc;
    }

    public void setTuanHoc(String tuanHoc) {
        TuanHoc = tuanHoc;
    }

    public String getMaHocPhan() {
        return MaHocPhan;
    }

    public String getTenHocPhan() {
        return TenHocPhan;
    }

    public String getTenLopHocPhan() {
        return TenLopHocPhan;
    }

    public String getLichHoc() {
        return LichHoc;
    }

    public String getTuanHoc() {
        return TuanHoc;
    }
}
