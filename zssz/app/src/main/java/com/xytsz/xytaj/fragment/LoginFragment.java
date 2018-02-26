package com.xytsz.xytaj.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.HomeActivity;
import com.xytsz.xytaj.bean.PersonInfo;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/5.
 *  登录界面
 *
 */
public class LoginFragment extends android.support.v4.app.Fragment {


    private static final int PERSONDATA = 11123;
    private static final int FAILLOGIN = 11133;
    private static final int ISMEMBER = 11143;

    private List<String> personNameList = new ArrayList<>();
    private List<String> personIDList = new ArrayList<>();
    private EditText login_id;
    private EditText passWord;
    private Button login;
    private String loginID;
    private String pWD;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PERSONDATA:
                    ToastUtil.shortToast(getActivity(), checknet);
                    break;
                case FAILLOGIN:
                    ToastUtil.shortToast(getActivity(), checknet);
                    break;
                case ISMEMBER:
                    List<Person> list = (List<Person>) msg.obj;
                    if (list.size() != 0) {
                        for (Person detail : list) {
                            personNameList.add(detail.getName());
                            personIDList.add(detail.getId() + "");
                        }
                        SpUtils.putStrListValue(getActivity(), GlobalContanstant.PERSONNAMELIST, personNameList);
                        SpUtils.putStrListValue(getActivity(), GlobalContanstant.PERSONIDLIST, personIDList);
                    }else {
                        ToastUtil.shortToast(getActivity(),neterror);
                    }
                    break;
            }
        }
    };
    private String logining;
    private String nodata;
    private String error;
    private String checknet;
    private String neterror;
    private String success;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =   View.inflate(getActivity(), R.layout.fragment_login,null);
        login_id = (EditText)view.findViewById(R.id.login_id);
        passWord = (EditText) view.findViewById(R.id.passWord);
        login = (Button) view.findViewById(R.id.login);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        logining = getString(R.string.logining);
        nodata = getString(R.string.login_datanull);
        error = getString(R.string.login_error);
        checknet = getString(R.string.login_checknet);
        neterror = getString(R.string.login_neterror);

        success = getString(R.string.visitor_login_success);
        //点击登陆按钮   从服务器获取到数据 username和pwd
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //检查网络：如果联网，进行操作 不联网不进行操作
                if (isNetworkAvailable(getContext())) {
                    ToastUtil.shortToast(getContext(), logining);
                    loginID = login_id.getText().toString();
                    pWD = passWord.getText().toString();

                    if (TextUtils.isEmpty(loginID) || TextUtils.isEmpty(pWD)) {
                        ToastUtil.shortToast(getContext(), nodata);

                    }

                    //上传服务器
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                final String json = tologin(loginID, pWD);
                                final String personJson = MyApplication.getAllPersonList();
                                if (json != null) {
                                    if (!json.equals("[]")) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (json.equals("false")) {
                                                    ToastUtil.shortToast(getContext(), error);
                                                } else {
                                                    final PersonInfo personInfo = JsonUtil.jsonToBean(json, PersonInfo.class);
                                                    //保存到本地  ID  名字
                                                    int personID = personInfo.getID();
                                                    String userName = personInfo.getUserName();
                                                    String phone = personInfo.getTelephone();
                                                    int department_id = personInfo.getDepartment_ID();

//                                                    int role = personInfo.get;
                                                    int role = 1;
                                                    //sp 保存
                                                    SpUtils.exit(getActivity().getApplicationContext());


                                                    SpUtils.saveString(getActivity().getApplicationContext(), GlobalContanstant.LOGINID, loginID);
                                                    SpUtils.saveString(getActivity().getApplicationContext(), GlobalContanstant.PASSWORD, pWD);
                                                    //保存不是第一次进入
                                                    SpUtils.saveBoolean(getActivity().getApplicationContext(), GlobalContanstant.ISFIRSTENTER, false);
                                                    SpUtils.saveInt(getActivity().getApplicationContext(), GlobalContanstant.PERSONID, personID);
                                                    SpUtils.saveString(getActivity().getApplicationContext(), GlobalContanstant.USERNAME, userName);
                                                    SpUtils.saveString(getActivity().getApplicationContext(), GlobalContanstant.PHONE, phone);
                                                    SpUtils.saveInt(getActivity().getApplicationContext(), GlobalContanstant.DEPARATMENT, department_id);
                                                    SpUtils.saveInt(getActivity().getApplicationContext(), GlobalContanstant.ROLE, role);

                                                    if (personJson != null) {

                                                        List<Person> memberList = JsonUtil.jsonToBean(personJson, new TypeToken<List<Person>>() {
                                                        }.getType());
                                                        Message message = Message.obtain();
                                                        message.obj = memberList;
                                                        message.what = ISMEMBER;
                                                        handler.sendMessage(message);
                                                    }



                                                    ToastUtil.shortToast(getContext(), success);

                                                    IntentUtil.startActivity(getActivity(),HomeActivity.class);
                                                    getActivity().finish();


                                                }
                                            }
                                        });
                                    } else {
                                        Message message = Message.obtain();
                                        message.what = PERSONDATA;
                                        handler.sendMessage(message);
                                    }
                                }
                            } catch (Exception e) {


                            }

                        }
                    }.start();

                } else {
                    Message message = Message.obtain();
                    message.what = FAILLOGIN;
                    handler.sendMessage(message);
                }
            }
        });

    }


    /**
     * @param context： 上下文
     * @return 网络是否可用
     */

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

    private String tologin(String loginID, String pWD) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.loginmethodName);
        soapObject.addProperty("login_ID", loginID);
        soapObject.addProperty("pwd", pWD);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }
}
