package com.xytsz.xytaj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.MoringCheckAdapter;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.bean.ReportData;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
public class MoringSignActivity extends AppCompatActivity {

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
                case GlobalContanstant.PERSONSIGNFAIL:
                    ToastUtil.shortToast(getApplicationContext(), "数据未获取");
                    morningProgressbar.setVisibility(View.GONE);
                    break;
                case GlobalContanstant.PERSONSIGNSUCCESS:
                    morningProgressbar.setVisibility(View.GONE);
                    llSign.setVisibility(View.VISIBLE);
                    reportSign.setVisibility(View.VISIBLE);
                    String data = (String) msg.obj;
                    persons = JsonUtil.jsonToBean(data, new TypeToken<List<Person>>() {
                    }.getType());

                    personlist = new String[persons.size()];
                    for (int i = 0; i < personlist.length; i++) {
                        personlist[i] = persons.get(i).getName();
                    }

                    checkItems.clear();
                    checkItems.add("正常");
                    checkItems.add("未戴工号牌");
                    checkItems.add("未穿工作服");
                    checkItems.add("未穿袖章");
                    checkItems.add("未穿拖鞋");
                    checkItems.add("未戴口罩");

                    moringCheckAdapter = new MoringCheckAdapter(checkItems);
                    strandLv.setAdapter(moringCheckAdapter);
                    moringCheckAdapter.setCheckItems(diseaseInformation);
                    moringCheckAdapter.notifyDataSetChanged();

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
        }
        setContentView(R.layout.activity_morningsign);
        ButterKnife.bind(this);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
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

        initData();
        diseaseInformation = new DiseaseInformation();


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
        new Thread() {
            @Override
            public void run() {
                try {
                    String data = getData();
                    if (data != null) {
                        Message message = Message.obtain();
                        message.obj = data;
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

    private String getData() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);
        soapObject.addProperty("ID", personID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }

    private String problemtext;
    private final String Tag = "com.xytsz.xytaj.fileprovider";
    private static final int Take_Photo = 1;
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {

        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {

                case PermissionUtils.CODE_CAMERA:
                    file = new File(getExternalCacheDir(),"personsign.jpg");
                    try {
                        if (file.exists()){
                        file.delete();
                    }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        fileUri = FileProvider.getUriForFile(MoringSignActivity.this, Tag, file);
                    } else {
                        fileUri =Uri.fromFile(file);
                    }


                    Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent1, Take_Photo);
                    break;

//
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Take_Photo:
                if (resultCode == RESULT_OK){

                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    if (Build.VERSION.SDK_INT >= 24){
                        bitmap = ReportActivity.getBitmap(ivSignPicture,file.getAbsolutePath());
                    }else {

                        bitmap = ReportActivity.getBitmap(ivSignPicture, fileUri.getPath());
                    }
                    ivSignPicture.setImageBitmap(bitmap);
                    diseaseInformation.fileName =saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    diseaseInformation.encode= ReportActivity.photo2Base64(path);

                }

                break;
        }

    }



    private final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Zsaj/Image/";
    private String path;
    private String saveToSDCard(Bitmap bitmap) {
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

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);//把图片数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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
            ToastUtil.shortToast(getApplicationContext(),"SD卡不存在");
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

                new AlertDialog.Builder(MoringSignActivity.this).setSingleChoiceItems(personlist, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = personlist[which];
                                tvSignPerson.setText(name);
                                dialog.dismiss();
                            }
                        }
                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.iv_sign_picture:
                //添加照片
                PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
                PermissionUtils.requestPermission(MoringSignActivity.this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);


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
                    problemtext = "1,1,1,1,1";
                    for (int i = 1; i < checkItemList.size(); i++) {
                        if (checkItemList.get(i).isCheck()) {
                            ToastUtil.shortToast(getApplicationContext(), "检查项选择错误");
                            return;
                        }
                    }
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

        String text = problemtext;
        String string = diseaseInformation.selectPerson;
        //提交服务器；
        // TODO: 2018/3/2
        new Thread() {
            @Override
            public void run() {
                try {
                    //一张图片
                    String imgresult = upImg2Service();
                    if (imgresult != null) {
                        if (imgresult.equals("true")) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.shortToast(getApplicationContext(), "图片上传成功");
                                }
                            });
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
                    }


                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MORNINGIMGFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private String upImg2Service() throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("tag", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        soapObject.addProperty("personid", selectPersonID);

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

    private String up2Service() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        soapObject.addProperty("team", "");
        soapObject.addProperty("leader", personID);
        soapObject.addProperty("selectpersonId", selectPersonID);
        soapObject.addProperty("error", problemtext);

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
}
