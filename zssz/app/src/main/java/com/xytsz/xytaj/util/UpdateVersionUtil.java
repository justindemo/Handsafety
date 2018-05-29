package com.xytsz.xytaj.util;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.HomeActivity;
import com.xytsz.xytaj.bean.UpdateStatus;
import com.xytsz.xytaj.bean.VersionInfo;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;


/**
 * Created by admin on 2017/4/6.
 *
 *
 */
public class UpdateVersionUtil {


    private static String versionInfo;
    private static File updateFile;

    public static String getVersionInfo() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getVersionInfoMethodName);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getVersionInfo_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    /**
     * 接口回调
     */
    public interface UpdateListener {
        void onUpdateReturned(int updateStatus, VersionInfo versionInfo);
    }

    public UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }


    /**
     * 检测版本测试
     */
    public static void localCheckedVersion(final Context context, final UpdateListener updateListener, String versionInfo) {

        VersionInfo mVersionInfo = JsonUtil.jsonToBean(versionInfo, VersionInfo.class);
        int clientVersionCode = ApkUtils.getVersionCode(context);
        int serverVersionCode = mVersionInfo.getVersionCode();

        //有新版本
        if (serverVersionCode != 0) {
            if (clientVersionCode < serverVersionCode) {
                int i = NetworkUtil.checkedNetWorkType(context);
                if (i == NetworkUtil.NOWIFI) {
                    updateListener.onUpdateReturned(UpdateStatus.NOWIFI, mVersionInfo);
                } else if (i == NetworkUtil.WIFI) {
                    updateListener.onUpdateReturned(UpdateStatus.YES, mVersionInfo);
                }
            } else {
                //无新本
                updateListener.onUpdateReturned(UpdateStatus.NO, null);
            }
        }
    }


    /**
     * 弹出新版本提示
     *
     * @param context     上下文
     * @param versionInfo 更新内容
     */
    public static void showDialog(final Context context, final VersionInfo versionInfo, final boolean isshow) {
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();

        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);
        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_update_id_cancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        tvContent.setText("更新内容：" + versionInfo.getMessage());
        tvUpdateTile.setText("最新版本：" + versionInfo.getVersionName());


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (isshow) {
                    new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("当前非wifi网络,下载会消耗手机流量!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            showDownloadDialog(context, versionInfo);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                } else {
                    showDownloadDialog(context, versionInfo);
                }


            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private static long initTotal = 0;
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/UpdateVersion/";
    private static File downloadFile;

    private static void showDownloadDialog(final Context context, VersionInfo versionInfo) {

        //新加的

        downloadFile = new File(filePath, "zsaj.apk");
        FileUtils.deletFile(downloadFile);

        if (downloadFile.exists()) {
            downloadFile.delete();
        }


        String url = versionInfo.getDownloadUrl();


        final ProgressDialog dialog = new ProgressDialog(context);
        //下载对话框.
        dialog.setTitle("下载进度");
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//有一个进度可以展示
        dialog.show();

        HttpUtils httpUtils = new HttpUtils(5000);
        //url :地址
        RequestCallBack<File> callback = new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (initTotal == 0) {
                    //第一次下载
                    initTotal = total;
                }

                if (initTotal != total) {
                    //下载暂停
                    total = initTotal;

                }

                int percent = (int) (current * 100f / total + 0.5f);
                dialog.setProgress(percent);

                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                File result = responseInfo.result;

                Intent installIntent = ApkUtils.getInstallIntent(result, context);
                context.startActivity(installIntent);
                dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();

                ToastUtil.shortToast(context, "下载出现错误!");
            }
        };
        httpUtils.download(url, downloadFile.getAbsolutePath(), false, callback);


    }


}
