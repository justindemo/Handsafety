package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.ProblemLocation;
import com.xytsz.xytaj.global.Data;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.NativeDialog;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 * 病害定位
 */
public class MakerProblemActivty extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {

    private static final int ISGETALLTASK = 711001;
    private MapView mMV;
    private BaiduMap baiduMap;
    private View pop;
    private TextView mReportName;
    private TextView mTvStatu;
    private ImageView mIvDealIcon;
    private ImageView mIvReportIcon;
    private Button mBtNavi;
    private Button mBtDetail;

    private List<ProblemLocation.ProblemListBean> details;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ISGETALLTASK:
                    details = (List<ProblemLocation.ProblemListBean>) msg.obj;
                    if (details != null) {
                        progress.setVisibility(View.GONE);
                        if (baiduMap != null) {
                            initPop();
                            draw();
                        }
                    }
                    break;
            }
        }
    };
    private LinearLayout progress;
    private ImageView mIvDealIcon2;
    private ImageView mIvDealIcon3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maker);
        initAcitionbar();
        initView();
        initData();
    }

    private void initView() {

        mMV = (MapView) findViewById(R.id.mv_maker);
        progress = (LinearLayout) findViewById(R.id.ll_progress_bar);

        baiduMap = mMV.getMap();

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pop.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

    }

    private void initData() {


        //网络
        locat();

        new Thread() {
            @Override
            public void run() {
                try {
                    String json = getAllTask();


                    if (json != null) {
                        ProblemLocation problemLocation = JsonUtil.jsonToBean(json, ProblemLocation.class);
                        List<ProblemLocation.ProblemListBean> list = problemLocation.getProblemList();

                        if (list.size() != 0) {
                            for (ProblemLocation.ProblemListBean detail : list) {
                                String taskNumber = detail.getTasknumber();
                                /**
                                 * 获取到图片的URl
                                 */
                                String imageJson = MyApplication.getAllImagUrl(taskNumber, GlobalContanstant.GETREVIEW);

                                if (imageJson != null) {
                                    //String list = new JSONObject(json).getJSONArray("").toString();
                                    List<ImageUrl> imageUrlList = new Gson().fromJson(imageJson, new TypeToken<List<ImageUrl>>() {
                                    }.getType());

                                    imageUrlLists.add(imageUrlList);
                                }

                            }
                            Message message = Message.obtain();
                            message.what = ISGETALLTASK;
                            message.obj = list;
                            handler.sendMessage(message);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(getApplicationContext(), "没有上报的数据");
                                }
                            });
                        }

                    }

                } catch (Exception e) {

                }
            }
        }.start();


    }

    private List<List<ImageUrl>> imageUrlLists = new ArrayList<>();

    private String getAllTask() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllTaskMethodName);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getAllTask_SOAP_ACTION, envelope);


        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    private void draw() {

        //是否显示缩放按钮
        mMV.showZoomControls(true);
        //是否显示地图标尺
        mMV.showScaleControl(false);


        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_en);
        ArrayList<BitmapDescriptor> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);
        bitmaps.add(BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_twinkle));


        for (int i = 0; i < details.size(); i++) {

            ProblemLocation.ProblemListBean problemDetail = details.get(i);
            double latitude = problemDetail.getLatitude();
            double longitude = problemDetail.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions option = new MarkerOptions()
                    .position(latLng).icons(bitmaps)
                    .title(problemDetail.getTasknumber());
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
            baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
            baiduMap.addOverlay(option);
        }

        // 把事件传递给Marker覆盖物
        baiduMap.setOnMarkerClickListener(this);

    }

    private void initPop() {
        pop = View.inflate(this, R.layout.maker_pop, null);
        mReportName = (TextView) pop.findViewById(R.id.report_name);
        mTvStatu = (TextView) pop.findViewById(R.id.tv_statu);
        mIvDealIcon = (ImageView) pop.findViewById(R.id.iv_deal_icon1);
        mIvDealIcon2 = (ImageView) pop.findViewById(R.id.iv_deal_icon2);
        mIvDealIcon3 = (ImageView) pop.findViewById(R.id.iv_deal_icon3);
        mIvReportIcon = (ImageView) pop.findViewById(R.id.marker_reporter_icon);
        mBtNavi = (Button) pop.findViewById(R.id.bt_navi);
        mBtDetail = (Button) pop.findViewById(R.id.bt_mark_detail);

        pop.setVisibility(View.INVISIBLE);

        ProblemLocation.ProblemListBean problemDetail = details.get(0);

        double latitude = problemDetail.getLatitude();
        double longitude = problemDetail.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(latLng)// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .build();
        mMV.addView(pop, param);
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
        mMV.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMV.onPause();
        if (locationClient != null){
            locationClient.unRegisterLocationListener(myListener);
            locationClient.stop();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMV.onDestroy();
        if (locationClient != null){
            locationClient.unRegisterLocationListener(myListener);
            locationClient.stop();
        }
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        for (final ProblemLocation.ProblemListBean detail : details) {

            if (TextUtils.equals(detail.getTasknumber(), marker.getTitle())) {

                String upload_person_name = detail.getUpload_Person_name();
                mReportName.setText(upload_person_name);

                //状态
                mTvStatu.setText(Data.phaseIndaciton[detail.getPhaseindication()]);

                //加载图片

                //病害头像
                if (imageUrlLists.size() != 0) {
                    for (final List<ImageUrl> imageUrlList : imageUrlLists) {

                        if (imageUrlList.size() != 0) {


//                            if (TextUtils.equals(imageUrlList.get(0).getTaskNumber(), marker.getTitle())) {
//                                if (imageUrlList.size() == 1) {
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(mIvDealIcon);
//                                }
//                                if (imageUrlList.size() == 2) {
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(mIvDealIcon);
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(1).getImgurl()).into(mIvDealIcon2);
//                                }
//                                if (imageUrlList.size() == 3) {
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(mIvDealIcon);
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(1).getImgurl()).into(mIvDealIcon2);
//                                    Glide.with(getApplicationContext()).load(imageUrlList.get(2).getImgurl()).into(mIvDealIcon3);
//                                }

//
//                                mIvDealIcon.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(v.getContext(), MarkerReportPhotoActivity.class);
//                                        intent.putExtra("imageUrls", (Serializable) imageUrlList);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
                        } else {Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(mIvDealIcon);}
                    }
                }

                // mIvReportIcon.setImageResource();


                mBtNavi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng latLng = new LatLng(detail.getLatitude(), detail.getLongitude());

                        NativeDialog msgDialog = new NativeDialog(v.getContext(), latlngNow, latLng);
                        msgDialog.show();
                        pop.setVisibility(View.INVISIBLE);

                    }
                });

                mBtDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ProcessDetailActivity.class);
                        intent.putExtra("detail", detail);
                        startActivity(intent);
                    }
                });
            }
        }

        pop.setVisibility(View.VISIBLE);
        MapViewLayoutParams param = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 使用经纬度模式
                .position(marker.getPosition())// 设置控件跟着地图移动
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-10)
                .build();
        mMV.updateViewLayout(pop, param);


        return true;
    }

    public BDAbstractLocationListener myListener = new MyListener();
    private LocationClient locationClient;
    private LatLng latlngNow;

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

    private class MyListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取到经度
            double longitude = bdLocation.getLongitude();
            //获取到纬度
            double latitude = bdLocation.getLatitude();
            //经纬度  填的是纬度，经度
            latlngNow = new LatLng(latitude, longitude);


        }

    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.problemdeal);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
