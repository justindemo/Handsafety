package com.xytsz.xytaj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.xytsz.xytaj.util.JsonUtil;
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
public class UnCheckActivity extends AppCompatActivity implements View.OnClickListener {


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(iconPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = iconPath + photoName;
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);//把图片数据写入文件
                photo2Base64(path);
            } catch (FileNotFoundException e) {

            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

    @OnClick({R.id.iv_predeal_icon1, R.id.iv_predeal_icon2, R.id.iv_predeal_icon3, R.id.bt_uncheck_predeal, R.id.iv_dealing_icon1, R.id.iv_dealing_icon2, R.id.iv_dealing_icon3, R.id.bt_uncheck_dealing, R.id.iv_dealed_icon1, R.id.iv_dealed_icon2, R.id.iv_dealed_icon3, R.id.bt_uncheck_dealed})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_predeal_icon1:

                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(getPhotopath(1));
                fileUri = Uri.fromFile(file);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent1, 9001);

               /* new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9011);
                                break;
                        }
                    }
                }).create().show();*/
                break;
            case R.id.iv_predeal_icon2:

                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file1 = new File(getPhotopath(2));
                fileUri = Uri.fromFile(file1);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent2, 9002);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9012);
                                break;
                        }
                    }
                }).create().show();*/
                break;
            case R.id.iv_predeal_icon3:

                Intent intent3 = new Intent("android.media.action.IMAGE_CAPTURE");

                File file2 = new File(getPhotopath(3));
                fileUri = Uri.fromFile(file2);
                intent3.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(intent3, 9003);
                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9013);
                                break;
                        }
                    }
                }).create().show();*/

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

                Intent intent4 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file3 = new File(getPhotopath(4));
                fileUri = Uri.fromFile(file3);
                intent4.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent4, 9004);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9014);
                                break;
                        }
                    }
                }).create().show();*/

                break;
            case R.id.iv_dealing_icon2:
                Intent intent5 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file4 = new File(getPhotopath(5));
                fileUri = Uri.fromFile(file4);
                intent5.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(intent5, 9005);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9015);
                                break;
                        }
                    }
                }).create().show();*/
                break;
            case R.id.iv_dealing_icon3:

                Intent intent6 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file5 = new File(getPhotopath(6));
                fileUri = Uri.fromFile(file5);
                intent6.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent6, 9006);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9016);
                                break;
                        }
                    }
                }).create().show();*/
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

                Intent intent7 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file6 = new File(getPhotopath(7));
                fileUri = Uri.fromFile(file6);
                intent7.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent7, 9007);

               /* new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9017);
                                break;
                        }
                    }
                }).create().show();*/
                break;
            case R.id.iv_dealed_icon2:
                Intent intent8 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file7 = new File(getPhotopath(8));
                fileUri = Uri.fromFile(file7);
                intent8.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent8, 9008);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9018);
                                break;
                        }
                    }
                }).create().show();*/

                break;
            case R.id.iv_dealed_icon3:
                Intent intent9 = new Intent("android.media.action.IMAGE_CAPTURE");
                File file8 = new File(getPhotopath(9));
                fileUri = Uri.fromFile(file8);
                intent9.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent9, 9009);
                btUncheckDealed.setFocusable(true);

                /*new AlertDialog.Builder(UnCheckActivity.this).setTitle(dialogtitle).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();

                                break;
                            case 1:
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 9019);
                                btUncheckDealed.setFocusable(true);
                                break;
                        }
                    }
                }).create().show();*/
                break;
            case R.id.bt_uncheck_dealed:

                if (isPostFirst) {
                    if (isPostSecond) {
                        uncheckProgressbar.setVisibility(View.VISIBLE);
                        ToastUtil.shortToast(getApplicationContext(),"开始上传");
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

    private String getPhotopath(int i) {
        // 照片全路径
        String fileName;
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zssz/Image/mymy/";
        String imageName = "imageTest" + i + ".jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
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


    private Bitmap getBitmap(ImageView imageView, String path) {
        Bitmap bitmap;
        int width = imageView.getWidth();

        int height = imageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth / width, imageHeight
                / height);

        // Decode the image file into a Bitmap sized to fill the
        // View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path,
                factoryOptions);

        int bitmapDegree = BitmapUtil.getBitmapDegree(path);
        Bitmap rotateBitmap = BitmapUtil.rotateBitmap(bitmap, bitmapDegree);
        return rotateBitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_CANCELED) {
            return;
        }


        Bitmap bitmap;
        String fileName;
        String encode;
        String picturePath;
        if (data == null) {
            switch (requestCode) {
                case 9001:
                    bitmap = getBitmap(ivPredealIcon1, fileUri.getPath());
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
                    bitmap = getBitmap(ivPredealIcon2, fileUri.getPath());
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
                    bitmap = getBitmap(ivPredealIcon3, fileUri.getPath());
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
                    bitmap = getBitmap(ivDealingIcon1, fileUri.getPath());
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

                    bitmap = getBitmap(ivDealingIcon2, fileUri.getPath());
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
                    //bitmap = (Bitmap) data.getExtras().get("data");

                    bitmap = getBitmap(ivDealingIcon3, fileUri.getPath());
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
                    //bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = getBitmap(ivDealedIcon1, fileUri.getPath());
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
                    //bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = getBitmap(ivDealedIcon2, fileUri.getPath());
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
                    //bitmap = (Bitmap) data.getExtras().get("data");

                    bitmap = getBitmap(ivDealedIcon3, fileUri.getPath());
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

        switch (requestCode) {
            case 9011:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivPredealIcon1, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNames.add(fileName);
                imageBase64Strings.add(encode);
                setParameter(bitmap, ivPredealIcon1, btUncheckPredeal);
                break;
            case 9012:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivPredealIcon2, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNames.add(fileName);
                imageBase64Strings.add(encode);
                setParameter(bitmap, ivPredealIcon2, btUncheckPredeal);
                break;
            case 9013:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivPredealIcon3, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNames.add(fileName);
                imageBase64Strings.add(encode);
                setParameter(bitmap, ivPredealIcon3, btUncheckPredeal);
                break;
            case 9014:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealingIcon1, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamess.add(fileName);
                imageBase64Stringss.add(encode);
                setParameter(bitmap, ivDealingIcon1, btUncheckDealing);
                break;
            case 9015:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealingIcon2, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamess.add(fileName);
                imageBase64Stringss.add(encode);
                setParameter(bitmap, ivDealingIcon2, btUncheckDealing);
                break;
            case 9016:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealingIcon3, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamess.add(fileName);
                imageBase64Stringss.add(encode);
                setParameter(bitmap, ivDealingIcon3, btUncheckDealing);
                break;
            case 9017:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealedIcon1, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamesss.add(fileName);
                imageBase64Stringsss.add(encode);
                setParameter(bitmap, ivDealedIcon1, btUncheckDealed);
                break;
            case 9018:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealedIcon2, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamesss.add(fileName);
                imageBase64Stringsss.add(encode);
                setParameter(bitmap, ivDealedIcon2, btUncheckDealed);
                break;
            case 9019:
                picturePath = getPicturePath(data);
                bitmap = getBitmap(ivDealedIcon3, picturePath);
                if (bitmap == null) {
                    ToastUtil.shortToast(getApplicationContext(), "此照片为空,重新选择");
                    return;
                }
                fileName = saveToSDCard(bitmap);
                encode = photo2Base64(path);
                fileNamesss.add(fileName);
                imageBase64Stringsss.add(encode);
                setParameter(bitmap, ivDealedIcon3, btUncheckDealed);
                break;


        }


    }

    private void setParameter(Bitmap bitmap, ImageView imageView,
                              Button btn) {
        //将选择的图片设置到控件上
        imageView.setImageBitmap(bitmap);
        imageView.setClickable(false);

        btn.setEnabled(true);
        if (btn.getId() == R.id.bt_uncheck_dealed) {
            btn.setBackgroundResource(R.drawable.shape_btn_uncheck_press);
        } else {
            btn.setBackgroundResource(R.drawable.btn_uncheck_press);
        }

    }

    private String getPicturePath(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        }
        return null;
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

}


