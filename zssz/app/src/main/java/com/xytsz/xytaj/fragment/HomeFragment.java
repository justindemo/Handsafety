package com.xytsz.xytaj.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import com.google.zxing.activity.CaptureActivity;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.CheckRoadActivity;
import com.xytsz.xytaj.activity.DealActivity;

import com.xytsz.xytaj.activity.PostRoadActivity;
import com.xytsz.xytaj.activity.ReportActivity;
import com.xytsz.xytaj.activity.RoadActivity;
import com.xytsz.xytaj.activity.SendRoadActivity;
import com.xytsz.xytaj.base.BaseFragment;

import com.xytsz.xytaj.global.GlobalContanstant;

import com.xytsz.xytaj.net.NetUrl;

import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/4.
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private static final int FAIL = 404;
    private static final int MANAGER = 111120;
    private static final int SIMPLER = 111122;
    private static final int REQUEST_CODE = 0x01;
    private int RESULT_OK = 0xA1;
    private MapView mapview;
    private BaiduMap map;
    private View mllReport;
    private View mllReview;
    private View mllDeal;
    private View mllSend;
    private View mllUncheck;
    private View mllCheck;
    private TextView mtvdealNumber;
    private TextView mtvreviewNumber;
    private TextView mtvsendNumber;
    private TextView mtvuncheckNumber;
    private TextView mtvcheckNumber;
    private LocationClient locationClient;
    private double longitude;
    private double latitude;
    public BDAbstractLocationListener myListener = new MyListener();
    private View.OnClickListener listener = new MyListener();
    private int role;
    private PermissionUtils.PermissionGrant mPermissionGrant
            = new PermissionUtils.PermissionGrant() {

        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    locat();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
            }
        }
    };


    private String noData;


    private TextView mActionbartext;
    private TextView mtvPatrolNumber;
    private ProgressBar testProgress;


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        mActionbartext = (TextView) view.findViewById(R.id.actionbar_text);

        mapview = (MapView) view.findViewById(R.id.home_mv);
        mllReport = view.findViewById(R.id.ll_home_report);
        mllReview = view.findViewById(R.id.ll_home_review);
        mllDeal = view.findViewById(R.id.ll_home_deal);
        mllSend = view.findViewById(R.id.ll_home_send);
        mllUncheck = view.findViewById(R.id.ll_home_uncheck);
        mllCheck = view.findViewById(R.id.ll_home_check);

        mtvdealNumber = (TextView) view.findViewById(R.id.tv_home_deal_number);
        mtvreviewNumber = (TextView) view.findViewById(R.id.tv_home_review_number);
        mtvsendNumber = (TextView) view.findViewById(R.id.tv_home_send_number);
        mtvuncheckNumber = (TextView) view.findViewById(R.id.tv_home_unchecek_number);
        mtvcheckNumber = (TextView) view.findViewById(R.id.tv_home_check_number);
        mtvPatrolNumber = (TextView) view.findViewById(R.id.tv_home_patrol_number);
        testProgress = (ProgressBar) view.findViewById(R.id.testprogress);

        return view;
    }

    private int personId;

    @Override
    public void initData() {

        noData = getString(R.string.table_nodata);

        mActionbartext.setText(R.string.app_name);

        //获取当前登陆人的ID
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
//        getData();

        //是否显示缩放按钮
        mapview.showZoomControls(false);
        //是否显示地图标尺
        mapview.showScaleControl(false);
        map = mapview.getMap();


        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        //1,纬度double latitude  2，经度longitude  116.347005,39.812991
        mllCheck.setOnClickListener(listener);
        mllReport.setOnClickListener(listener);
        mllUncheck.setOnClickListener(listener);
        mllDeal.setOnClickListener(listener);
        mllSend.setOnClickListener(listener);
        mllReview.setOnClickListener(listener);


    }

    private List<HeaderProperty> headerList = new ArrayList<>();

    private void getData() {

        testProgress.setVisibility(View.VISIBLE);

        headerList.clear();
        if (HomeFragment.this.getActivity() != null) {
            HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                    SpUtils.getString(HomeFragment.this.getActivity(), GlobalContanstant.CookieHeader));

            headerList.add(headerPropertyObj);
        }


        new Thread() {
            @Override
            public void run() {
                try {
                    // 做判断 根据权限。
                    //上报没有
                    // 如果是管理者，
                    // 1.是否是下派者领导。
                    //2.显示下边三行
                    // 如果是处置者就显示第一行，
                    // 如果是下派领导，显示下拍和验收  先不做处理

                    String patrolcount = getManageData(NetUrl.getMytaskmethod, personId);
                    String dealcount = getManageData(NetUrl.getDealcountMethod, personId);
                    String examinecount = getManageData(NetUrl.getRepeatmethod, personId);
                    String reviewcount = getManageData(NetUrl.getreviewcountmethod, personId);
                    String postcount = getManageData(NetUrl.getpostcountmethod, personId);
                    String checkcount = getManageData(NetUrl.getcheckcountmethod, personId);

                    Message message = Message.obtain();
                    message.what = MANAGER;
                    Bundle bundle = new Bundle();
                    bundle.putString("patrolcount", patrolcount);
                    bundle.putString("examinecount", examinecount);
                    bundle.putString("dealcount", dealcount);
                    bundle.putString("reviewcount", reviewcount);
                    bundle.putString("postcount", postcount);
                    bundle.putString("checkcount", checkcount);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }


    //根据personId 去获取
    private String getManageData(String methodName, int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, methodName);
        soapObject.addProperty("personId", personId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope, headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }

    private void locat() {
        locationClient = new LocationClient(HomeFragment.this.getActivity().getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3600 * 1000;
        option.setScanSpan(10000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);

        locationClient.start();

    }

    private void markMe(LatLng latlng) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        MarkerOptions option = new MarkerOptions();
        //经纬度
        option.position(latlng).icon(bitmap);
        map.clear();
        map.addOverlay(option);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationClient != null) {
            locationClient.start();
        }
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
        getData();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapview != null) {
            mapview.onResume();
        }
//        getData();

//        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
//        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapview != null) {
            mapview.onPause();
        }
        if (locationClient != null) {
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapview != null) {
            mapview.onDestroy();
        }
        if (locationClient != null) {
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MANAGER:
                    testProgress.setVisibility(View.GONE);
                    Bundle data = msg.getData();

                    String patrolcount = data.getString("patrolcount");
                    String examinecount = data.getString("examinecount");
                    String dealcount = data.getString("dealcount");
                    String reviewcount = data.getString("reviewcount");
                    String postcount = data.getString("postcount");
                    String checkcount = data.getString("checkcount");

                    if (patrolcount!= null && examinecount != null && dealcount != null
                            && reviewcount != null && postcount != null&& checkcount != null ){
                        mtvPatrolNumber.setVisibility(View.VISIBLE);
                        mtvreviewNumber.setVisibility(View.VISIBLE);
                        mtvsendNumber.setVisibility(View.VISIBLE);
                        mtvcheckNumber.setVisibility(View.VISIBLE);
                        mtvuncheckNumber.setVisibility(View.VISIBLE);
                        mtvdealNumber.setVisibility(View.VISIBLE);

                        mtvPatrolNumber.setText(patrolcount);
                        mtvreviewNumber.setText(examinecount);
                        mtvdealNumber.setText(dealcount);
                        mtvsendNumber.setText(reviewcount);
                        mtvuncheckNumber.setText(postcount);
                        mtvcheckNumber.setText(checkcount);
                        if (patrolcount.equals("0")){
                            mtvPatrolNumber.setVisibility(View.GONE);
                        }
                        if (examinecount.equals("0")){
                            mtvreviewNumber.setVisibility(View.GONE);
                        }
                        if (dealcount.equals("0")){
                            mtvdealNumber.setVisibility(View.GONE);
                        }
                        if (reviewcount.equals("0")){
                            mtvsendNumber.setVisibility(View.GONE);
                        }
                        if (postcount.equals("0")){
                            mtvuncheckNumber.setVisibility(View.GONE);
                        }
                        if (checkcount.equals("0")){
                            mtvcheckNumber.setVisibility(View.GONE);
                        }

                    }

                    break;
                case FAIL:
                    testProgress.setVisibility(View.GONE);
                    break;

            }
        }
    };


    private class MyListener extends BDAbstractLocationListener implements View.OnClickListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取到经度
            longitude = bdLocation.getLongitude();
            //获取到纬度
            latitude = bdLocation.getLatitude();
            //经纬度  填的是纬度，经度
            LatLng latlng = new LatLng(latitude, longitude);
            try {
                if (map != null) {
                    map.setMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
                    markMe(latlng);
                } else {
                    ToastUtil.shortToast(getContext(), "无法定位，请检查网络");
                }
            } catch (Exception e) {


            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //排查
                case R.id.ll_home_report:
                    PermissionUtils.requestPermission(HomeFragment.this.getActivity(), PermissionUtils.CODE_CAMERA, mPermissionGrant);
                    break;
                //检查
                case R.id.ll_home_review:
                    IntentUtil.startActivity(getContext(), RoadActivity.class);
                    break;
                //审批
                case R.id.ll_home_send:
                    //下派
                    IntentUtil.startActivity(getContext(), SendRoadActivity.class);
                    break;
                //整改
                case R.id.ll_home_deal:
                    //处置 1，2
                    IntentUtil.startActivity(getContext(), DealActivity.class);
                    break;
                //处置
                case R.id.ll_home_uncheck:
                    //报验
                    IntentUtil.startActivity(getContext(), PostRoadActivity.class);
                    break;
                //验收
                case R.id.ll_home_check:
                    //验收
                    IntentUtil.startActivity(getContext(), CheckRoadActivity.class);
                    break;

            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            ToastUtil.shortToast(getActivity(), "扫描成功");
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            Intent intent = new Intent(getContext(), ReportActivity.class);
            intent.putExtra("scan", scanResult);
            startActivity(intent);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
