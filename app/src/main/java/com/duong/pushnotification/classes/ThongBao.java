package com.duong.pushnotification.classes;


import java.io.Serializable;

public class ThongBao implements Serializable {
    public static final String THONG_BAO_THAY_CO = "ThayCo";
    public static final String THONG_BAO_CHUNG = "ThongBaoChung";

    private String ID;
    private String TieuDe;
    private String NoiDung;
    private String Loai;
    private Boolean DaXem;

    public ThongBao(String ID, String tieuDe, String noiDung, String loai, Boolean daXem) {
        this.ID = ID;
        TieuDe = tieuDe;
        NoiDung = noiDung;
        Loai = loai;
        DaXem = daXem;
    }

    public ThongBao() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getLoai() {
        return Loai;
    }

    public void setLoai(String loai) {
        Loai = loai;
    }

    public Boolean getDaXem() {
        return DaXem;
    }

    public void setDaXem(Boolean daXem) {
        DaXem = daXem;
    }

}
