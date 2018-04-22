package com.xytsz.xytaj.util;

import android.app.Fragment;

import com.xytsz.xytaj.fragment.ReadFragment;

/**
 * Created by admin on 2018/3/21.
 *
 */
public class CallBackUtil {
    public static ReadFragment.OnReadFragmentListener monReadFragmentListener;
    public static void setReadFragementListener(ReadFragment.OnReadFragmentListener onReadFragmentListener){
        monReadFragmentListener = onReadFragmentListener;
    }
    public static void doNext(int position){
        monReadFragmentListener.onNext(position);
    }
}
