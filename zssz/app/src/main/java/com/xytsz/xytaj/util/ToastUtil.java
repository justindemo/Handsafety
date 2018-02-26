package com.xytsz.xytaj.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2017/1/3.
 * gonj
 */
public class ToastUtil {
    //吐司
    public static void shortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    //吐司
    public static void longToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
