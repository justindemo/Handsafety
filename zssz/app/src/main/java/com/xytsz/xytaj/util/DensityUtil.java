package com.xytsz.xytaj.util;

import android.content.Context;

public class DensityUtil {
	 /** 
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)  2 == To
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  //获取手机的屏幕的密度
        return (int) (dpValue * scale + 0.5f); // 四舍五入    3.2 3   3.8 -> 3  3.8+0.5=4.3 -> 4
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
}
