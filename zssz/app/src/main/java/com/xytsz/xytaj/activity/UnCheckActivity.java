package com.xytsz.xytaj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.kobjects.base64.Base64;
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
 * Created by admin on 2017/2/17.
 * 验收核实
 */
public class UnCheckActivity extends AppCompatActivity {


    private static final int IS_PHOTO_SUCCESS1 = 10000003;
    private static final int IS_PHOTO_SUCCESS3 = 10000005;
    private static final int IS_PHOTO_SUCCESS2 = 10000004;
    @Bind(R.id.uncheck_progressbar)
    LinearLayout uncheckProgressbar;

    private boolean isPostFirst;
    @Bind(R.id.iv_predeal_icon1)
    ImageView ivPredealIcon1;
    @Bind(R.id.iv_predeal_icon2)
    ImageView ivPredealIcon2;
    @Bind(R.id.iv_predeal_icon3)
    ImageView ivPredealIcon3;
    @Bind(R.id.bt_uncheck_predeal)
    Button btUncheckPredeal;
    @Bind(R.id.iv_dealing_icon1)
    ImageView ivDealingIcon1;
    @Bind(R.id.iv_dealing_icon2)
    ImageView ivDealingIcon2;
    @Bind(R.id.iv_dealing_icon3)
    ImageView ivDealingIcon3;
    @Bind(R.id.bt_uncheck_dealing)
    Button btUncheckDealing;
    @Bind(R.id.iv_dealed_icon1)
    ImageView ivDealedIcon1;
    @Bind(R.id.iv_dealed_icon2)
    ImageView ivDealedIcon2;
    @Bind(R.id.iv_dealed_icon3)
    ImageView ivDealedIcon3;
    @Bind(R.id.bt_uncheck_dealed)
    Button btUncheckDealed;
    @Bind(R.id.et_repair_statu)
    EditText etRepairStatu;

    private Review reviewRoadDetail;
    private static final int ISPOST = 10000001;
    private boolean isPostSecond;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case GlobalContanstant.CHECKFAIL:
                    ToastUtil.shortToast(getApplicationContext(), "上传失败");
                    uncheckProgressbar.setVisibility(View.GONE);
                    btUncheckDealed.setVisibility(View.VISIBLE);
                    break;

                case ISPOST:
                    String isPost = (String) msg.obj;
                    if (isPost != null) {
                        if (isPost.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "上传图片中");
                            new Thread() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < fileNamesss.size(); i++) {
                                        diseaseInformation.photoName = fileNamesss.get(i);
                                        diseaseInformation.encode = imageBase64Stringsss.get(i);
                                        diseaseInformation.diviceNum = taskNumber;

                                        try {
                                            isphotoSuccess = connectWebService(diseaseInformation, GlobalContanstant.GETPOST);
                                        } catch (Exception e) {
                                            Message message = Message.obtain();
                                            message.what = GlobalContanstant.CHECKFAIL;
                                            handler.sendMessage(message);
                                        }


                                    }
                                    Message message = Message.obtain();
                                    message.what = IS_PHOTO_SUCCESS3;
                                    message.obj = isphotoSuccess;
                                    handler.sendMessage(message);

                                }
                            }.start();
                        } else {
                            ToastUtil.shortToast(getApplicationContext(), "上传失败");
                            uncheckProgressbar.setVisibility(View.GONE);
                            btUncheckDealed.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "上传失败");
                        uncheckProgressbar.setVisibility(View.GONE);
                        btUncheckDealed.setVisibility(View.VISIBLE);
                    }
                    break;
                case IS_PHOTO_SUCCESS1:
                    String isphotoSuccess = (String) msg.obj;
                    if (isphotoSuccess != null) {
                        if (isphotoSuccess.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "处置前照片上报成功");
                            btUncheckPredeal.setVisibility(View.GONE);
                            isPostFirst = true;
                            ivPredealIcon1.setEnabled(false);
                            ivPredealIcon2.setEnabled(false);
                            ivPredealIcon3.setEnabled(false);
                        } else {
                            imageBase64Strings.clear();
                            ToastUtil.shortToast(getApplicationContext(), "照片上报失败");
                        }
                    } else {
                        imageBase64Strings.clear();
                        ToastUtil.shortToast(getApplicationContext(), "照片上报失败");
                    }

                    break;
                case IS_PHOTO_SUCCESS2:
                    String isphotoSuccess1 = (String) msg.obj;
                    if (isphotoSuccess1 != null) {
                        if (isphotoSuccess1.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "处置中照片上报成功");
                            btUncheckDealing.setVisibility(View.GONE);
                            isPostSecond = true;
                            ivDealingIcon1.setEnabled(false);
                            ivDealingIcon2.setEnabled(false);
                            ivDealingIcon3.setEnabled(false);
                        } else {
                            imageBase64Stringss.clear();
                            ToastUtil.shortToast(getApplicationContext(), "照片上报失败");
                        }
                    } else {
                        imageBase64Stringss.clear();
                        ToastUtil.shortToast(getApplicationContext(), "照片上报失败");
                    }

                    break;
                case IS_PHOTO_SUCCESS3:
                    String isphotoSuccess2 = (String) msg.obj;
                    if (isphotoSuccess2 != null) {
                        if (isphotoSuccess2.equals("true")) {
                            goHome();
                            uncheckProgressbar.setVisibility(View.GONE);

                            ToastUtil.shortToast(getApplicationContext(), "报验成功");
                        } else {
                            uncheckProgressbar.setVisibility(View.GONE);
                            btUncheckDealed.setVisibility(View.VISIBLE);
                            imageBase64Stringsss.clear();
                            ToastUtil.shortToast(getApplicationContext(), "照片上报失败");
                        }
                    } else {
                        uncheckProgressbar.setVisibility(View.GONE);
                        btUncheckDealed.setVisibility(View.VISIBLE);
                        imageBase64Stringsss.clear();
                        ToastUtil.shortToast(getApplicationContext(), "图片未上传");
                    }
                    break;
            }
        }
    };
    private int personID;
    private String path;
    private String taskNumber;
    private String isphotoSuccess;
    private String dialogtitle;
    private Uri fileUri;
    private String fileResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fileResult = savedInstanceState.getString("file_path");
        }

        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            reviewRoadDetail = (Review) getIntent().getSerializableExtra("reviewRoadDetail");
        }

        //获取当前登陆人的ID
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);


        setContentView(R.layout.activity_uncheck);
        ButterKnife.bind(this);

        initAcitionbar();
        dialogtitle = this.getString(R.string.report_dialog_title);
        initData();
    }

    private String[] items = new String[]{"拍照", "照片"};

    private void initData() {

        diseaseInformation = new DiseaseInformation();
        //进入页面 开启线程 去请求网络是否有处置前 和处置中照片
        taskNumber = reviewRoadDetail.getDeciceCheckNum();


        new Thread() {
            @Override
            public void run() {
                //处置前

                try {
                    //根据taskNumber 获取url
                    String preDealJson = getPreImgUrl(taskNumber);

                    //如果有值 先赋值  不能点击
                    if (preDealJson != null) {
                        final List<ImageUrl> imageUrlList = JsonUtil.jsonToBean(preDealJson,
                                new TypeToken<List<ImageUrl>>() {
                                }.getType());


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                switch (imageUrlList.size()) {
                                    //如果没有处置前的图片 都不能点击

                                    case 0:
                                        btUncheckDealing.setEnabled(false);
                                        btUncheckPredeal.setEnabled(false);
                                        btUncheckDealed.setEnabled(false);
                                        break;
                                    //有图片的的时候  处置中和处置后的不能点
                                    case 1:
                                        isPostFirst = true;
                                        btUncheckDealed.setEnabled(false);
                                        btUncheckPredeal.setVisibility(View.INVISIBLE);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(ivPredealIcon1);
                                        ivPredealIcon1.setEnabled(false);
                                        ivPredealIcon2.setVisibility(View.INVISIBLE);
                                        ivPredealIcon3.setVisibility(View.INVISIBLE);
                                        break;
                                    case 2:
                                        isPostFirst = true;
                                        btUncheckDealed.setFocusable(false);
                                        btUncheckPredeal.setVisibility(View.INVISIBLE);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(ivPredealIcon1);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(1).getImgurl()).into(ivPredealIcon2);
                                        ivPredealIcon1.setEnabled(false);
                                        ivPredealIcon2.setEnabled(false);
                                        ivPredealIcon3.setEnabled(false);
                                        ivPredealIcon3.setVisibility(View.INVISIBLE);
                                        break;
                                    case 3:
                                        isPostFirst = true;
                                        btUncheckDealed.setEnabled(false);
                                        btUncheckPredeal.setVisibility(View.INVISIBLE);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(0).getImgurl()).into(ivPredealIcon1);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(1).getImgurl()).into(ivPredealIcon2);
                                        Glide.with(getApplicationContext()).load(imageUrlList.get(2).getImgurl()).into(ivPredealIcon3);
                                        ivPredealIcon1.setEnabled(false);
                                        ivPredealIcon2.setEnabled(false);
                                        ivPredealIcon3.setEnabled(false);
                                        break;
                                }


                            }
                        });


                    }


                    //处置中
                    final String dealingJson = getRngImgUrl(taskNumber);
                    //如果有值 先赋值  不能点击
                    if (dealingJson != null) {
                        final List<ImageUrl> imageIngUrlList = JsonUtil.jsonToBean(dealingJson, new TypeToken<List<ImageUrl>>() {
                        }.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                switch (imageIngUrlList.size()) {
                                    case 0:
                                        btUncheckDealing.setEnabled(false);
                                        btUncheckDealed.setEnabled(false);
                                        break;
                                    case 1:
                                        btUncheckDealing.setVisibility(View.INVISIBLE);
                                        btUncheckDealed.setEnabled(false);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(0).getImgurl()).into(ivDealingIcon1);
                                        ivDealingIcon1.setEnabled(false);
                                        ivDealingIcon2.setVisibility(View.INVISIBLE);
                                        ivDealingIcon2.setEnabled(false);
                                        ivDealingIcon3.setVisibility(View.INVISIBLE);
                                        ivDealingIcon3.setEnabled(false);
                                        isPostFirst = true;
                                        isPostSecond = true;
                                        break;
                                    case 2:
                                        isPostFirst = true;
                                        isPostSecond = true;
                                        btUncheckDealing.setVisibility(View.INVISIBLE);
                                        btUncheckDealed.setEnabled(false);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(0).getImgurl()).into(ivDealingIcon1);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(1).getImgurl()).into(ivDealingIcon2);
                                        ivDealingIcon1.setEnabled(false);
                                        ivDealingIcon2.setEnabled(false);
                                        ivDealingIcon3.setVisibility(View.INVISIBLE);
                                        ivDealingIcon3.setEnabled(false);

                                        break;
                                    case 3:
                                        isPostFirst = true;
                                        isPostSecond = true;
                                        btUncheckDealing.setVisibility(View.INVISIBLE);
                                        btUncheckDealed.setEnabled(false);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(0).getImgurl()).into(ivDealingIcon1);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(1).getImgurl()).into(ivDealingIcon2);
                                        Glide.with(getApplicationContext()).load(imageIngUrlList.get(2).getImgurl()).into(ivDealingIcon3);
                                        ivDealingIcon1.setEnabled(false);
                                        ivDealingIcon2.setEnabled(false);
                                        ivDealingIcon3.setEnabled(false);
                                        break;
                                }

                            }
                        });


                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);

                }

            }
        }.start();


    }

    /**
     * 获取到处置中和处置前的照片
     *
     * @param taskNumber ：单号
     * @return json
     */
    private String getPreImgUrl(String taskNumber) throws Exception {
        SoapObject soapobject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapobject.addProperty("DeciceCheckNum", taskNumber);
        soapobject.addProperty("PhaseId", GlobalContanstant.GETNOTIFY);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapobject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getPreImageURLSoap_Action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;
    }

    /**
     * 获取处置中的照片
     *
     * @param taskNumber:danhao
     * @return ：json
     * @throws Exception
     */
    private String getRngImgUrl(String taskNumber) throws Exception {
        SoapObject soapobject = new SoapObject(NetUrl.nameSpace, NetUrl.getAllImageURLmethodName);
        soapobject.addProperty("DeciceCheckNum", taskNumber);
        soapobject.addProperty("PhaseId", GlobalContanstant.GETSEND);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapobject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getRngImageURLSoap_Action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;
    }

    private String connectWebService(DiseaseInformation diseaseInformation, int phaseIndication) throws Exception {
        //构建初始化soapObject
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.diviceNum);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        soapObject.addProperty("PhaseId", phaseIndication);


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

    /**
     * 给拍的照片命名
     */
    public String createPhotoName() {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String fileName = format.format(date) + ".jpg";
        return fileName;
    }


    private String toManagement(int phaseIndication, Review reviewRoadDetail) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.dealmethodName);
        soapObject.addProperty("id", reviewRoadDetail.getId());
        soapObject.addProperty("opinion", reviewRoadDetail.getWXInfo());
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("state", phaseIndication);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;


        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }


    private DiseaseInformation diseaseInformation;
    private static final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/UncheckImage/";//图片的存储目录

    public String saveToSDCard(Bitmap bitmap) {
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

    private String photo2Base64(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = Base64.encode(baos.toByteArray()) + "";
            Log.i("upload", uploadBuffer);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String Tag = "com.xytsz.xytaj.fileprovider";

    private File file;

    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Zsaj/UncheckImage/demo/";

    private void camera(int position) {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "uncheck" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(UnCheckActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(pathUrl, "uncheck" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = Uri.fromFile(file);
            fileResult = fileUri.getPath();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, position);
    }


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {

                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    File filePath = new File(pathUrl);
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    break;

                case PermissionUtils.CODE_CAMERA:
                    camera(9001);
                    break;

            }
        }
    };



    @OnClick({R.id.iv_predeal_icon1, R.id.iv_predeal_icon2, R.id.iv_predeal_icon3, R.id.bt_uncheck_predeal, R.id.iv_dealing_icon1, R.id.iv_dealing_icon2, R.id.iv_dealing_icon3, R.id.bt_uncheck_dealing, R.id.iv_dealed_icon1, R.id.iv_dealed_icon2, R.id.iv_dealed_icon3, R.id.bt_uncheck_dealed})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_predeal_icon1:

                /*Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                file = new File(getPhotopath(1));
                fileUri = Uri.fromFile(file);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent1, 9001);*/

                PermissionUtils.requestPermission(UnCheckActivity.this,PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE,
                        mPermissionGrant);
                PermissionUtils.requestPermission(UnCheckActivity.this,PermissionUtils.CODE_CAMERA,
                        mPermissionGrant);
                break;
            case R.id.iv_predeal_icon2:
                camera(9002);
                break;
            case R.id.iv_predeal_icon3:
                camera(9003);

                break;

            // 点击上报处置前的照片
            case R.id.bt_uncheck_predeal:
                ToastUtil.shortToast(getApplicationContext(), "正在上传，请稍候");
                new Thread() {

                    @Override
                    public void run() {
                        for (int i = 0; i < fileNames.size(); i++) {
                            diseaseInformation.photoName = fileNames.get(i);
                            diseaseInformation.encode = imageBase64Strings.get(i);
                            diseaseInformation.diviceNum = taskNumber;
                            try {
                                isphotoSuccess = connectWebService(diseaseInformation, GlobalContanstant.GETNOTIFY);
                            } catch (Exception e) {
                                Message message = Message.obtain();
                                message.what = GlobalContanstant.CHECKFAIL;
                                handler.sendMessage(message);
                            }


                        }
                        Message message = Message.obtain();
                        message.what = IS_PHOTO_SUCCESS1;
                        message.obj = isphotoSuccess;
                        handler.sendMessage(message);

                    }
                }.start();

                break;
            case R.id.iv_dealing_icon1:
                camera(9004);
                break;
            case R.id.iv_dealing_icon2:
                camera(9005);
                break;
            case R.id.iv_dealing_icon3:
                camera(9006);
                break;
            //点击上报正在处置图片
            case R.id.bt_uncheck_dealing:
                //点击上报ing的图片的时候先判断是否有上报处置前的照片
                //是否有处置前的照片

                if (isPostFirst) {
                    ToastUtil.shortToast(getApplicationContext(), "正在上传，请稍候");
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < fileNamess.size(); i++) {
                                diseaseInformation.photoName = fileNamess.get(i);
                                diseaseInformation.encode = imageBase64Stringss.get(i);
                                diseaseInformation.diviceNum = taskNumber;
                                Log.i("diviceNum", diseaseInformation.diviceNum);

                                try {
                                    isphotoSuccess = connectWebService(diseaseInformation, GlobalContanstant.GETSEND);
                                } catch (Exception e) {
                                    Message message = Message.obtain();
                                    message.what = GlobalContanstant.CHECKFAIL;
                                    handler.sendMessage(message);
                                }


                            }
                            Message message = Message.obtain();
                            message.what = IS_PHOTO_SUCCESS2;
                            message.obj = isphotoSuccess;
                            handler.sendMessage(message);

                        }
                    }.start();

                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请先上报处置前的照片");
                }
                break;
            case R.id.iv_dealed_icon1:
                camera(9007);
                break;
            case R.id.iv_dealed_icon2:
                camera(9008);
                break;
            case R.id.iv_dealed_icon3:
                camera(9009);
                break;
            case R.id.bt_uncheck_dealed:

                if (isPostFirst) {
                    if (isPostSecond) {
                        uncheckProgressbar.setVisibility(View.VISIBLE);
                        ToastUtil.shortToast(getApplicationContext(), "开始上传");
                        btUncheckDealed.setVisibility(View.GONE);

                        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
                        //维修说明
                        String repair = etRepairStatu.getText().toString();
                        reviewRoadDetail.setWXInfo(repair);


                        new Thread() {
                            @Override
                            public void run() {
                                //to上传信息以及 维修说明
                                try {

                                    String isPost = toManagement(GlobalContanstant.GETCHECK, reviewRoadDetail);

                                    //发信息  实现UI更新
                                    Message message = Message.obtain();
                                    message.what = ISPOST;
                                    message.obj = isPost;
                                    handler.sendMessage(message);

                                } catch (Exception e) {
                                    Message message = Message.obtain();
                                    message.what = GlobalContanstant.CHECKFAIL;
                                    handler.sendMessage(message);
                                }
                            }
                        }.start();


                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "请先上报处置中的照片");

                    }
                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请先上报处置前的照片");

                }


                break;
        }
    }


    /**
     * 处置中的文件名集合
     */
    private List<String> fileNames = new ArrayList<>();
    private List<String> fileNamess = new ArrayList<>();
    private List<String> fileNamesss = new ArrayList<>();
    /**
     * 处置中的base64集合
     */
    private List<String> imageBase64Strings = new ArrayList<>();
    private List<String> imageBase64Stringss = new ArrayList<>();
    private List<String> imageBase64Stringsss = new ArrayList<>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_CANCELED) {
            return;
        }

        Bitmap bitmap;
        String fileName;
        String encode;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 9001:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivPredealIcon1.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivPredealIcon1.setClickable(false);
                    encode = photo2Base64(path);
                    fileNames.add(fileName);
                    imageBase64Strings.add(encode);
                    btUncheckPredeal.setEnabled(true);
                    btUncheckPredeal.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9002:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivPredealIcon2.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivPredealIcon2.setClickable(false);
                    encode = photo2Base64(path);
                    fileNames.add(fileName);
                    imageBase64Strings.add(encode);
                    btUncheckPredeal.setEnabled(true);
                    btUncheckPredeal.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9003:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivPredealIcon3.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivPredealIcon3.setClickable(false);
                    encode = photo2Base64(path);
                    fileNames.add(fileName);
                    imageBase64Strings.add(encode);
                    btUncheckPredeal.setEnabled(true);
                    btUncheckPredeal.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9004:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivDealingIcon1.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealingIcon1.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamess.add(fileName);
                    imageBase64Stringss.add(encode);
                    btUncheckDealing.setEnabled(true);
                    btUncheckDealing.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9005:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivDealingIcon2.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealingIcon2.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamess.add(fileName);
                    imageBase64Stringss.add(encode);
                    btUncheckDealing.setEnabled(true);
                    btUncheckDealing.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9006:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {


                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivDealingIcon3.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealingIcon3.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamess.add(fileName);
                    imageBase64Stringss.add(encode);
                    btUncheckDealing.setEnabled(true);
                    btUncheckDealing.setBackgroundResource(R.drawable.btn_uncheck_press);
                    break;
                case 9007:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }
                    if (bitmap == null) {
                        return;
                    }
                    ivDealedIcon1.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealedIcon1.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamesss.add(fileName);
                    imageBase64Stringsss.add(encode);
                    btUncheckDealed.setEnabled(true);
                    btUncheckDealed.setBackgroundResource(R.drawable.shape_btn_uncheck_press);
                    break;
                case 9008:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }

                    if (bitmap == null) {
                        return;
                    }
                    ivDealedIcon2.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealedIcon2.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamesss.add(fileName);
                    imageBase64Stringsss.add(encode);
                    btUncheckDealed.setEnabled(true);
                    btUncheckDealed.setBackgroundResource(R.drawable.shape_btn_uncheck_press);
                    break;
                case 9009:
                    if (data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {

                        bitmap = BitmapUtil.getScaleBitmap(fileResult);

                    }

                    if (bitmap == null) {
                        return;
                    }
                    ivDealedIcon3.setImageBitmap(bitmap);
                    fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    ivDealedIcon3.setClickable(false);
                    encode = photo2Base64(path);
                    fileNamesss.add(fileName);
                    imageBase64Stringsss.add(encode);
                    btUncheckDealed.setEnabled(true);
                    btUncheckDealed.setBackgroundResource(R.drawable.shape_btn_uncheck_press);
                    break;

            }
        }


        super.onActivityResult(requestCode, resultCode, data);


    }


    private void goHome() {
        Intent intent = new Intent(UnCheckActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        startActivity(intent);
        finish();
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.post);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(UnCheckActivity.this,requestCode,permissions,grantResults,mPermissionGrant);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (Build.VERSION.SDK_INT >= 24) {
            outState.putString("file_path", fileResult);
        } else {
            outState.putString("file_path", fileResult);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }
}


