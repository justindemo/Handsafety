package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/6/16.
 * 病害详情单
 */
public class SendRoadDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SUCCESS = 500;

    @Bind(R.id.tv_send_detail_reportetime)
    TextView tvSendDetailReportetime;
    @Bind(R.id.tv_send_detail_address)
    TextView tvSendDetailAddress;
    @Bind(R.id.iv_send_detail_photo1)
    ImageView ivSendDetailPhoto1;
    @Bind(R.id.iv_send_detail_photo2)
    ImageView ivSendDetailPhoto2;
    @Bind(R.id.iv_send_detail_photo3)
    ImageView ivSendDetailPhoto3;
    @Bind(R.id.tv_send_problem_audio)
    TextView tvSendProblemAudio;
    @Bind(R.id.iv_play_video)
    ImageView ivPlayVideo;
    @Bind(R.id.ll_video)
    LinearLayout llVideo;
    @Bind(R.id.tv_send_detail_diseasedes)
    TextView tvSendDetailDiseasedes;
    @Bind(R.id.tv_send_detail_facility)
    TextView tvSendDetailFacility;
    @Bind(R.id.tv_send_detail_facility_person)
    TextView tvSendDetailFacilityPerson;
    @Bind(R.id.tv_send_detail_facility_problem)
    TextView tvSendDetailFacilityProblem;
    @Bind(R.id.tv_send_pass)
    TextView tvSendPass;
    @Bind(R.id.tv_send_detail_facility_loca)
    TextView tvSendDetailFacilityLoca;
    @Bind(R.id.tv_send_detail_facility_team)
    TextView tvSendDetailFacilityTeam;
    @Bind(R.id.tv_send_detail_mine_advice)
    EditText tvSendDetailMineAdvice;
    @Bind(R.id.ll_send_mine_advice)
    LinearLayout llSendMineAdvice;
    @Bind(R.id.ll_road_idea)
    LinearLayout llRoadIdea;
    @Bind(R.id.iv_review_icon1)
    ImageView ivReviewIcon1;
    @Bind(R.id.iv_review_icon2)
    ImageView ivReviewIcon2;
    @Bind(R.id.iv_review_icon3)
    ImageView ivReviewIcon3;
    @Bind(R.id.ll_review_img)
    LinearLayout llReviewImg;
    @Bind(R.id.sendroad_progressbar)
    LinearLayout sendroadProgressbar;
    @Bind(R.id.space_view)
    View spaceView;
    @Bind(R.id.tv_check_pass)
    TextView tvCheckPass;
    @Bind(R.id.tv_check_back)
    TextView tvCheckBack;
    @Bind(R.id.ll_check_idea)
    LinearLayout llCheckIdea;

    private Review detail;
    private List<ImageUrl> imageUrls;
    private int id;
    private AudioUrl audioUrl;
    private SoundUtil soundUtil;
    private String videopath;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.IMAGEFAIL:
                    sendroadProgressbar.setVisibility(View.GONE);
                    if (tag == GlobalContanstant.REVIEW) {
                        llCheckIdea.setVisibility(View.VISIBLE);
                    } else {
                        llRoadIdea.setVisibility(View.VISIBLE);
                    }
                    ToastUtil.shortToast(getApplicationContext(), "图片上传失败");
                    return;
                case GlobalContanstant.REVIEWSTATEFAIL:
                    sendroadProgressbar.setVisibility(View.GONE);
                    if (tag == GlobalContanstant.REVIEW) {
                        llCheckIdea.setVisibility(View.VISIBLE);
                    } else {
                        llRoadIdea.setVisibility(View.VISIBLE);
                    }
                    ToastUtil.shortToast(getApplicationContext(), "数据上传失败");
                    return;
                case GlobalContanstant.REVIEWSTATESUCCESS:
                    String resultstate = (String) msg.obj;
                    if (resultstate.equals("true")) {
                        ToastUtil.shortToast(getApplicationContext(), "数据上传成功");
                        sendroadProgressbar.setVisibility(View.GONE);
                        Intent intent = getIntent();
                        intent.putExtra("position", position);
                        setResult(302, intent);
                        finish();
                    }

                    break;
                case SUCCESS:
                    videopath = (String) msg.obj;
                    if (videopath.isEmpty() || videopath == null || videopath.equals("false")) {
                        llVideo.setVisibility(View.GONE);
                    } else {
                        llVideo.setVisibility(View.VISIBLE);
                    }

                    break;
                case GlobalContanstant.CHECKROADPASS:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            sendroadProgressbar.setVisibility(View.GONE);
                            ToastUtil.shortToast(getApplicationContext(), "提交成功");
                            Intent intent = getIntent();
                            intent.putExtra("position", position);
                            setResult(301, intent);
                            finish();
                        }
                    }
                    break;
                case GlobalContanstant.CHECKROADFAIL:
                    sendroadProgressbar.setVisibility(View.GONE);
                    if (tag == GlobalContanstant.REVIEW) {
                        llCheckIdea.setVisibility(View.VISIBLE);
                    } else {
                        llRoadIdea.setVisibility(View.VISIBLE);
                    }
                    ToastUtil.shortToast(getApplicationContext(), "数据上传出错");
                    break;
            }
        }
    };

    private int position;
    private int tag;
    private int personID;
    private static final int Take_Photo = 100;
    private static final String Tag = "com.xytsz.xytaj.fileprovider";
    private String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Zsaj/Image/check/";
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    File file = new File(filepath);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    break;
                case PermissionUtils.CODE_CAMERA:
                    camera(100);
                    break;
            }
        }
    };
    private int role;
    private File file;
    private Uri fileUri;
    private DiseaseInformation diseaseInformation;
    private String isphotoSuccess1;
    private String mineAdvice;
    private String fileResult;
    private Bitmap scaledBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fileResult = savedInstanceState.getString("file_path");
        }
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            detail = (Review) getIntent().getSerializableExtra("detail");
            imageUrls = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrls");
            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");
            position = getIntent().getIntExtra("position", -1);
            tag = getIntent().getIntExtra("tag", -1);
        }

        setContentView(R.layout.activity_sendroaddetail);
        ButterKnife.bind(this);

        initAcitionbar();

        initData();
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.problem_detail);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initData() {
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);

        diseaseInformation = new DiseaseInformation();
        tvSendDetailFacility.setText(detail.getDeviceName());
        tvSendDetailFacilityPerson.setText(detail.getCheckPersonName());
        tvSendDetailFacilityTeam.setText(detail.getDeptName());
        StringBuilder stringBuilder = new StringBuilder();
        List<String> errorInfo = detail.getErrorInfo();


        if (errorInfo == null || errorInfo.size() == 0) {
            tvSendDetailFacilityProblem.setText("正常");
        } else {
            for (String s : errorInfo) {
                stringBuilder.append(s).append(";");
            }
            String problem = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
            tvSendDetailFacilityProblem.setText(problem);
        }


        tvSendDetailReportetime.setText(detail.getCheckTime());
        tvSendDetailAddress.setText(detail.getRemarks());
        tvSendDetailFacilityLoca.setText(detail.getAddressInfo());


        if (imageUrls.size() != 0) {
            if (imageUrls.size() == 1) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                ivSendDetailPhoto2.setVisibility(View.INVISIBLE);
                ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 2) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 3) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                Glide.with(getApplicationContext()).load(imageUrls.get(2).getImgurl()).into(ivSendDetailPhoto3);
            }
        } else {
            ivSendDetailPhoto1.setVisibility(View.VISIBLE);
            ivSendDetailPhoto1.setVisibility(View.INVISIBLE);
            ivSendDetailPhoto1.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(ivSendDetailPhoto1);

        }

        ivSendDetailPhoto1.setOnClickListener(this);
        ivSendDetailPhoto2.setOnClickListener(this);
        ivSendDetailPhoto3.setOnClickListener(this);


        if (detail.getRemarks().isEmpty()) {
            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {

                        if (!audioUrl.getTime().isEmpty()) {
                            tvSendDetailAddress.setVisibility(View.GONE);
                            tvSendProblemAudio.setVisibility(View.VISIBLE);
                            soundUtil = new SoundUtil();
                            tvSendProblemAudio.setText(audioUrl.getTime());

                            tvSendProblemAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                    final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                    tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


                                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                        @Override
                                        public void onFinish() {
                                            tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });

                                    soundUtil.play(audioUrl.getAudioUrl());
                                }
                            });
                        }
                    } else {
                        tvSendDetailAddress.setVisibility(View.VISIBLE);
                        tvSendProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                tvSendDetailAddress.setVisibility(View.VISIBLE);
                tvSendProblemAudio.setVisibility(View.GONE);
            }

        } else {
            tvSendDetailAddress.setVisibility(View.VISIBLE);
            tvSendProblemAudio.setVisibility(View.GONE);
        }

        switch (tag) {
            //代表检查
            case GlobalContanstant.REVIEW:

                //检查图片可以看到
                llReviewImg.setVisibility(View.VISIBLE);
                llSendMineAdvice.setVisibility(View.GONE);
                //然后显示对应的节点的上传的意见
                break;
            //代表整改
            case GlobalContanstant.NOTIFY:

                spaceView.setVisibility(View.VISIBLE);
                llRoadIdea.setVisibility(View.VISIBLE);
                llCheckIdea.setVisibility(View.GONE);
                break;
            //代表 审批
            case GlobalContanstant.SEND:
                tvSendDetailMineAdvice.setText(detail.getZZCSInfo());
                llRoadIdea.setVisibility(View.GONE);
                llCheckIdea.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SendRoadDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrls);
        startActivity(intent);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (Build.VERSION.SDK_INT >= 24) {
            outState.putString("file_path", file.getAbsolutePath());
        } else {
            outState.putString("file_path", fileUri.getPath());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @OnClick({R.id.iv_play_video, R.id.tv_send_pass, R.id.iv_review_icon1, R.id.iv_review_icon2,
            R.id.iv_review_icon3, R.id.tv_check_pass, R.id.tv_check_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_play_video:
                Intent intent1 = new Intent(SendRoadDetailActivity.this, PlayVideoActivity.class);
                intent1.putExtra("videoPath", videopath);
                startActivity(intent1);
                break;
            case R.id.tv_send_pass:
                //拿到意见
                mineAdvice = tvSendDetailMineAdvice.getText().toString().trim();
                switch (tag) {
                    //代表检查
                    case GlobalContanstant.REVIEW:
                        //然后显示对应的节点的上传的意见
                        break;
                    //代表整改
                    case GlobalContanstant.NOTIFY:
                        sendroadProgressbar.setVisibility(View.VISIBLE);

                        llRoadIdea.setVisibility(View.GONE);
                        UpData(mineAdvice);
                        break;
                    //代表 审批
                    case GlobalContanstant.SEND:

                        break;
                }

                break;

            case R.id.tv_check_pass:
                sendroadProgressbar.setVisibility(View.VISIBLE);
                llRoadIdea.setVisibility(View.GONE);
                llCheckIdea.setVisibility(View.GONE);
                upImage(true);

                break;
            case R.id.tv_check_back:
                sendroadProgressbar.setVisibility(View.VISIBLE);
                llRoadIdea.setVisibility(View.GONE);
                llCheckIdea.setVisibility(View.GONE);
                upImage(false);

                break;

            case R.id.iv_review_icon1:

                PermissionUtils.requestPermission(SendRoadDetailActivity.this,
                            PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
                PermissionUtils.requestPermission(SendRoadDetailActivity.this,
                            PermissionUtils.CODE_CAMERA, mPermissionGrant);


                break;
            case R.id.iv_review_icon2:

                camera(200);
                break;
            case R.id.iv_review_icon3:

                camera(300);

                break;
        }
    }


    private String result;

    private void upImage(final boolean t) {

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < fileNames.size(); i++) {
                    diseaseInformation.photoName = fileNames.get(i);
                    diseaseInformation.encode = imageBase64Strings.get(i);

                    try {
                        isphotoSuccess1 = connectWebService(diseaseInformation);

                    } catch (Exception e) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.IMAGEFAIL;
                        handler.sendMessage(message);
                    }

                }

                if (isphotoSuccess1 != null) {
                    if (isphotoSuccess1.equals("true")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.shortToast(getApplicationContext(), "图片上传成功");
                            }
                        });

                        try {
                            if (t) {
                                result = upState(detail.getId(), personID);
                            } else {
                                result = back(detail.getId(), personID);
                            }

                            Message message = Message.obtain();
                            message.obj = result;
                            message.what = GlobalContanstant.REVIEWSTATESUCCESS;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = Message.obtain();
                            message.what = GlobalContanstant.REVIEWSTATEFAIL;
                            handler.sendMessage(message);
                        }
                    }


                }

            }


        }.start();


    }

    private String back(int id, int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.REVIEWSTATEBACKMETHOD);
        //传递的参数
        soapObject.addProperty("id", id);
        soapObject.addProperty("personId", personID);  //文件类型

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String result = object.getProperty(0).toString();
        return result;
    }

    private String upState(int id, int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.REVIEWSTATEMETHOD);
        //传递的参数
        soapObject.addProperty("id", id);
        soapObject.addProperty("personId", personID);  //文件类型

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String result = object.getProperty(0).toString();
        return result;
    }

    private String connectWebService(DiseaseInformation diseaseInformation) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", detail.getDeciceCheckNum());
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        ///
        soapObject.addProperty("PhaseId", GlobalContanstant.REVIEWPICTURE);

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String isphotoSuccess = object.getProperty(0).toString();
        return isphotoSuccess;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    private void camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "checkimag" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(SendRoadDetailActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(filepath, "checkimag" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = Uri.fromFile(file);
            fileResult = fileUri.getPath();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, position);
    }



    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();

    private Bitmap bitmap;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Take_Photo:
                    if (data != null) {
                       bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
//                        firstbitmap = ReportActivity.getBitmap(ivReviewIcon1, fileResult);
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);


                    }
                    if (bitmap == null){
                        return;
                    }

                    ivReviewIcon1.setImageBitmap(bitmap);
                    String fileName1 = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    String encode1 = ReportActivity.photo2Base64(path);
                    ivReviewIcon1.setClickable(false);
                    fileNames.add(fileName1);
                    imageBase64Strings.add(encode1);
                    break;

                case 200:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                    } else {
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null){
                        return;
                    }
                    ivReviewIcon2.setImageBitmap(bitmap);
                    String fileName2 = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivReviewIcon2.setClickable(false);
                    String encode2 = ReportActivity.photo2Base64(path);
                    fileNames.add(fileName2);
                    imageBase64Strings.add(encode2);
                    break;
                case 300:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                    } else {
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null){
                        return;
                    }
                    ivReviewIcon3.setImageBitmap(bitmap);
                    String fileName3 = saveToSDCard(bitmap);
                    ivReviewIcon3.setClickable(false);
                    //将选择的图片设置到控件上
                    String encode3 = ReportActivity.photo2Base64(path);
                    fileNames.add(fileName3);
                    imageBase64Strings.add(encode3);

                    break;
            }


        }
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        String fileName = format.format(date)  +".jpg";
        return fileName;

    }


    private void UpData(final String advice) {
        new Thread() {
            @Override
            public void run() {

                try {
                    String data = toNet(advice);
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKROADPASS;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKROADFAIL;
                    handler.sendMessage(message);
                }

            }
        }.start();
    }

    //选择一个
    private String toNet(String advice) throws Exception {
        //发通知节点//哪个单子，状态，通知人是谁，意见是什么,时间是否需要。
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.dealmethodName);
        soapObject.addProperty("id", detail.getId());
        soapObject.addProperty("opinion", advice);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("state", GlobalContanstant.NOTIFY);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String data = object.getProperty(0).toString();

        return data;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (Build.VERSION.SDK_INT >= 24) {
            outState.putString("file_path", fileResult);

        } else {
            outState.putString("file_path", fileResult);

        }
        super.onSaveInstanceState(outState);
    }
}
