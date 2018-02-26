package com.xytsz.xytaj.bean;


import android.graphics.Color;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.xytsz.xytaj.R;

/**
 * Created by admin on 2017/6/30.
 *  线路辅助类
 */
public class MyDrivingRouteOverlay extends DrivingRouteOverlay {


    /**
     * 构造函数
     *
     * @param baiduMap 该DrivingRouteOvelray引用的 BaiduMap
     */
    public MyDrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }


    @Override
    public int getLineColor() {
        return Color.BLUE;
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
    }


}
