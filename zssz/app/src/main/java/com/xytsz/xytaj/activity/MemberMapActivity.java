package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.xytsz.xytaj.util.NativeDialog;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/8/3.
 * 会员位置
 */
public class MemberMapActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {

    @Bind(R.id.mp_membermap)
    MapView mpMembermap;

    private LatLng latLng = new LatLng(39.768193,116.356317);
    private View pop;
    private TextView tvName;
    private Button btGeo;
    private BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mylatitude = bdLocation.getLatitude();
            mylongitude = bdLocation.getLongitude();

        }
    };
    private double mylatitude;
    private double mylongitude;
    private LocationClient locationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membermap);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            //获取经纬度
            Serializable latlngs = getIntent().getSerializableExtra("latlng");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("会员位置");

        }

        initData();


    }

    private void initData() {

        mpMembermap.showScaleControl(false);
        mpMembermap.showZoomControls(true);


        BaiduMap map = mpMembermap.getMap();
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        map.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
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

        locat();

        //mark标记物

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_geo);

        MarkerOptions options = new MarkerOptions();
        options.position(latLng).icon(bitmapDescriptor);

        map.clear();
        map.addOverlay(options);

        map.setOnMarkerClickListener(this);

        initPop();
    }

    private void locat() {
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setIgnoreKillProcess(false);
        option.setIsNeedLocationDescribe(false);
        option.setLocationNotify(false);

        locationClient.setLocOption(option);
        locationClient.start();

    }

    private void initPop() {
        pop = View.inflate(this, R.layout.membermaker_pop, null);
        tvName = (TextView) pop.findViewById(R.id.membermap_tv_name);
        btGeo = (Button) pop.findViewById(R.id.membermap_btn_geo);
        //刚开始开不见
        pop.setVisibility(View.INVISIBLE);

        MapViewLayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
                .position(latLng)
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .build();

        mpMembermap.addView(pop,params);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (locationClient != null){
            locationClient.start();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mpMembermap.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mpMembermap.onPause();
        if (locationClient != null){
            locationClient.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpMembermap.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        pop.setVisibility(View.VISIBLE);
        //设置名称
        //tvName.setText("");
        btGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导航
                LatLng startLatLng = new LatLng(mylatitude,mylongitude);
                NativeDialog msgDialog = new NativeDialog(v.getContext(), startLatLng, latLng);
                msgDialog.show();

                pop.setVisibility(View.INVISIBLE);
            }
        });

        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(marker.getPosition())// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-10)
                .build();
        mpMembermap.updateViewLayout(pop, param);

        return true;
    }
}

