package com.xytsz.xytaj.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by admin on 2017/1/16.
 *
 */
public class VersionUtil {

    /*
    * 获取版本信息
    * */
    public static String getVersionName(Context context) {
        //工具PackageManager:解析功能清单的xml工具
        PackageManager pm=context.getPackageManager();
        //获取版本
        //String packageName="com.itheima.mobilesafe91";
        String packageName=context.getPackageName();
        int flags = 0;//默认值
        try {
            //PackageInfo:javaBean对象存解析出来的所有变量
            PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
            //获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*** 方法
     @param
     @return
     */
    public static int getVersionCode(Context context) {
        //工具PackageManager:解析功能清单的xml工具
        PackageManager pm=context.getPackageManager();
        //获取版本
        //String packageName="com.itheima.mobilesafe91";
        String packageName=context.getPackageName();
        int flags = 0;//默认值
        try {
            //PackageInfo:javaBean对象存解析出来的所有变量
            PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
            //获取版本号:是一个自然数,每一次更新都+1
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
