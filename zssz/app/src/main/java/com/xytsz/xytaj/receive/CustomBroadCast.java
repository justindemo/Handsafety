package com.xytsz.xytaj.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/18.
 *
 *  自定义的广播
 */
public class CustomBroadCast extends BroadcastReceiver {

    /** 自定义的Action */
    public static String CUSTOM_BROAD_CAST_ACTION = "com.xytsz.broadcast_user.CUSTOMBROADCAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == CUSTOM_BROAD_CAST_ACTION){
            //如果是当前这个action 就去接收消息
            if (context instanceof OnCustomBroadCastListener && mOnCustomBroadCastListener.contains(context)){
                ((OnCustomBroadCastListener)context).onIntentListener(intent);
            }
        }
    }


    /** 私有化构造方法，实现单例模式 */
    private CustomBroadCast() {
    }

    /** 自定义的广播对象 */
    private static CustomBroadCast mCustomBroadCast;
    /** 自定义的Context集合，用于注册和取消广播 */
    private List<Context> contextList = new ArrayList<Context>();
    /** 保存接口数据 */
    private List<OnCustomBroadCastListener> mOnCustomBroadCastListener = new ArrayList<OnCustomBroadCastListener>();


    public static CustomBroadCast getInstance(){
        if (mCustomBroadCast == null){
            synchronized (CustomBroadCast.class){
                if (mCustomBroadCast == null){
                    mCustomBroadCast = new CustomBroadCast();
                }
            }
        }
        return mCustomBroadCast;
    }

    /**
     * 注册广播
     *
     * @param context
     */
    public CustomBroadCast registerAction(Context context) {
        if (contextList.size() > 0 && !contextList.contains(context)
                || contextList.size() <= 0) {
            contextList.add(context);
            IntentFilter filter = new IntentFilter();
            filter.addAction(CUSTOM_BROAD_CAST_ACTION);
            context.registerReceiver(mCustomBroadCast, filter);
            // 如果实现了接口,这里才去注册接口
            if (context instanceof OnCustomBroadCastListener) {
                registerOnCustomBroadCastListener((OnCustomBroadCastListener) context);
            }
        }
        return mCustomBroadCast;
    }

    /**
     * 注销广播
     *
     * @param context
     */
    public void unRegister(Context context) {
        if (contextList.contains(context)) {
            context.unregisterReceiver(mCustomBroadCast);
            contextList.remove(context);
            if (context instanceof OnCustomBroadCastListener) {
                mOnCustomBroadCastListener.remove(context);
            }
        }
    }

    /**
     * 注册接口监听
     * @param listener
     */
    public void  registerOnCustomBroadCastListener(OnCustomBroadCastListener listener){
        mOnCustomBroadCastListener.add(listener);

    }


    public interface OnCustomBroadCastListener {

        public void onIntentListener(Intent intent);

    }
}
