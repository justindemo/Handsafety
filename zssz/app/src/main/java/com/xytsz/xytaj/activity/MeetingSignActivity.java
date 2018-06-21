package com.xytsz.xytaj.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SearchAdapter;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.SearchView;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/24.
 * <p>
 * 会议签到
 */
public class MeetingSignActivity extends AppCompatActivity implements SearchView.SearchViewListener{

    @Bind(R.id.tv_sign_person)
    TextView tvSignPerson;
    @Bind(R.id.report_sign)
    Button reportSign;
    @Bind(R.id.morning_progressbar)
    LinearLayout morningProgressbar;
    private int meetingId;
    private HeaderProperty headerPropertyObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingsign);
        if(getIntent() != null){
            meetingId = getIntent().getIntExtra("trainId", -1);
        }
        ButterKnife.bind(this);

        initActionbar();
        //是否签到
        isSign();

    }


    private void isSign (){

        HeaderProperty headerPropertyObj =  new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.clear();

        headerList.add(headerPropertyObj);
            new Thread() {
                @Override
                public void run() {
                    try {
                        String result = getIsSign(meetingId);
                        Message message = Message.obtain();
                        message.obj = result;
                        message.what = GlobalContanstant.CHECKROADPASS;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        Message message = Message.obtain();
                        message.what = 300;
                        handler.sendMessage(message);
                    }
                }
            }.start();

        }


        private String getIsSign(int meetID) throws  Exception {
            SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.isMeetingSign);
            soapObject.addProperty("personId", personId);
            soapObject.addProperty("meetId", meetID);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);

            HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
            httpTransportSE.call(null, envelope,headerList);

            SoapObject object = (SoapObject) envelope.bodyIn;
            return object.getProperty(0).toString();

        }



    private void initData() {
        autoCompleteData = null;
        autoCompleteAdapter = null;
        resultAdapter = null;
        resultData = null;
        dialog = new AlertDialog.Builder(MeetingSignActivity.this).create();
        dialog.setCancelable(true);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(true);//
        dialog.show();
        dialog.setContentView(R.layout.sendroad_choicecheckperson);
        lv = (ListView) dialog.findViewById(R.id.lv_sendroad_list);
        SearchView searchView = (SearchView) dialog.findViewById(R.id.search_layout);
        searchView.setSearchViewListener(MeetingSignActivity.this);
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

    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("会议签到");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
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
            autoCompleteAdapter = new ArrayAdapter<>(MeetingSignActivity.this,
                    android.R.layout.simple_list_item_1, autoCompleteData);
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
            resultAdapter = new SearchAdapter(MeetingSignActivity.this, resultData);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }

    private ListView lv;
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

    private Dialog dialog;
    private int personId;

    @OnClick({R.id.tv_sign_person, R.id.report_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_person:
                //弹窗
                getData();
                break;
            case R.id.report_sign:
                //签到
                String personName = tvSignPerson.getText().toString();
                if (personName.isEmpty()){
                    return;
                }else {
                    for (Person person : persons) {
                        if (person.getName().equals(personName)) {
                            personId = person.getId();
                        }
                    }
                    upData();
                }

                break;
        }
    }

    private void upData() {
        morningProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    String result = upSerive(personId);
                    if (result != null){
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
    private List<HeaderProperty> headerList = new ArrayList<>();
    private String upSerive(int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.uploadMeeting);
        soapObject.addProperty("meetId",meetingId);
        soapObject.addProperty("personId",personId);
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

    private List<Person> persons;
    private String[] personlist;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.MYSENDSUCCESS:
                    morningProgressbar.setVisibility(View.GONE);
                    String personjson = (String) msg.obj;
                    persons = JsonUtil.jsonToBean(personjson, new TypeToken<List<Person>>() {
                    }.getType());
                    personlist = new String[persons.size()];
                    for (int i = 0; i < personlist.length; i++) {
                        personlist[i] = persons.get(i).getName();
                    }
                    initData();
                    break;
                case GlobalContanstant.FAIL:
                    morningProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"未获取人员");
                    break;

                case GlobalContanstant.CHECKFAIL:
                    morningProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"上传失败");
                    break;
                case GlobalContanstant.CHECKPASS:
                    morningProgressbar.setVisibility(View.GONE);
                    String result = (String) msg.obj;
                    ToastUtil.shortToast(getApplicationContext(),result);
                    if (result.equals("签到成功")) {
                        finish();
                    }
                    break;
                case GlobalContanstant.CHECKROADPASS:
                    morningProgressbar.setVisibility(View.GONE);
                    String issign = (String) msg.obj;
                    if (TextUtils.equals(issign,"true")){
                        ToastUtil.shortToast(getApplicationContext(),"您已签到过");
                        //新加
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        },1500);
                    }

                    break;

            }
        }
    };


    private void getData() {
        morningProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    String personList = getPersonList();
                    if (personList != null){
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.obj = personList;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getALLPersonList);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }
}
