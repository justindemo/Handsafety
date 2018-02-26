package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytaj.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/12/14.
 * <p>
 * 标记地点
 */
public class MarkPositionActivity extends AppCompatActivity {

    @Bind(R.id.mark_baidumap)
    MapView markBaidumap;
    private double longitude;
    private double latitude;
    private BaiduMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent()!= null){
            longitude = getIntent().getDoubleExtra("longitude", -1);
            latitude = getIntent().getDoubleExtra("latitude", -1);
        }
        setContentView(R.layout.activity_markposition);
        ButterKnife.bind(this);

        initActionBar();


        markBaidumap.showScaleControl(false);
        map = markBaidumap.getMap();
        map.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
        marke(latitude,longitude);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("病害位置");
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
        markBaidumap.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        markBaidumap.onPause();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        markBaidumap.onDestroy();
        super.onDestroy();
    }
}
