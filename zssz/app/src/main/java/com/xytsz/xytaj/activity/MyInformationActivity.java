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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.global.Data;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/13.
 * 我的资料界面
 */
public class MyInformationActivity extends AppCompatActivity {

    private static final int SUCCESS = 500;
    @Bind(R.id.my_name)
    EditText myName;
    @Bind(R.id.my_telephone)
    EditText myTelephone;
    @Bind(R.id.my_department)
    EditText myDepartment;
    @Bind(R.id.my_ll_activite)
    RelativeLayout myLlActivite;
    @Bind(R.id.mine_compelet)
    Button mineCompelet;
    @Bind(R.id.mine_modification_psd)
    Button mineModificationPsd;

    private String phone;
    private String username;
    private int role;
    private String name;
    private String success;
    private String originalName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinformation);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            String information = getString(R.string.mine_information);

            actionBar.setTitle(information);

        }


        String compelet = getString(R.string.information_compelet);
        success = getString(R.string.save_success);
        originalName = getString(R.string.visitor);
        phone = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);

        username = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);

        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);
        String department = SpUtils.getString(getApplicationContext(), GlobalContanstant.DEPARATMENT);


        String phones = SpUtils.getString(getApplicationContext(), GlobalContanstant.PHONE);


        String visitor = getString(R.string.visitor_department);
        //逻辑：现在是根据获取显示
        //未来：用用户名和密码登录 2：用手机登录
        //用账号密码登录的  这边显示部门， 否则则不显示
        //具体还要显示什么再确定

        if (role == 0) {
            mineCompelet.setVisibility(View.VISIBLE);

            if (username.equals(originalName)) {
                ToastUtil.shortToast(getApplicationContext(), compelet);
            } else {
                myName.setText(username);
                myName.setCursorVisible(false);
                myName.setFocusable(false);
                myName.setFocusableInTouchMode(false);
                mineCompelet.setVisibility(View.GONE);
            }

            myTelephone.setText(phone);
            myDepartment.setText(visitor);
            mineCompelet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = myName.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtil.shortToast(getApplicationContext(), "姓名不能为空");
                        return;
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                String json = getvisitor();
                                if (json != null) {
                                    Message message = Message.obtain();
                                    message.obj = json;
                                    message.what = SUCCESS;
                                    handler.sendMessage(message);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }.start();
                }
            });

        } else {
            myName.setText(username);
            myName.setCursorVisible(false);
            myName.setFocusable(false);
            myName.setFocusableInTouchMode(false);
            myTelephone.setText(phones);
            myDepartment.setText(department);
        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    String json = (String) msg.obj;
                    if (json.equals("true")) {
                        SpUtils.saveString(getApplicationContext(), GlobalContanstant.USERNAME, name);
                        ToastUtil.shortToast(getApplicationContext(), success);
                        Intent intent = getIntent();
                        setResult(3, intent);
                        MyInformationActivity.this.finish();
                    }
                    break;
            }
        }
    };


    private String getvisitor() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.updatePesronmethodName);
        soapObject.addProperty("tel", phone);
        soapObject.addProperty("name", name);
        soapObject.addProperty("bir", "1989-08-08");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.updatePesron_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }

    @OnClick(R.id.mine_modification_psd)
    public void onViewClicked() {
        Intent intent = new Intent(MyInformationActivity.this,ModificationActivity.class);
        startActivity(intent);
    }
}
