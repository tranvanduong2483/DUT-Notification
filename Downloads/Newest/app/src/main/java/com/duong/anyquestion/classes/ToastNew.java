package com.duong.anyquestion.classes;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastNew {
    public static Toast m_currentToast;
    public static void showToast(Activity context, String text, int time)
    {
        if(m_currentToast != null)
            m_currentToast.cancel();
        m_currentToast = Toast.makeText(context, text, time);
        m_currentToast.show();
    }

    public static void showToast(Context context, String text, int time)
    {
        if(m_currentToast != null)
            m_currentToast.cancel();
        m_currentToast = Toast.makeText(context.getApplicationContext(), text, time);
        m_currentToast.show();
    }


}
