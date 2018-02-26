package com.xytsz.xytaj.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytaj.bean.AppInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2017/2/14.
 */
public class APPUtil {
    public static String paks = new String("com.baidu.BaiduMap");


    public static void startNative_Baidu(Context context, LatLng loc1, LatLng loc2) {
        if (loc1==null || loc2==null) {
            return;
        }
        if(isAvilible(context,"com.baidu.BaiduMap")){
            try {
                Intent intent = new Intent();
                // 骑行导航
                intent = Intent.getIntent("intent://map/direction?" +
                        "origin=latlng:"+loc1.latitude+","+loc1.longitude+"|name:我自己&" +   //起点  此处不传值默认选择当前位置
                        "destination=latlng:"+loc2.latitude+","+loc2.longitude+"|name:我的目的地"+        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region=北京" +           //
                        "&src=掌上市政#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            return;
        }



    }
    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        packageManager.getInstalledApplications(packageManager.GET_META_DATA);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 通过包名获取应用信息
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoByPak(Context context, String packageName){
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageName.equals(packageInfo.packageName)) {
                AppInfo tmpInfo =new AppInfo();
                tmpInfo.appName = (packageInfo.applicationInfo.loadLabel(packageManager).toString());
                tmpInfo.packageName=(packageInfo.packageName);
                tmpInfo.versionName= (packageInfo.versionName);
                tmpInfo.versionCode = (packageInfo.versionCode);
                tmpInfo.icon = (packageInfo.applicationInfo.loadIcon(packageManager));
                return tmpInfo;
            }
        }
        return null;
    }

    /**
     * 返回当前设备上的地图应用集合
     * @param context
     * @return
     */
    public static List<AppInfo> getMapApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();
            AppInfo appinfo = getAppInfoByPak(context,paks);
            if (appinfo!=null) {
                apps.add(appinfo);

        }
        return apps;
    }

    /**
     * 获取应用中所有浏览器集合
     * @param context
     * @return
     */
    public static List<AppInfo> getWebApps(Context context){
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();

        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";

        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");
        intent.setDataAndType(uri, null);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_SHARED_LIBRARY_FILES);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo tmpInfo =new AppInfo();
            tmpInfo.appName = (resolveInfo.loadLabel(packageManager).toString());
            tmpInfo.icon= (resolveInfo.loadIcon(packageManager));
            tmpInfo.packageName =(resolveInfo.activityInfo.packageName);
            apps.add(tmpInfo);
        }

        return apps;
    }
}
