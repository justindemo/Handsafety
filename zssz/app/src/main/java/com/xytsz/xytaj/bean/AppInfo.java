package com.xytsz.xytaj.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by admin on 2017/1/5.
 *
 */
public class AppInfo {
    public String appName;
    public Drawable icon;
    public String packageName;
    public long apkSize;
    public int versionCode ;
    public boolean isSd;
    public String versionName ;
    public boolean isSystemApp;
    //进程变量
    public String prcoessName;
    //进程id
    public int pid;
    //占用的内存
    public int memSize;
    //表示选择中变量
    public boolean isCheck=false;
    //缓存大小
    public long cacheSize;
    //唯一id对应流量
    public int uid;
    public long uploadSize;
    public long downloadSize;
    public String desc=null;





}
