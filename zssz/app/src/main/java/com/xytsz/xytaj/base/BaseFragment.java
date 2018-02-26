package com.xytsz.xytaj.base;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2017/1/4.
 *
 */
public abstract class  BaseFragment extends Fragment {


    // 加载fragment的布局文件,显示界面
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mRootView = initView();
        return mRootView;
    }

    /**
     * fragment加载显示数据,类似于activity的oncreate方法
     * @param savedInstanceState ：ss
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract View initView();
    public abstract void initData();

}
