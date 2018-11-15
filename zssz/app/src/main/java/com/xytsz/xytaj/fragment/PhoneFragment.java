package com.xytsz.xytaj.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.HomeActivity;
import com.xytsz.xytaj.bean.VisitorInfo;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by admin on 2017/9/5.
 * <p/>
 * 手机快捷登录
 */
public class PhoneFragment extends android.support.v4.app.Fragment {

    private static final int FAIL = 404;
    private EditText login_phone;
    private EditText passWord;
    private Button login;
    private TextView tvGet;
    private String visitor;
    private String success;
    private String nodata;
    private String resend;
    private String reget;
    private String error;
    private String code;
    private String checknet;
    private String logining;
    private String nopwd;
    private String neterror;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_phone, null);
        login_phone = (EditText) view.findViewById(R.id.login_phone);
        passWord = (EditText) view.findViewById(R.id.login_passWord);
        tvGet = (TextView) view.findViewById(R.id.tv_get);
        login = (Button) view.findViewById(R.id.login_btn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private String phone;

    private EventHandler eventHandler;
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECEIVE_SMS:
                    // 创建EventHandler对象

                    break;
            }
        }
    };

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    private void initData() {
        checknet = getString(R.string.login_checknet);
        logining = getString(R.string.logining);
        nopwd = getString(R.string.phone_nopwd);
        PermissionUtils.requestPermission(getActivity(), PermissionUtils.CODE_RECEIVE_SMS, mPermissionGrant);
        visitor = getString(R.string.visitor);
        success = getString(R.string.visitor_login_success);
        code = getString(R.string.visitor_VerificationCode);
        error = getString(R.string.visitor_phone_error);
        reget = getString(R.string.visitor_reget);
        resend = getString(R.string.visitor_resend);
        nodata = getString(R.string.visitor_datanull);
        neterror = getString(R.string.visitor_neterror);


        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);


        tvGet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getActivity())) {
                    //判断字符是否是13个
                    phone = login_phone.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.shortToast(getContext(), nodata);
                        return;
                    }
                    if (phone.length() == 11) {
                        boolean mobileTrue = isMobileNO(phone);
                        if (mobileTrue) {

                            SMSSDK.getVerificationCode("86", phone);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (TIME == 0) {
                                        TIME = 60;
                                    }
                                    for (int i = 60; i > 0; i--) {
                                        handler.sendEmptyMessage(CODE_ING);
                                        if (i <= 0) {
                                            break;
                                        }
                                        try {
                                            Thread.sleep(1000);

                                        } catch (InterruptedException e) {

                                        }
                                    }
                                    handler.sendEmptyMessage(CODE_REPEAT);

                                }
                            }).start();


                        } else {
                            ToastUtil.shortToast(getContext(), error);
                        }
                        // 拿权限 获取验证码，字体变灰色，然后变成60秒倒计时

                    } else {
                        ToastUtil.shortToast(getContext(), error);
                    }

                } else {
                    ToastUtil.shortToast(getContext(), checknet);
                }


            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证是否成功
                phone = login_phone.getText().toString();
                String pwd = passWord.getText().toString();
                if (!TextUtils.isEmpty(pwd)) {
                    ToastUtil.shortToast(getActivity(), logining);
                    SMSSDK.submitVerificationCode("86", phone, pwd);
                } else {
                    ToastUtil.shortToast(getContext(), nopwd);
                }

            }
        });

    }


    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private int TIME = 60;//倒计时60s
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FAIL:
                    ToastUtil.shortToast(getContext(), neterror);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                            ToastUtil.shortToast(getActivity(), success);

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        String login = login();
                                        if (login != null) {
                                            VisitorInfo visitorInfo = JsonUtil.jsonToBean(login, VisitorInfo.class);

                                            String telphone = visitorInfo.getTel();
                                            String name = visitorInfo.getName();
                                            String birthday = visitorInfo.getBirthday();
                                            int role_id = visitorInfo.getRole_id();
                                            int integral = visitorInfo.getIntegral();

                                            SpUtils.exit(getActivity().getApplicationContext());
                                            SpUtils.saveString(getContext(), GlobalContanstant.LOGINID, telphone);
                                            SpUtils.saveBoolean(getContext(), GlobalContanstant.ISFIRSTENTER, false);
                                            SpUtils.saveString(getActivity().getApplicationContext(), GlobalContanstant.USERNAME, name);
                                            SpUtils.saveInt(getActivity().getApplicationContext(), GlobalContanstant.ROLE, role_id);

                                            SpUtils.saveInt(getActivity().getApplicationContext(), GlobalContanstant.INERGRAL, integral);


                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.shortToast(getContext(), success);
                                                    IntentUtil.startActivity(getContext(), HomeActivity.class);
                                                    getActivity().finish();
                                                }
                                            });


                                        }


                                    } catch (Exception e) {
                                        Message message = Message.obtain();
                                        message.what = FAIL;
                                        handler.sendMessage(message);
                                    }
                                }
                            }.start();

                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            ToastUtil.shortToast(getContext(), code);
                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if (result == SMSSDK.RESULT_ERROR) {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                ToastUtil.shortToast(getContext(), des);
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
                case CODE_ING://已发送,倒计时
                    tvGet.setText(resend + "(" + --TIME + "s)");
                    tvGet.setTextColor(Color.parseColor("#ea9c71"));
                    break;
                case CODE_REPEAT://重新发送
                    tvGet.setText(reget);
                    break;
            }
        }
    };

    private String login() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getSZPeoplemethodName);
        soapObject.addProperty("tel", phone);
        soapObject.addProperty("name", visitor);
        soapObject.addProperty("bir", "1989-01-10");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getSZPeople_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);
    }
}
