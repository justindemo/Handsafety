package com.xytsz.xytaj.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;


public class ApkUtils {
	private static final String TAG = ApkUtils.class.getSimpleName();

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本名称信息
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {

		}
		return null;
	}

	/**
	 * @return 当前程序的版本号
	 */
	public static int getVersionCode(Context context) {
		int version;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			version = 0;
		}
		return version;
	}

	/**
	 * 得到安装的intent
	 * @param apkFile
	 * @return
	 */
	public static Intent getInstallIntent(File apkFile,Context context) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			File file = new File(Environment.getExternalStorageDirectory(), "zssz_app.apk");
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
					context.getApplicationContext().getPackageName() + ".fileprovider",
					apkFile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(apkFile),
					"application/vnd.android.package-archive");
		}
		return intent;
	}
	

}
