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
public class MoringSignActivity extends AppCompatActivity implements SearchView.SearchViewListener {

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
                    ToastUtil.shortToast(getApplicationContext(),"上传失败");
                    break;
                case GlobalContanstant.CHECKPASS:
                    String endsign  = (String) msg.obj;
                    if (endsign.equals("true")){
                        ToastUtil.shortToast(getApplicationContext(),"签到结束");
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


                    //退出又进来  count = totalcount;

                    count = totalcount;
                    ++count;
                    if (count == personlist.length) {
                        reportSign.setText("结束签到");
                    }


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            signId = getIntent().getIntExtra("singnid", -1);
            totalcount = getIntent().getIntExtra("count", -1);
        }
        setContentView(R.layout.activity_morningsign);
        ButterKnife.bind(this);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        department = SpUtils.getString(getApplicationContext(), GlobalContanstant.DEPARATMENT);
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
        tvSignTeam.setText(department);
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


    private String getData(String tag) throws Exception {
        String method = null;

        switch (tag) {
            case "person":
                method = NetUrl.getPersonList;
                break;
            case "check":
                method = NetUrl.getSignCheck;

                break;
        }
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);

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
                    file = new File(getExternalCacheDir(), "personsign.jpg");
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        fileUri = FileProvider.getUriForFile(MoringSignActivity.this, Tag, file);
                    } else {
                        fileUri = Uri.fromFile(file);
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
        switch (requestCode) {
            case Take_Photo:
                if (resultCode == RESULT_OK) {

                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    if (Build.VERSION.SDK_INT >= 24) {
                        bitmap = ReportActivity.getBitmap(ivSignPicture, file.getAbsolutePath());
                    } else {

                        bitmap = ReportActivity.getBitmap(ivSignPicture, fileUri.getPath());
                    }
                    ivSignPicture.setImageBitmap(bitmap);
                    diseaseInformation.fileName = saveToSDCard(bitmap);
                    //将选择的图片设置到控件上
                    diseaseInformation.encode = ReportActivity.photo2Base64(path);


                }

                break;
        }

    }


    private final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";
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

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);//把图片数据写入文件
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
                autoCompleteData = null;
                autoCompleteAdapter = null;
                resultAdapter = null;
                resultData = null;
                dialog = new AlertDialog.Builder(MoringSignActivity.this).create();
                dialog.setCancelable(true);// 可以用“返回键”取消
                dialog.setCanceledOnTouchOutside(true);//
                dialog.show();
                dialog.setContentView(R.layout.sendroad_choicecheckperson);
                lv = (ListView) dialog.findViewById(R.id.lv_sendroad_list);
                SearchView searchView = (SearchView) dialog.findViewById(R.id.search_layout);
                searchView.setSearchViewListener(MoringSignActivity.this);
                searchView.setAutoCompleteAdapter(autoCompleteAdapter);
                initPersonData();
                if (lv != null) {
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //得到人名，显示
                            tvSignPerson.setText(resultData.get(position));
                            dialog.dismiss();

                        }

                    });
                }
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);



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
                if (count != personlist.length) {


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
                } else {
                    closeSign();
                }
                break;
        }
    }

    private void closeSign() {
        morningProgressbar.setVisibility(View.VISIBLE);
        reportSign.setVisibility(View.GONE);
        //提交服务器；
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = EndSign();
                    if (result != null) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.CHECKPASS;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private String EndSign() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.EndSignmethod);
        soapObject.addProperty("SignID", signId);
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

    private void initPersonData() {

        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        //getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
    }


    private void getDbData() {

        dbData.clear();
        for (int i = 0; i < personlist.length; i++) {
            dbData.add(personlist[i]);
        }

    }

    private void getAutoCompleteData(String text) {

        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i));
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(MoringSignActivity.this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private void getResultData(String text) {

        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).contains(text.trim())) {
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(MoringSignActivity.this, resultData);
        } else {
            resultAdapter.notifyDataSetChanged();
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
        soapObject.addProperty("SignID", signId);
        soapObject.addProperty("AdminPersonId", personID);
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

        httpTransportSE.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    @Override
    public void onRefreshAutoComplete(String text) {
        getAutoCompleteData(text);
    }

    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lv.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lv.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lv.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClear(SearchView.EditChangedListener editChangedListener) {
        editChangedListener = null;
    }


    private ArrayAdapter<String> autoCompleteAdapter;
    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;
    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<String> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 9;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    private List<String> dbData = new ArrayList<>();

}
