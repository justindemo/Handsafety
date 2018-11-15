package com.xytsz.xytaj.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.ReportData;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileBase64Util;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * Created by admin on 2018/10/23.
 * </p>
 */
public class RandomReportActivity extends AppCompatActivity {


    @Bind(R.id.random_report_facility)
    EditText randomReportFacility;
    @Bind(R.id.random_report_facility_person)
    TextView randomReportFacilityPerson;
    @Bind(R.id.random_report_facility_loca)
    EditText randomReportFacilityLoca;
    @Bind(R.id.random_report_facility_problem)
    EditText randomReportFacilityProblem;
    @Bind(R.id.random_iv_report_icon1)
    ImageView randomIvReportIcon1;
    @Bind(R.id.random_iv_photo1_delete)
    ImageView randomIvPhoto1Delete;
    @Bind(R.id.random_iv_report_icon2)
    ImageView randomIvReportIcon2;
    @Bind(R.id.random_iv_photo2_delete)
    ImageView randomIvPhoto2Delete;
    @Bind(R.id.random_iv_report_icon3)
    ImageView randomIvReportIcon3;
    @Bind(R.id.random_iv_photo3_delete)
    ImageView randomIvPhoto3Delete;
    @Bind(R.id.iv_input_style)
    ImageView ivInputStyle;
    @Bind(R.id.tv_press_audio)
    TextView tvPressAudio;
    @Bind(R.id.tv_audio)
    TextView tvAudio;
    @Bind(R.id.tv_re_record)
    TextView tvReRecord;
    @Bind(R.id.ll_audio)
    LinearLayout llAudio;
    @Bind(R.id.locationDesc)
    EditText locationDesc;
    @Bind(R.id.btn_report)
    Button btnReport;
    @Bind(R.id.report_progressbar)
    LinearLayout reportProgressbar;
    private int personId;
    private String userName;
    private String uploading;
    private String upimg;
    private String imgsuccess;
    private String request;
    private String deleteTitle;
    private String deleteContent;
    private String deleteOk;
    private String deleteCancle;
    private String recordLittle;
    private String recordMax;
    private String reportLocation;
    private String notifa;
    private String notifatitle;
    private String error;
    private String imageerror;
    private String reportsuccess;
    private String uperror;
    private String audiosuccess;
    private String audioFail;
    private String disrecord;
    private String reportMore;

    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();
    private List<HeaderProperty> headerList = new ArrayList<>();

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    locate();
                    break;

                case PermissionUtils.CODE_CAMERA:
                    camera(1);
                    break;

                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    File filePath = new File(pathUrl);
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    break;
            }

        }
    };
    private SoundUtil soundUtil;
    private DiseaseInformation diseaseInformation;
    //图片的存储位置
    private static final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";//图片的存储目录
    private static final String audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Audio/";
    private static final String Tag = "com.xytsz.xytaj.fileprovider";
    private String remoteInfo;
    private String isphotoSuccess1;
    public BDAbstractLocationListener myListener = new MyListener();
    private LocationClient locationClient;

    //定位
    private void locate() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3600 * 1000;
        option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
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
            String addrStr = bdLocation.getAddrStr();
            randomReportFacilityLoca.setText(addrStr);

        }
    }

    private String fileResult;

    private boolean isdelete1;
    private boolean isdelete2;
    private boolean isdelete3;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.CODE_RECORD_AUDIO) {
            PermissionUtils.openSettingActivity(RandomReportActivity.this, "请打开录音权限");
            return;
        }

        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomreporte);
        ButterKnife.bind(this);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        userName = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);

        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        uploading = this.getString(R.string.report_uploading);
        upimg = this.getString(R.string.report_upimg);
        imgsuccess = this.getString(R.string.report_imgsuccess);
        request = this.getString(R.string.report_request);
        deleteTitle = getString(R.string.report_delete_title);

        deleteContent = getString(R.string.report_delete_content);
        deleteOk = getString(R.string.report_delete_ok);
        deleteCancle = getString(R.string.report_delete_cancle);
        recordLittle = getString(R.string.report_record_little);
        recordMax = getString(R.string.report_record_max);

        reportLocation = getString(R.string.reportlocation);

        notifa = this.getString(R.string.report_notifica);
        notifatitle = this.getString(R.string.report_notifica_title);
        error = this.getString(R.string.report_error);
        imageerror = this.getString(R.string.report_imageerror);
        reportsuccess = this.getString(R.string.report_success);
        uperror = this.getString(R.string.report_uperror);

        audiosuccess = getString(R.string.audio_success);
        audioFail = getString(R.string.audio_fail);


        disrecord = getString(R.string.disrecord);

        reportMore = getString(R.string.reportmore);

        initAcitionbar();

        initData();


    }

    /**
     * 给拍的照片命名
     */
    public String createPhotoName(int position) {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return format.format(date) + "_" + position + "_" + personId + ".jpg";

    }

    private String path;

    /**
     * 保存到本地
     */
    public String saveToSDCard(Bitmap bitmap, int position) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName(position);
        path = iconPath + photoName;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File pictureFile = new File(path);
                //压缩图片
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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


    private void initData() {
        randomReportFacilityPerson.setText(userName);
        if (diseaseInformation == null){
            diseaseInformation = new DiseaseInformation();
        }

        soundUtil = new SoundUtil();


        tvPressAudio.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!Environment.getExternalStorageDirectory().exists()) {
                    ToastUtil.shortToast(getApplicationContext(), "No SDcard");
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvPressAudio.setBackgroundResource(R.drawable.shape_tv_audio_press);
                        //开始录音，开始倒计时计时
                        audioName = createAudioName();
                        audioNamepath = audioPath + audioName;
                        start(audioName);
                        startTime = System.currentTimeMillis();
                        startTimer.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        tvPressAudio.setBackgroundResource(R.drawable.shape_tv_audio);
                        //先判断录音是否小于一秒,
                        //不小于：让重录显示出来，。
                        // 点击重录的时候。弹窗确定后time为0，删除文件
                        //
                        startTimer.cancel();
                        long endtime = System.currentTimeMillis();
                        if (endtime - startTime < MAX_TIME) {
                            finishRecord(endtime);
                        } else {  //当手指向上滑，会cancel
                            cancelRecord();
                        }

                        if (endtime - startTime < MIN_INTERVAL_TIME) {
                            tvPressAudio.setEnabled(true);
                            tvPressAudio.setFocusable(true);

                            tvAudio.setVisibility(View.GONE);
                        } else {
                            tvPressAudio.setEnabled(false);
                            tvPressAudio.setFocusable(false);
                            tvAudio.setVisibility(View.VISIBLE);
                        }


                        break;

                }

                return true;
            }
        });

        //点击播放按钮
        tvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioNamepath == null) {
                    ToastUtil.shortToast(getApplicationContext(), disrecord);
                    return;
                } else {
                    final Drawable drawableleft = getResources().getDrawable(R.mipmap.iv_audio_play);
                    final Drawable drawable = getResources().getDrawable(R.mipmap.iv_audio_pause);
                    tvAudio.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);


                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                        @Override
                        public void onFinish() {
                            tvAudio.setCompoundDrawablesWithIntrinsicBounds(drawableleft, null, null, null);
                        }

                        @Override
                        public void onError() {
                            ToastUtil.shortToast(getApplicationContext(), reportMore);
                        }
                    });

                    paly(audioNamepath);
                }
            }
        });

        tvReRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RandomReportActivity.this).setTitle(deleteTitle).setMessage(deleteContent).setPositiveButton(deleteOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(audioNamepath);
                        if (file.isFile() && file.exists()) {
                            file.delete();

                        }
                        audioNamepath = null;
                        tvReRecord.setVisibility(View.GONE);
                        tvAudio.setText("");
                        tvPressAudio.setEnabled(true);
                        tvPressAudio.setFocusable(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton(deleteCancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

    }

    private void paly(String name) {

        soundUtil.play(name);
    }

    private void cancelRecord() {
        stopRecording();
        File file = new File(audioNamepath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    private String audioName;
    private String audioNamepath;
    private static long startTime;
    private boolean btn_vocie = false;
    private static final int MIN_INTERVAL_TIME = 1500;// 1s 最短
    public final static int MAX_TIME = 60 * 1000 + 500;// 1分钟，最长

    /**
     * 录音开始计时器，允许的最大时长进入倒计时
     */
    private CountDownTimer startTimer = new CountDownTimer(MAX_TIME - 500, 1000) { // 50秒后开始倒计时
        @Override
        public void onFinish() {
            ToastUtil.shortToast(getApplicationContext(), recordMax);
            long endtime = System.currentTimeMillis();
            finishRecord(endtime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    };

    private void finishRecord(long endtime) {
        long intervalTime = endtime - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            ToastUtil.shortToast(getApplicationContext(), recordLittle);
            stopRecording();
            File file = new File(audioNamepath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            return;
        } else {
            int time = (int) (intervalTime / 1000);
            tvAudio.setText(time + "s");
            tvReRecord.setVisibility(View.VISIBLE);
            stopRecording();
        }
    }

    private void stopRecording() {
        soundUtil.stop();
    }


    private String createAudioName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String name = simpleDateFormat.format(new Date()) + ".aac";
        return name;
    }

    private void start(String name) {
        soundUtil = new SoundUtil();
        soundUtil.start(name);
    }

    private File file;
    private Uri fileUri;
    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/mymy/";

    private void camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "randomimg" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(RandomReportActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(pathUrl, "randomimg" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = Uri.fromFile(file);
            fileResult = fileUri.getPath();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, position);
    }


    @OnClick({R.id.random_iv_report_icon1, R.id.random_iv_photo1_delete, R.id.random_iv_report_icon2, R.id.random_iv_photo2_delete, R.id.random_iv_report_icon3, R.id.random_iv_photo3_delete, R.id.iv_input_style, R.id.btn_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.random_iv_report_icon1:
                PermissionUtils.requestPermission(RandomReportActivity.this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
                PermissionUtils.requestPermission(RandomReportActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
                break;
            case R.id.random_iv_photo1_delete:
                randomIvReportIcon1.setClickable(true);
                randomIvReportIcon1.setImageResource(R.mipmap.iv_add);
                if (fileNames.size() > 0) {
                    fileNames.set(0, "");
                    imageBase64Strings.set(0, "");
                    isdelete1 = true;
                }
                break;
            case R.id.random_iv_report_icon2:
                camera(2);
                break;
            case R.id.random_iv_photo2_delete:
                randomIvReportIcon2.setClickable(true);
                randomIvReportIcon2.setImageResource(R.mipmap.iv_add);
                if (fileNames.size() > 1) {
                    fileNames.set(1, "");
                    imageBase64Strings.set(1, "");
                    isdelete2 = true;
                }
                break;
            case R.id.random_iv_report_icon3:
                camera(3);
                break;
            case R.id.random_iv_photo3_delete:
                randomIvReportIcon3.setClickable(true);
                randomIvReportIcon3.setImageResource(R.mipmap.iv_add);
                if (fileNames.size() > 2) {
                    fileNames.set(2, "");
                    imageBase64Strings.set(2, "");
                    isdelete3 = true;
                }

                break;
            case R.id.iv_input_style:

                if (btn_vocie) {
                    llAudio.setVisibility(View.GONE);
                    locationDesc.setVisibility(View.VISIBLE);
                    ivInputStyle.setImageResource(R.mipmap.iv_audio);
                    btn_vocie = false;
                } else {
                    PermissionUtils.requestPermission(RandomReportActivity.this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);
                    //判断是否有权限
                    try {
                        int checkSelfPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);

                        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(RandomReportActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
                                new AlertDialog.Builder(RandomReportActivity.this).setMessage("需要录音权限，请允许").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                            } else {
                                //23以下 不提示弹窗
                                ActivityCompat.requestPermissions(RandomReportActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, PermissionUtils.CODE_RECORD_AUDIO);
                            }
                        }
                    } catch (RuntimeException e) {
                        ToastUtil.shortToast(getApplicationContext(), "请打开录音权限");
                    }


                    ivInputStyle.setImageResource(R.mipmap.iv_normal_text);
                    llAudio.setVisibility(View.VISIBLE);
                    locationDesc.setVisibility(View.GONE);
                    btn_vocie = true;
                }

                break;
            case R.id.btn_report:
                String facility = randomReportFacility.getText().toString();
                String location = randomReportFacilityLoca.getText().toString();
                String problem = randomReportFacilityProblem.getText().toString();
                diseaseInformation.taskNumber = getTaskNumber();

                if (!facility.isEmpty() && !location.isEmpty() && !problem.isEmpty() && locationDesc != null) {

                    String locationdesc = locationDesc.getText().toString().trim();
                    if (locationdesc.isEmpty() && audioNamepath == null) {
                        ToastUtil.shortToast(getApplicationContext(), reportLocation);
                        return;
                    } else {
                        if (fileNames.size() > 0 ) {
                            if (audioNamepath != null){
                                locationdesc = "";
                            }
                            diseaseInformation.locationDesc = locationdesc;
                            diseaseInformation.divice = facility;
                            diseaseInformation.upload_Person_ID = personId;
                            diseaseInformation.location = location;
                            diseaseInformation.audioTime = tvAudio.getText().toString();
                            btnReport.setVisibility(View.GONE);
                            reportProgressbar.setVisibility(View.VISIBLE);
                            ToastUtil.shortToast(getApplicationContext(), uploading);
                            sumbitcontent();
                        } else {
                            ToastUtil.shortToast(getApplicationContext(), request);
                        }
                    }


                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请核对数据");
                }


                break;
        }
    }

    private void sumbitcontent() {
        new Thread() {
            @Override
            public void run() {
                if (audioNamepath != null) {
                    File file = new File(audioNamepath);
                    //如果文件存在
                    if (file.exists()) {
                        try {
                            String encodeBase64File = FileBase64Util.encodeBase64File(audioNamepath);
                            diseaseInformation.encodeBase64File = encodeBase64File;
                            diseaseInformation.audioName = audioName;
                            String result = updateAudio(diseaseInformation);
                            Message message = Message.obtain();
                            message.what = AUDIO_SUCCESS;
                            message.obj = result;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = Message.obtain();
                            message.what = AUDIO_FAIL;
                            handler.sendMessage(message);
                            return;
                        }


                    }
                }


                try {
                    remoteInfo = getRemoteInfo(diseaseInformation);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.REPORTEFAIL;
                    handler.sendMessage(message);
                }

                if (remoteInfo != null) {
                    if (remoteInfo.equals("true")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(getApplicationContext(), reportsuccess);
                            }
                        });

                        if(diseaseInformation != null) {
                            for (int i = 0; i < fileNames.size(); i++) {
                                diseaseInformation.photoName = fileNames.get(i);
                                diseaseInformation.encode = imageBase64Strings.get(i);
                                if (!diseaseInformation.photoName.isEmpty()) {
                                    try {
                                        isphotoSuccess1 = connectWebService(diseaseInformation);
                                    } catch (Exception e) {
                                        Message message = Message.obtain();
                                        message.what = GlobalContanstant.IMAGEFAIL;
                                        handler.sendMessage(message);
                                    }
                                }

                            }
                            Message message = Message.obtain();
                            message.obj = isphotoSuccess1;
                            message.what = GlobalContanstant.REPORTESUCCESS;
                            handler.sendMessage(message);
                        }else {
                            Message message = Message.obtain();
                            message.what = GlobalContanstant.IMAGEFAIL;
                            handler.sendMessage(message);
                        }

                    } else {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.REPORTEFAIL;
                        handler.sendMessage(message);
                    }

                } else {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.REPORTEFAIL;
                    handler.sendMessage(message);
                }

            }
        }.start();
    }


    //得到任务单号
    private String getTaskNumber() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }


    //上传所有的数据
    public String getRemoteInfo(DiseaseInformation diseaseInformation) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.randomreportmethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("device", diseaseInformation.divice);
        //tag
        soapObject.addProperty("AddressInfo", diseaseInformation.location);
        //具体的描述
        soapObject.addProperty("Remarks", diseaseInformation.locationDesc);
        //问题string
        soapObject.addProperty("problem", diseaseInformation.problem);
        soapObject.addProperty("personId", personId);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope, headerList);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        return object.getProperty(0).toString();


    }

    private String updateAudio(DiseaseInformation diseaseInformation) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.audiomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.audioName);  //文件类型
        soapObject.addProperty("AudioBase64", diseaseInformation.encodeBase64File);   //参数2  图片字符串
        soapObject.addProperty("time", diseaseInformation.audioTime);   //参数2  图片字符串

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope, headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();

    }

    private String connectWebService(DiseaseInformation diseaseInformation) throws Exception {
        //构建初始化soapObject
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        soapObject.addProperty("PhaseId", GlobalContanstant.GETREVIEW);

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope, headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();

    }




    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {


                case GlobalContanstant.REPORTEFAIL:
//                    initArray();
                    btnReport.setVisibility(View.VISIBLE);
                    reportProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), uperror);

                    return;

                case GlobalContanstant.REPORTESUCCESS:

                    reportProgressbar.setVisibility(View.GONE);

                    String reportSuccess = (String) msg.obj;
                    if (reportSuccess != null) {
                        if (reportSuccess.equals("true")) {
                            String userName = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);
                            Bitmap largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                            NotificationManager nm = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                            //Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Notification noti = new NotificationCompat.Builder(getApplicationContext())
                                    .setTicker(userName + notifatitle)
                                    .setContentTitle(userName)
                                    .setContentText(notifa)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(largeBitmap)
                                    .setContentIntent(getContentIntent())
                                    .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH)//高优先级
                                    .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                                    //自动隐藏
                                    .setAutoCancel(true)
                                    .build();
                            //id =0 =  用来定义取消的id
                            nm.notify(0, noti);

                            ToastUtil.shortToast(getApplicationContext(), imgsuccess);

                            diseaseInformation = null;

                            goHome();
                        } else {
                            ToastUtil.shortToast(getApplicationContext(), uperror);
                            btnReport.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), uperror);
                        btnReport.setVisibility(View.VISIBLE);
                    }
                    break;

                case GlobalContanstant.IMAGEFAIL:
                    ToastUtil.shortToast(getApplicationContext(), imageerror);
                    btnReport.setVisibility(View.VISIBLE);
                    reportProgressbar.setVisibility(View.GONE);
                    return;

                case AUDIO_FAIL:
                    btnReport.setVisibility(View.VISIBLE);
                    reportProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), audioFail);
                    return;

                case AUDIO_SUCCESS:
                    String audioSuccess = (String) msg.obj;
                    if (audioSuccess != null) {
                        if (audioSuccess.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), audiosuccess);
                        }
                    } else {
                        btnReport.setVisibility(View.VISIBLE);
                        reportProgressbar.setVisibility(View.GONE);
                    }

                    break;


            }
        }
    };

    private static final int AUDIO_SUCCESS = 500;
    private static final int AUDIO_FAIL = 404;

    private void goHome() {
        Intent intent = new Intent(RandomReportActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        startActivity(intent);
        finish();
    }


    private PendingIntent getContentIntent() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        return PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    /**
     * 显示图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        Bitmap bitmap = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

                }
                if (bitmap == null) {
                    return;
                } else {
                    randomIvReportIcon1.setImageBitmap(bitmap);
                    String fileName1 = saveToSDCard(bitmap, 1);
                    //将选择的图片设置到控件上
                    randomIvReportIcon1.setClickable(false);
                    String encode1 = photo2Base64(path);
                    if (!isdelete1) {
                        fileNames.add(fileName1);
                        imageBase64Strings.add(encode1);
                    } else {
                        fileNames.set(0, fileName1);
                        imageBase64Strings.set(0, encode1);
                    }
                }
            } else if (requestCode == 2) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

//                    bitmap = getBitmap(mIvphoto2, fileResult);
                }
                if (bitmap == null) {
                    return;
                } else {
                    randomIvReportIcon2.setImageBitmap(bitmap);
                    String fileName2 = saveToSDCard(bitmap, 2);
                    //将选择的图片设置到控件上
                    randomIvReportIcon2.setClickable(false);
                    String encode2 = photo2Base64(path);
                    if (!isdelete2) {
                        fileNames.add(fileName2);
                        imageBase64Strings.add(encode2);
                    } else {
                        fileNames.set(1, fileName2);
                        imageBase64Strings.set(1, encode2);
                    }

                }
            } else if (requestCode == 3) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");

                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

//                    bitmap = getBitmap(mIvphoto1, fileResult);
                }
                if (bitmap == null) {
                    return;
                } else {
                    randomIvReportIcon3.setImageBitmap(bitmap);
                    String fileName3 = saveToSDCard(bitmap, 3);
                    //将选择的图片设置到控件上
                    randomIvReportIcon3.setClickable(false);
                    String encode3 = photo2Base64(path);
                    if (!isdelete3) {
                        fileNames.add(fileName3);
                        imageBase64Strings.add(encode3);
                    } else {
                        fileNames.set(2, fileName3);
                        imageBase64Strings.set(2, encode3);
                    }

                }
            }
        }
        //新加的
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static String photo2Base64(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            byte[] encode = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
            String uploadBuffer = new String(encode);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.randomreprote);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(locationClient != null){
            locationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationClient != null){
            locationClient.stop();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationClient != null){
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
        }
    }
}
