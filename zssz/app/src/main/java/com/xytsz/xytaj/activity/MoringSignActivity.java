package com.xytsz.xytaj.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.MoringCheckAdapter;
import com.xytsz.xytaj.adapter.SearchAdapter;
import com.xytsz.xytaj.adapter.SendRoadAdapter;
import com.xytsz.xytaj.bean.CheckItem;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.SearchView;
import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/3/2.
 * 早会签到
 */
public class MoringSignActivity extends AppCompatActivity
{

    @Bind(R.id.tv_sign_team)
    TextView tvSignTeam;
    @Bind(R.id.tv_sign_person)
    TextView tvSignPerson;
    @Bind(R.id.iv_sign_picture)
    ImageView ivSignPicture;
    @Bind(R.id.strand_lv)
    ListView strandLv;
    @Bind(R.id.ll_sign)
    LinearLayout llSign;
    @Bind(R.id.report_sign)
    Button reportSign;
    @Bind(R.id.morning_progressbar)
    LinearLayout morningProgressbar;
    private String tag;
    private String method;
    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.CHECKFAIL:
                    reportSign.setVisibility(View.VISIBLE);
                    morningProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "上传失败");
                    break;
                case GlobalContanstant.CHECKPASS:
                    String endsign = (String) msg.obj;
                    if (endsign.equals("true")) {
                        ToastUtil.shortToast(getApplicationContext(), "签到结束");
                        finish();
                    }

                    break;

                case GlobalContanstant.PERSONSIGNFAIL:
                    ToastUtil.shortToast(getApplicationContext(), "数据未获取");
                    morningProgressbar.setVisibility(View.GONE);
                    break;
                case GlobalContanstant.PERSONSIGNSUCCESS:
                    morningProgressbar.setVisibility(View.GONE);
                    llSign.setVisibility(View.VISIBLE);
                    reportSign.setVisibility(View.VISIBLE);
                    Bundle bundle = msg.getData();
                    String personJson = bundle.getString("person");
                    String checkJson = bundle.getString("check");

                    persons = JsonUtil.jsonToBean(personJson, new TypeToken<List<Person>>() {
                    }.getType());
                    checkItemLists = JsonUtil.jsonToBean(checkJson, new TypeToken<List<CheckItem>>() {
                    }.getType());


                    personlist = new String[persons.size()];
                    for (int i = 0; i < personlist.length; i++) {
                        personlist[i] = persons.get(i).getName();
                    }

                    checkItems.clear();
                    checkItems.add("正常");
                    if (checkItemLists != null) {
                        for (int i = 0; i < checkItemLists.size(); i++) {
                            checkItems.add(checkItemLists.get(i).getSignDicInfo());
                        }
                    }
                    moringCheckAdapter = new MoringCheckAdapter(checkItems);
                    strandLv.setAdapter(moringCheckAdapter);
                    moringCheckAdapter.setCheckItems(diseaseInformation);
                    moringCheckAdapter.notifyDataSetChanged();

                    break;

                case GlobalContanstant.MORNINGSUCCESS:
                    morningProgressbar.setVisibility(View.GONE);
                    reportSign.setVisibility(View.VISIBLE);
                    String result = (String) msg.obj;
                    ToastUtil.shortToast(getApplicationContext(), result);
                    path = null;
                    tvSignPerson.setText("");

                    ivSignPicture.setImageResource(R.mipmap.iv_add);

                    //默认是0 就

                    finish();
                    //退出又进来  count = totalcount;

//                    count = totalcount;
//                    ++count;
//                    if (count == personlist.length) {
//                        reportSign.setText("结束签到");
//                    }


                    break;
                case GlobalContanstant.MORNINGFAIL:
                    reportSign.setVisibility(View.VISIBLE);
                    morningProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "上传失败");
                    path = null;
                    break;
            }


        }
    };

    private int personID;
    private List<Person> persons;
    private String[] personlist;
    private DiseaseInformation diseaseInformation;
    private List<String> checkItems = new ArrayList<>();
    private String title;
    private int selectPersonID;
    private MoringCheckAdapter moringCheckAdapter;
    private Uri fileUri;
    private File file;
    private String department;
    private String problemTag;
    private List<CheckItem> checkItemLists;
    private int count;
    private ListView lv;
    private Dialog dialog;
    private int totalcount;
    private SoapObject soapObject;
    private int trainId;
    private String userName;
    private LocationClient locationClient;
    public BDAbstractLocationListener myListener = new MyListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            fileResult = savedInstanceState.getString("file_path");
        }
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            //早会ID
            signId = getIntent().getIntExtra("singnid", -1);
            totalcount = getIntent().getIntExtra("count", -1);
            //培训ID
            trainId = getIntent().getIntExtra("trainId", -1);

        }
        setContentView(R.layout.activity_morningsign);
        ButterKnife.bind(this);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        department = SpUtils.getString(getApplicationContext(), GlobalContanstant.DEPARATMENT);
        userName = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);
        switch (tag) {
            case "moringsign":
                method = NetUrl.MoringSignmethod;
                title = getResources().getString(R.string.morningsign);
                break;
            case "trainsign":
                method = NetUrl.TrainSignmethod;
                title = getResources().getString(R.string.trainsign);
                break;

        }
        initActionbar(title);
        PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        initData();
        diseaseInformation = new DiseaseInformation();


    }



    private void locat() {
        //进入上报页面的 时候 开始定位
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5 * 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);
        locationClient.start();
    }


    private void initActionbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return super.onSupportNavigateUp();
    }

    private void initData() {
        reportSign.setVisibility(View.GONE);
        tvSignTeam.setText(department);
        tvSignPerson.setText(userName);
        tvSignPerson.setClickable(false);

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        new Thread() {
            @Override
            public void run() {
                try {
                    String personData = getData("person");
                    String checkData = getData("check");


                    if (personData != null && checkData != null) {
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("person", personData);
                        bundle.putString("check", checkData);
                        message.setData(bundle);
                        message.what = GlobalContanstant.PERSONSIGNSUCCESS;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.PERSONSIGNFAIL;
                    handler.sendMessage(message);
                }

            }
        }.start();


    }


    private List<HeaderProperty> headerList = new ArrayList<>();

    private String getData(String tag) throws Exception {

        switch (tag) {
            case "person":
                soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);
                soapObject.addProperty("ID", personID);
                break;
            case "check":
                soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getSignCheck);
                if (this.tag.equals("trainsign")) {
                    soapObject.addProperty("Type", 2);
                } else if (this.tag.equals("moringsign")){
                    soapObject.addProperty("Type", 1);
                }

                break;
        }


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        //添加cookie
//
        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }

    private String problemtext;
    private final String Tag = "com.xytsz.xytaj.fileprovider";
    private static final int Take_Photo = 1;
    private String fileResult;
    private String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/mymy/";
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {

        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {

                case PermissionUtils.CODE_CAMERA:

                    camera(1);
                    break;

                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    locat();
                    break;

//
            }
        }
    };

    private void camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "moringsign" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(MoringSignActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(filepath, "moringsign" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = Uri.fromFile(file);
            fileResult = fileUri.getPath();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, position);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (Build.VERSION.SDK_INT >= 24){
            outState.putString("file_path",fileResult);
        }else {
            outState.putString("file_path", fileResult);

        }

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        switch (requestCode) {
            case Take_Photo:
                if (resultCode == RESULT_OK) {
                    if (data != null){
                        bitmap = (Bitmap) data.getExtras().get("data");
                    }else {
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);


                    }
                    if (bitmap ==null){
                        return;
                    }
                    ivSignPicture.setImageBitmap(bitmap);
                    diseaseInformation.fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    diseaseInformation.encode = ReportActivity.photo2Base64(path);

                }

                break;
        }

        super.onActivityResult(requestCode,resultCode,data);
    }


    private final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";
    private String path;

    private String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName();
        path = iconPath + photoName;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File pictureFile = new File(path);
                //压缩图片
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, bos);
                byte[] bytes = bos.toByteArray();
                //将图片封装成File对象
                FileOutputStream outputStream = new FileOutputStream(pictureFile);
                outputStream.write(bytes);
                outputStream.close();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.shortToast(getApplicationContext(), "SD卡不存在");
        }


        return photoName;

    }

    private String createPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String fileName = format.format(date) + ".jpg";
        return fileName;
    }

    @OnClick({R.id.tv_sign_person, R.id.iv_sign_picture, R.id.report_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_person:

                break;
            case R.id.iv_sign_picture:
                //添加照片
                PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
                PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);

                break;
            case R.id.report_sign:
                //没有图片不能上报
                //没有选择检查项不能上报
                //选择正常和不正常的同时的时候 不能上报。
                    String personName = tvSignPerson.getText().toString();
                    //获取人员ID
                    for (Person pe : persons) {
                        if (TextUtils.equals(personName, pe.getName())) {
                            selectPersonID = pe.getId();
                        }
                    }
                    List<DiseaseInformation.CheckItem> checkItemList = moringCheckAdapter.getDiseaseInformation();

                    StringBuilder stringBuilder = new StringBuilder();

                    if (checkItemList.get(0).isCheck()) {
                        for (int i = 1; i < checkItemList.size(); i++) {
                            if (checkItemList.get(i).isCheck()) {
                                ToastUtil.shortToast(getApplicationContext(), "检查项选择错误");
                                return;
                            }
                        }

                        problemTag = "正常";
                    } else {
                        problemTag = "不正常";
                    }


                    for (int i = 1; i < checkItemList.size(); i++) {

                        int position = checkItemList.get(i).getPosition();
                        stringBuilder.append(position).append(",");

                    }
                    problemtext = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);

                    upData();

                break;
        }
    }





    private void upData() {
        morningProgressbar.setVisibility(View.VISIBLE);
        reportSign.setVisibility(View.GONE);
        //提交服务器；
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = up2Service();
                    if (result != null) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MORNINGSUCCESS;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MORNINGFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private int signId;

    private String up2Service() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        if (tag.equals("trainsign")) {
            soapObject.addProperty("TrainId", trainId);
        } else if (tag.equals("moringsign")){
            soapObject.addProperty("SignID", signId);
            soapObject.addProperty("latitude", diseaseInformation.latitude);
            soapObject.addProperty("longitude", diseaseInformation.longitude);
        }else {
            soapObject.addProperty("MeetingID", trainId);
        }
        soapObject.addProperty("SignPersonId", selectPersonID);
        soapObject.addProperty("CheckInfo", problemTag);
        soapObject.addProperty("Info", problemtext);
        soapObject.addProperty("FileName", diseaseInformation.fileName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;


        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope,headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }



    private class MyListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (diseaseInformation != null){
                diseaseInformation.longitude = bdLocation.getLongitude() +"";
                diseaseInformation.latitude = bdLocation.getLatitude() +"";
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (locationClient != null){
            locationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationClient != null){
            locationClient.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient !=null){
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
        }
    }
}
