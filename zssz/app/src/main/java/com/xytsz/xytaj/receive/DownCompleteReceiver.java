package com.xytsz.xytaj.receive;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.xytsz.xytaj.util.ApkUtils;
import com.xytsz.xytaj.util.ToastUtil;

import java.io.File;

/**
 * Created by admin on 2018/5/22.
 *
 *
 */
public class DownCompleteReceiver extends BroadcastReceiver {
    private long downloadId;
    private File downFile;

    public DownCompleteReceiver(long downloadId,File downFile) {
        this.downloadId = downloadId;
        this.downFile = downFile;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        long id = intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId != id) {
            return;
        }

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor c = dm.query(query);

        if (c != null && c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            // 下载失败也会返回这个广播，所以要判断下是否真的下载成功
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                ApkUtils.getInstallIntent(downFile,context);
            }else if(DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
                    ToastUtil.shortToast(context,"下载失败");
                }else if (DownloadManager.ERROR_UNKNOWN == c.getInt(columnIndex)){
                ToastUtil.shortToast(context,"下载出错");
            }

        }
    }
}
