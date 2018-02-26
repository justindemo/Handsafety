package com.xytsz.xytaj.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.xytsz.xytaj.util.ToastUtil;

/**
 * Created by admin on 2017/1/9.
 */
public class BaidumapReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)){
            ToastUtil.shortToast(context,"网络出错");
        }else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)){
            ToastUtil.shortToast(context,"校验失败");
        }
    }

}
