package com.xytsz.xytaj.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.dalong.marqueeview.MarqueeView;
import com.google.zxing.activity.CaptureActivity;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.CheckRoadActivity;
import com.xytsz.xytaj.activity.DealActivity;
import com.xytsz.xytaj.activity.PostRoadActivity;
import com.xytsz.xytaj.activity.ReportActivity;
import com.xytsz.xytaj.activity.RoadActivity;
import com.xytsz.xytaj.activity.SendRoadActivity;
import com.xytsz.xytaj.base.BaseFragment;

import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;

import com.xytsz.xytaj.net.NetUrl;

import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

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
public class HomeFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback{


    private static final int FAIL = 404;
    private static final int MANAGER = 111120;
    private static final int SIMPLER = 111122;
    private static final int REQUEST_CODE =0x01 ;
    private int RESULT_OK = 0xA1;
    private TextureMapView mapview;
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
    private MarqueeView mtvMarquee;
    private int alluser;
    private String noreport;
    private String noreview;
    private String nosend;
    private String nodeal;
    private String nopost;
    private String nocheck;
    private String noData;
    private List<Review> reviewList;
    private List<Review> sendList;
    private List<Review> checkList;

    private int reviewNumber;
    private int sendNumber;
    private int checkNumber;
    private int dealNumber;

    private List<Integer> manageNumbers = new ArrayList<>();
    private String loading;
    private TextView mActionbartext;
    private LinearLayout mllScan;


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        mActionbartext = (TextView) view.findViewById(R.id.actionbar_text);
        mtvMarquee = (MarqueeView) view.findViewById(R.id.tv_marquee);
        mapview = (TextureMapView) view.findViewById(R.id.home_mv);
        mllReport = view.findViewById(R.id.ll_home_report);
        mllReview = view.findViewById(R.id.ll_home_review);
        mllDeal = view.findViewById(R.id.ll_home_deal);
        mllSend = view.findViewById(R.id.ll_home_send);
        mllUncheck = view.findViewById(R.id.ll_home_uncheck);
        mllCheck = view.findViewById(R.id.ll_home_check);
        mllScan = (LinearLayout) view.findViewById(R.id.ll_home_scan);
        mtvdealNumber = (TextView) view.findViewById(R.id.tv_home_deal_number);
        mtvreviewNumber = (TextView) view.findViewById(R.id.tv_home_review_number);
        mtvsendNumber = (TextView) view.findViewById(R.id.tv_home_send_number);
        mtvuncheckNumber = (TextView) view.findViewById(R.id.tv_home_unchecek_number);
        mtvcheckNumber = (TextView) view.findViewById(R.id.tv_home_check_number);
        return view;
    }

    private int personId;

    @Override
    public void initData() {

        String alltitle = getString(R.string.alltitle);
        noreport = getString(R.string.home_noreporte);
        loading = getString(R.string.home_loading);
        noreview = getString(R.string.home_noreview);
        nosend = getString(R.string.home_nosend);
        nodeal = getString(R.string.home_nodeal);
        nopost = getString(R.string.home_nopost);
        nocheck = getString(R.string.home_nocheck);
        noData = getString(R.string.table_nodata);

        alluser = SpUtils.getInt(getContext(), GlobalContanstant.ALLUSERCOUNT);
        mtvMarquee.setText(alltitle + alluser);
        mtvMarquee.setFocusable(true);
        mtvMarquee.requestFocus();
        mtvMarquee.sepX = 2;
        mtvMarquee.startScroll();

        mActionbartext.setText(R.string.app_name);

        //获取当前登陆人的ID
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        getData();


        //是否显示缩放按钮
        mapview.showZoomControls(false);
        //是否显示地图标尺
        mapview.showScaleControl(false);
        map = mapview.getMap();

        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(this.getActivity(), PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);

        map.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        //1,纬度double latitude  2，经度longitude  116.347005,39.812991
        mllCheck.setOnClickListener(listener);
        mllReport.setOnClickListener(listener);
        mllUncheck.setOnClickListener(listener);
        mllDeal.setOnClickListener(listener);
        mllSend.setOnClickListener(listener);
        mllReview.setOnClickListener(listener);
        mllScan.setOnClickListener(listener);

    }


    private void getData() {
        reviewNumber = 0;
        sendNumber = 0;
        checkNumber = 0;
        dealNumber = 0;
        manageNumbers.clear();
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

//                    if (role == 1) {
//
//                        String reviewData = getManageData(GlobalContanstant.GETREVIEW);
//                        String sendData = getManageData(GlobalContanstant.GETSEND);
//                        String checkData = getManageData(GlobalContanstant.GETCHECK);
//                        Review review = JsonUtil.jsonToBean(reviewData, Review.class);
//                        Review send = JsonUtil.jsonToBean(sendData, Review.class);
//                        Review check = JsonUtil.jsonToBean(checkData, Review.class);
//
//                        reviewList = review.getReviewRoadList();
//                        sendList = send.getReviewRoadList();
//                        checkList = check.getReviewRoadList();
//
//                        for (Review.ReviewRoad reviewRoad : reviewList) {
//                            reviewNumber += reviewRoad.getList().size();
//                        }
//                        manageNumbers.add(reviewNumber);
//                        for (Review.ReviewRoad reviewRoad : sendList) {
//                            sendNumber += reviewRoad.getList().size();
//                        }
//                        manageNumbers.add(sendNumber);
//                        for (Review.ReviewRoad reviewRoad : checkList) {
//                            checkNumber += reviewRoad.getList().size();
//                        }
//                        manageNumbers.add(checkNumber);
//
//
//                        Message message = Message.obtain();
//                        message.what = MANAGER;
//                        message.obj = manageNumbers;
//                        handler.sendMessage(message);
//
//
//                    }
//
//                    if (role == 1 || role == 2) {
//                        String simpleData = getSimpleData(GlobalContanstant.GETDEAL, personId);
//                        Review review = JsonUtil.jsonToBean(simpleData, Review.class);
//                        List<Review.ReviewRoad> reviewRoadList = review.getReviewRoadList();
//                        for (Review.ReviewRoad reviewRoad : reviewRoadList) {
//                            dealNumber += reviewRoad.getList().size();
//                        }
//
//                        Message message = Message.obtain();
//                        message.what = SIMPLER;
//                        message.obj = dealNumber;
//                        handler.sendMessage(message);
//                    }


                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getSimpleData(int state, int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getManagementList);
        soapObject.addProperty("PhaseIndication", state);
        soapObject.addProperty("personId", personId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getManagementList_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;

    }

    //根据personId 去获取
    private String getManageData(int state) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskList);
        soapObject.addProperty("PhaseIndication", state);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getTasklist_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;
    }

    private void locat() {
        locationClient = new LocationClient(getContext());
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationClient != null) {
            locationClient.start();
        }
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
        mtvMarquee.startScroll();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
        mtvMarquee.startScroll();

        getData();


    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
        mtvMarquee.stopScroll();
        if (locationClient != null){
            locationClient.unRegisterLocationListener(myListener);
            locationClient.stop();
        }

    }

    @Override
    public void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    private static final int ISLOAD = 33301;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MANAGER:
                    mtvreviewNumber.setVisibility(View.VISIBLE);
                    mtvsendNumber.setVisibility(View.VISIBLE);
                    mtvcheckNumber.setVisibility(View.VISIBLE);

                    List<Integer> list = (List<Integer>) msg.obj;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) == 0) {
                            if (i == 0) {
                                mtvreviewNumber.setVisibility(View.GONE);
                            } else if (i == 1) {
                                mtvsendNumber.setVisibility(View.GONE);
                            } else if (i == 2) {
                                mtvcheckNumber.setVisibility(View.GONE);
                            }
                        } else {
                            mtvreviewNumber.setText(String.valueOf(list.get(0)));
                            mtvsendNumber.setText(String.valueOf(list.get(1)));
                            mtvcheckNumber.setText(String.valueOf(list.get(2)));
                        }
                    }

                    reviewNumber = 0;
                    sendNumber = 0;
                    checkNumber = 0;

                    break;
                case SIMPLER:
                    mtvuncheckNumber.setVisibility(View.VISIBLE);
                    mtvdealNumber.setVisibility(View.VISIBLE);
                    int number = (int) msg.obj;
                    if (number == 0) {
                        mtvdealNumber.setVisibility(View.GONE);
                        mtvuncheckNumber.setVisibility(View.GONE);
                    } else {
                        mtvdealNumber.setText(String.valueOf(number));
                        mtvuncheckNumber.setText(String.valueOf(number));
                    }
                    dealNumber = 0;
                    break;

                case FAIL:
                    ToastUtil.shortToast(getContext(), noData);
                    break;
                case ISLOAD:
                    String isload = (String) msg.obj;
                    if (!isload.equals("true")) {
                        ToastUtil.shortToast(getContext(), "上报位置信息失败，请检查网络");
                    }
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

            new Thread() {
                @Override
                public void run() {

                    try {
                        String isLoad = toUpLoadLocation(personId, latitude + "", longitude + "");

                        Message message = Message.obtain();
                        message.what = ISLOAD;
                        message.obj = isLoad;
                        handler.sendMessage(message);

                    } catch (Exception e) {

                    }

                }
            }.start();

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_home_report:
                    PermissionUtils.requestPermission(HomeFragment.this.getActivity(),PermissionUtils.CODE_CAMERA,mPermissionGrant);
                    break;
                case R.id.ll_home_review:
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), RoadActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), noreview);
                    }
                    break;
                case R.id.ll_home_send:
                    //下派
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), SendRoadActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), nosend);
                    }

                    break;
                case R.id.ll_home_deal:
                    //处置 1，2
                    if (role == 1 || role == 2) {
                        IntentUtil.startActivity(getContext(), DealActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), nodeal);
                    }
                    break;
                case R.id.ll_home_uncheck:
                    //报验
                    if (role == 1 || role == 2) {
                        IntentUtil.startActivity(getContext(), PostRoadActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), nopost);
                    }

                    break;
                case R.id.ll_home_check:
                    //验收
                    if (role == 1) {
                        IntentUtil.startActivity(getContext(), CheckRoadActivity.class);
                    } else {
                        ToastUtil.shortToast(getContext(), "您没有验收的权限");
                    }

                    break;

                case R.id.ll_home_scan:

                   // PermissionUtils.requestPermission(HomeFragment.this.getActivity(),PermissionUtils.CODE_CAMERA,mPermissionGrant);

                    break;
            }
        }
    }

    private String toUpLoadLocation(int personID, String latitude, String longitude) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.uploadLocationmethodName);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("latitude", latitude);
        soapObject.addProperty("longitude", longitude);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.toUploadlocation_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            ToastUtil.shortToast(getActivity(),"扫描成功");
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            Intent intent = new Intent(getContext(),ReportActivity.class);
            intent.putExtra("scan",scanResult);
            startActivity(intent);

        }



    }
}
