package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;

import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.MyDrivingRouteOverlay;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/6/30.
 * 道路长度的具体信息
 */
public class RoadMapActivity extends AppCompatActivity implements OnGetRoutePlanResultListener {
    @Bind(R.id.roadmap_mv)
    MapView roadmapMv;

    private LatLng starpoint = new LatLng(39.768096,116.282378);
    private LatLng midpoint = new LatLng(39.769049,116.29914);
    private LatLng endpoint = new LatLng(39.769385,116.315795);
    private RoutePlanSearch mSearch;
    private BaiduMap baiduMap;
    private TextView mTvTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);
        ButterKnife.bind(this);

        roadmapMv.showScaleControl(false);
        roadmapMv.showZoomControls(true);
        baiduMap = roadmapMv.getMap();
        //显示当前道路的长短,根据经纬度

        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(midpoint));
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        startSearchRoad(starpoint,endpoint);
        //填充mark
        View view = LayoutInflater.from(this).inflate(R.layout.pop,null,false);
        mTvTitle = (TextView) view.findViewById(R.id.title);
        mTvTitle.setText("共"+"3"+"公里");
        BitmapDescriptor markPop = BitmapDescriptorFactory.fromView(view);
        MarkerOptions options = new MarkerOptions().position(midpoint).icon(markPop);
        baiduMap.addOverlay(options);




    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }
    //驾车路线
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            Log.d("baiduMap", "起终点或途经点地址有岐义");
            return;
        }
        if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED) {
            //权限鉴定未完成则再次尝试
            Log.d("baiduMap", "权限鉴定未完成,再次尝试");

            //return;
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            MyDrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
            //baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }

    }

    private void startSearchRoad(LatLng startpoint, LatLng endpoint) {
        PlanNode stNode = PlanNode.withLocation(startpoint);
        PlanNode enNode = PlanNode.withLocation(endpoint);
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    @Override
    protected void onPause() {
        roadmapMv.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        roadmapMv.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        roadmapMv.onDestroy();
        super.onDestroy();
        mSearch.destroy();
    }

}
