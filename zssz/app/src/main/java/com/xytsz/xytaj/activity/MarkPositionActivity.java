package com.xytsz.xytaj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.Company;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.NativeDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/12/14.
 * <p>
 * 标记地点
 */
public class MarkPositionActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {

    @Bind(R.id.mark_baidumap)
    MapView markBaidumap;
    private double longitude;
    private double latitude;
    private BaiduMap map;
    private String company;
    private View pop;
    private Button mBtNavi;
    private TextView mReportName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent()!= null){
            longitude = getIntent().getDoubleExtra("longitude", -1);
            latitude = getIntent().getDoubleExtra("latitude", -1);
            company = getIntent().getStringExtra("company");
        }
        setContentView(R.layout.activity_markposition);
        ButterKnife.bind(this);

        initActionBar();


        markBaidumap.showScaleControl(false);
        map = markBaidumap.getMap();
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        marke(latitude,longitude);
        locat();
        initPop();

        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pop.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        map.setOnMarkerClickListener(this);

    }

    public static void intent2MarkPosition(Context context, Bundle bundle){
        Intent in = new Intent(context,MarkPositionActivity.class);
        in.putExtra("longitude",bundle.getDouble("longitude"));
        in.putExtra("latitude",bundle.getDouble("latitude"));
        in.putExtra("company",bundle.getString("company"));
        context.startActivity(in);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("公司位置");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void marke(double latitude, double longitude) {

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<>();
        bitmaps.add(BitmapDescriptorFactory.fromResource(R.mipmap.icon_twinkle));
        bitmaps.add(bitmapDescriptor);


        LatLng latLng = new LatLng(latitude,longitude);
        map.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

        MarkerOptions options =new MarkerOptions();
        options.position(latLng).icons(bitmaps);
        map.clear();
        map.addOverlay(options);



    }



    @Override
    protected void onResume() {
        super.onResume();
        markBaidumap.onResume();
        if (locationClient != null){
            locationClient.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        markBaidumap.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationClient !=null){
            locationClient.stop();
        }
    }

    public BDAbstractLocationListener myListener = new MyListener();
    private LocationClient locationClient;

    private void locat() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);
        locationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markBaidumap.onDestroy();
        if (locationClient !=null){
            locationClient.stop();
            locationClient.registerLocationListener(myListener);
            locationClient = null;
        }
    }

    private void initPop() {
        pop = View.inflate(this, R.layout.pop, null);
        mReportName = (TextView) pop.findViewById(R.id.tv_pop_title);

        mBtNavi = (Button) pop.findViewById(R.id.bt_navi);


        pop.setVisibility(View.INVISIBLE);

        LatLng latLng = new LatLng(latitude, longitude);


        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(latLng)// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .build();
        markBaidumap.addView(pop, param);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mReportName.setText(company);

        mBtNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到百度地图导航
                //病害位置
                LatLng latLng = new LatLng(latitude, longitude);
                //定位当前的信息
                /**
                 * 导航
                 */

                LatLng  latlngNow = new LatLng(mylatitude, mylongitude);
                NativeDialog msgDialog = new NativeDialog(v.getContext(), latlngNow, latLng);
                msgDialog.show();

                pop.setVisibility(View.INVISIBLE);

            }
        });


        pop.setVisibility(View.VISIBLE);
        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(marker.getPosition())// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-10)
                .build();
        markBaidumap.updateViewLayout(pop, param);

        return true;
    }
    private double mylongitude;
    private double mylatitude;

    private class MyListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取到经度
            mylongitude = bdLocation.getLongitude();
            Log.i("精度", mylongitude + "");
            //获取到纬度
            mylatitude = bdLocation.getLatitude();
            //经纬度  填的是纬度，经度

        }

    }

}
