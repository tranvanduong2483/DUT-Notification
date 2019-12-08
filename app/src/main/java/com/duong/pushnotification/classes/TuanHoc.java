package com.duong.pushnotification.classes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TuanHoc {
    private String Tuan;
    private String ThoiGian;

    public TuanHoc(String tuan, String thoiGian) {
        Tuan = tuan;
        ThoiGian = thoiGian;
    }

    public TuanHoc() {
    }

    public String getTuan() {
        return Tuan;
    }

    public void setTuan(String tuan) {
        Tuan = tuan;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }


    public static int getTuanHocHienTai(ArrayList<TuanHoc> list_tuanhoc) {
        Date date_now = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < list_tuanhoc.size(); i++) {
            try {
                Date date1 = curFormater.parse(list_tuanhoc.get(i).getThoiGian());

                if (i==list_tuanhoc.size()-1){
                    if (date1.compareTo(date_now)<=0)
                        return Integer.parseInt(list_tuanhoc.get(i).Tuan);
                }

                Date date2 = curFormater.parse(list_tuanhoc.get(i+1).getThoiGian());
                if (date1.compareTo(date_now) <=0 && date_now.compareTo(date2) <0)
                    return Integer.parseInt(list_tuanhoc.get(i).Tuan);
            }catch (Exception ignored){ }
        }
        return 0;
    }

    ;
}
