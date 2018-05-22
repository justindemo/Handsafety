package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.PermissionUtils;
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
 * Created by admin on 2018/4/12.
 * 培训照片
 */
public class TrainPhotoActivity extends AppCompatActivity {
    @Bind(R.id.iv_train_icon1)
    ImageView ivTrainIcon1;
    @Bind(R.id.iv_train_icon2)
    ImageView ivTrainIcon2;
    @Bind(R.id.iv_train_icon3)
    ImageView ivTrainIcon3;
    @Bind(R.id.train_photo_bt)
    Button trainPhotoBt;
    @Bind(R.id.trainphoto_progressbar)
    LinearLayout trainphotoProgressbar;
    private int trainId;
    private DiseaseInformation diseaseInformation;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.FAIL:
                    trainphotoProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "上传失败");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    trainphotoProgressbar.setVisibility(View.GONE);
                    String result = (String) msg.obj;
                    if (result != null && result.equals("true")) {
                        ToastUtil.shortToast(getApplicationContext(), "上传成功");
                        finish();
                    }
                    break;
            }
        }
    };
    private String isSuccess;
    private String tag;
    private String title;
    private String method;
    private String fileResult;
    private File fileUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            fileResult = savedInstanceState.getString("file_path");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainphoto);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            trainId = getIntent().getIntExtra("trainId", -1);
        }
        switch (tag){
            case "trainsign":
                title = "培训照片";
                method = NetUrl.TrainphotomethodName;
                break;
            case "meetingsign":
                title = "会议照片";
                method = NetUrl.MeetingPhotoMethodName;
                break;
        }
        initActionBar(title);
        diseaseInformation = new DiseaseInformation();
    }

    private void initActionBar(String title) {
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

    private static final String Tag = "com.xytsz.xytaj.fileprovider";
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";
    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Zsaj/Image/mymy/";


    @OnClick({R.id.iv_train_icon1, R.id.iv_train_icon2, R.id.iv_train_icon3, R.id.train_photo_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_train_icon1:
                PermissionUtils.requestPermission(TrainPhotoActivity.this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
                PermissionUtils.requestPermission(TrainPhotoActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);

                break;
            case R.id.iv_train_icon2:
                camera(2);

                break;
            case R.id.iv_train_icon3:
                camera(3);
                break;
            case R.id.train_photo_bt:
                trainphotoProgressbar.setVisibility(View.VISIBLE);
                updata();
                break;
        }


    }

    private void updata() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < fileNames.size(); i++) {
                        diseaseInformation.photoName = fileNames.get(i);
                        diseaseInformation.encode = imageBase64Strings.get(i);

                        isSuccess = up2service();
                    }
                    Message message = Message.obtain();
                    message.obj = isSuccess;
                    message.what = GlobalContanstant.MYSENDSUCCESS;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String up2service() throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        //传递的参数
        soapObject.addProperty("TrainID", trainId);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        ///

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

    private File file;
    private Uri fileUri;

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {

                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    File filePath = new File(pathUrl);
                    if (!filePath.exists()){
                        filePath.mkdirs();
                    }
                    break;

                case PermissionUtils.CODE_CAMERA:

                    camera(1);
                    break;

            }
        }
    };

    private void camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "trainphoto" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(TrainPhotoActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(pathUrl, "trainphoto" + position + ".jpg");
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

    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (Build.VERSION.SDK_INT >= 24){
            outState.putString("file_path",fileResult);
        }else {
            outState.putString("file_path",fileResult);
        }
        super.onSaveInstanceState(outState, outPersistentState);
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

        //当data为空的时候，不做任何处理
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null){
                    bitmap = (Bitmap) data.getExtras().get("data");
                }else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

                }
                if (bitmap == null){
                    return;
                }
                ivTrainIcon1.setImageBitmap(bitmap);
                String fileName1 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                ivTrainIcon1.setClickable(false);
                String encode1 = ReportActivity.photo2Base64(path);
                fileNames.add(fileName1);
                imageBase64Strings.add(encode1);
            } else if (requestCode == 2) {
                if (data != null){
                    bitmap = (Bitmap) data.getExtras().get("data");
                }else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);


                }
                if (bitmap == null){
                    return;
                }

                ivTrainIcon2.setImageBitmap(bitmap);
                String fileName2 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                ivTrainIcon2.setClickable(false);
                String encode2 = ReportActivity.photo2Base64(path);
                fileNames.add(fileName2);
                imageBase64Strings.add(encode2);

            } else if (requestCode == 3) {
                if (data != null){
                    bitmap = (Bitmap) data.getExtras().get("data");
                }else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);


                }
                if (bitmap == null){
                    return;
                }
                ivTrainIcon3.setImageBitmap(bitmap);
                String fileName3 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                ivTrainIcon3.setClickable(false);
                String encode3 = ReportActivity.photo2Base64(path);
                fileNames.add(fileName3);
                imageBase64Strings.add(encode3);

            }
        }
        //新加的

        super.onActivityResult(requestCode,resultCode,data);

    }


    private String path;

    private String createPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String fileName = format.format(date) + ".jpg";
        return fileName;

    }

    private String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName();
        path = filePath + photoName;
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



}
