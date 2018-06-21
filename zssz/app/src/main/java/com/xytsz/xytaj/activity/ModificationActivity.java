package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
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
 * Created by admin on 2017/12/15.
 * <p>
 * 修改密码
 */
public class ModificationActivity extends AppCompatActivity {

    @Bind(R.id.modification_psd)
    EditText modificationPsd;
    @Bind(R.id.modification_iv)
    ImageView modificationIv;
    @Bind(R.id.mine_compelet)
    Button mineCompelet;
    @Bind(R.id.modification_progressbar)
    LinearLayout modificationProgressbar;
    private boolean visiable;
    private String newPsd;
    private int personID;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.CHECKFAIL:
                    modificationProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), "修改出错");
                    break;
                case GlobalContanstant.CHECKPASS:
                    String result = (String) msg.obj;
                    if (result.equals("true")) {
                        modificationProgressbar.setVisibility(View.GONE);
                        ToastUtil.shortToast(getApplicationContext(), "修改成功");
                        SpUtils.saveString(getApplicationContext(),GlobalContanstant.PASSWORD,newPsd);
                        finish();
                    }else {
                        modificationProgressbar.setVisibility(View.GONE);
                        ToastUtil.shortToast(getApplicationContext(), "修改失败");
                    }
                    break;
            }
        }
    };
    private String loginID;
    private String oldPWd;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        ButterKnife.bind(this);

        initActionbar();
        initData();

    }
    private void initData() {
        modificationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visiable) {
                    modificationIv.setImageResource(R.mipmap.modification_invisiable);
                    modificationPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    visiable = false;
                } else {
                    modificationIv.setImageResource(R.mipmap.modification_visiable);
                    modificationPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    visiable = true;
                }
            }
        });

    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            String information = getString(R.string.information_modification);
            actionBar.setTitle(information);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick(R.id.mine_compelet)
    public void onViewClicked() {

        //提交服务器，并保存。
        newPsd = modificationPsd.getText().toString();
        oldPWd = SpUtils.getString(getApplicationContext(), GlobalContanstant.PASSWORD);
        loginID = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);
        modificationProgressbar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = updatePsd(loginID,oldPWd ,newPsd);
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKPASS;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();


    }

    private List<HeaderProperty> headerList = new ArrayList<>();
    private String updatePsd(String loginID, String oldPWd,String newPsd) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.modificaitonmethodName);
        soapObject.addProperty("loginID", loginID);
        soapObject.addProperty("oldPwd",oldPWd);
        soapObject.addProperty("newPwd", newPsd);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        //添加cookie
//
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);

        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;
    }

}
