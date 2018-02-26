package com.xytsz.xytaj.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.xytsz.xytaj.bean.AppInfo;

/**
 * Created by admin on 2017/1/5.
 *
 */
public class IntentUtil {
    /**开启 界面
     * */

    public static void startActivity(Context context, Class<?> activity) {
        Intent intent=new Intent(context,activity);
        context.startActivity(intent);
    }


    /**开启服务
     */
    public static void startService(Context context, Class service) {
        Intent intent=new Intent(context,service);
        context.startService(intent);
    }

    /*** 方法

     */
    public static void startAppDetails(Context context, String packageName) {
        // <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS"/>
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="package" />
        Intent intent = new Intent();
        // 设置动作
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        // 设置数据变量
        intent.setData(Uri.parse("package:" + packageName));
        // 设置类别
        intent.addCategory("android.intent.category.DEFAULT");
        // 打开
        context.startActivity(intent);
    }

    /*** 方法 分享
     @param info
     */
    public static void shareInfo(Context context, AppInfo info) {
        // <action android:name="android.intent.action.SEND" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        //给系统应用传递变量一般都使用Intent.EXTRAxxx
        intent.putExtra(Intent.EXTRA_TEXT, "分享福利:\n http://www.baidu.com/appstore?packagename="+info.packageName);
        // 设置数据类型
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    /*** 方法
     获取app管理
     @param packageName
     */
    public static void startMainIntent(Context context, String packageName) {
        // 使用pm去解析指定包名的应用mainIntent
        PackageManager pm = context.getPackageManager();
        // 获指定包名的这个应用的mainIntent
        Intent mainIntent = pm.getLaunchIntentForPackage(packageName);
        // 打包
        if (mainIntent == null) {
            ToastUtil.shortToast(context, "打开失败");
        } else {
            context.startActivity(mainIntent);
        }
    }

    /*** 方法
     导入安装界面
     @param packageName
     */
    public static void startUninstallActivity(Context context, String packageName) {

//		<intent-filter>
//        <action android:name="android.intent.action.DELETE" />
//        <action android:name="android.intent.action.UNINSTALL_PACKAGE" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <data android:scheme="package" />
//    </intent-filter>
        Intent intent =new Intent();
        //把传入的action保存给一个mAction变量，如果再调用一次就覆盖
        intent.setAction("android.intent.action.UNINSTALL_PACKAGE");
        //把category传入add内部是添加进集合mCategories.
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:"+packageName));
        context.startActivity(intent);
    }

    /*** 方法
     取消注册广播
     */
    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    /*** 方法
     添加拦截包移除的广播
     */
    public static void registerPackageMoveReceiver(Context context, BroadcastReceiver receiver) {
        //注册代码
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);//添加拦截包移除的广播
        //当前广播具有特殊性  http:// content://  如果不注册当前的这个scheme协议头那么获取不到广播
        filter.addDataScheme("package");
        context.registerReceiver(receiver, filter);
    }

    /*** 方法

     */
    public static void registerScreenOnOFFReceiver(Context context, BroadcastReceiver receiver) {
        //注册
        IntentFilter intentFilter = new IntentFilter();
        //添加需要接收的广播信号
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);//不显示
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);//显示
        //注册
        context.registerReceiver(receiver, intentFilter);
    }

    /*** 方法

     */
    public static void startLauncherHome(Context context) {
//		  <action android:name="android.intent.action.MAIN" />
//          <category android:name="android.intent.category.HOME" />
//          <category android:name="android.intent.category.DEFAULT" />
//          <category android:name="android.intent.category.MONKEY"/>
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        context.startActivity(intent);
    }


}
