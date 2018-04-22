package com.xytsz.xytaj.fragment;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.ContingencyPlanActivity;
import com.xytsz.xytaj.activity.MoringSignActivity;
import com.xytsz.xytaj.activity.MoringSignListActivity;
import com.xytsz.xytaj.activity.PatrolListActivity;
import com.xytsz.xytaj.activity.SystemManageActivity;
import com.xytsz.xytaj.activity.TrainTestActivity;
import com.xytsz.xytaj.adapter.PatrolListAdapter;
import com.xytsz.xytaj.adapter.SuperviseAdapter;
import com.xytsz.xytaj.adapter.SuperviseSecondAdapter;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.PatrolListBean;
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
 * Created by admin on 2017/6/29.
 *
 *  监管界面
 */
public class  SuperviseFragment extends BaseFragment  {

    private static final int MYTASK = 1;
    private static final int SYSTEMMANAGE = 2;
    private RecyclerView recycleView;
    private RecyclerView recycleViewSecond;
    private int role;
    private static final int SIGN = 0;



    private TextView mActionbartext;
    private int personId;
    private SuperviseAdapter adapter;


    @Override
    public View initView() {

        View view = View.inflate(getActivity(), R.layout.fragment_supervise, null);
        recycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recycleViewSecond = (RecyclerView) view.findViewById(R.id.recycle_second);
        mActionbartext = (TextView) view.findViewById(R.id.actionbar_text);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),3);

        recycleView.setLayoutManager(gridLayoutManager);
        recycleViewSecond.setLayoutManager(gridLayoutManager1);
        return view;
    }

    private List<String>  titles = new ArrayList<>();
    private List<String>  titles1 = new ArrayList<>();
    private List<PatrolListBean> patrolListBeens;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.PATROLLISTSUCCESS:
                    String jsonData = (String) msg.obj;
                    if (!jsonData.equals("[]")) {
                        patrolListBeens = JsonUtil.jsonToBean(jsonData, new TypeToken<List<PatrolListBean>>() {
                        }.getType());

                        if (patrolListBeens != null) {
                            if (patrolListBeens.size() != 0) {
                                //展示
                                number = patrolListBeens.size();
                                adapter.notifyDataSetChanged();

                            }

                        }
                    }
                    break;
                case GlobalContanstant.PATROLLISTFAIL:
                    ToastUtil.shortToast(getContext(), "数据未加载");
                    break;
            }
        }
    };

    private int number;

    @Override
    public void initData() {
        titles1.clear();
        titles1.add("培训考试");
        titles1.add("应急预案");
        titles.clear();
        titles.add("早会签到");
        titles.add("我的任务");
        titles.add("制度管理");
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        role = SpUtils.getInt(getContext(),GlobalContanstant.ROLE);
        getData();


        mActionbartext.setText(R.string.supervise);
        adapter = new SuperviseAdapter(titles,role,number);
        recycleView.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case SIGN:
                        if (role !=1 ){
                            IntentUtil.startActivity(SuperviseFragment.this.getActivity(),MoringSignListActivity.class);

                        }else {
                            ToastUtil.shortToast(getContext(),"您没有权限");
                        }

                        break;

                    case MYTASK:
                        //我的任务
                        IntentUtil.startActivity(getContext(),PatrolListActivity.class);
                        break;
                    case SYSTEMMANAGE:
                        IntentUtil.startActivity(getContext(),SystemManageActivity.class);
                        break;
                }
            }
        });


        SuperviseSecondAdapter secondAdapter = new SuperviseSecondAdapter(titles1,role);
        recycleViewSecond.setAdapter(secondAdapter);
        secondAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case SIGN:
                        //培训考试
                        IntentUtil.startActivity(getContext(), TrainTestActivity.class);

                        break;
                    case MYTASK:
                        IntentUtil.startActivity(getContext(), ContingencyPlanActivity.class);
                        break;


                }
            }
        });

    }

    /**
     * 获取显示的数据
     */
    private void getData() {

        new Thread() {
            @Override
            public void run() {
                try {
                    String jsonData = downData(personId);
                    if (jsonData != null) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.PATROLLISTSUCCESS;
                        message.obj = jsonData;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.PATROLLISTFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String downData(int personId) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getTaskByPersonID);
        soapObject.addProperty("personId", personId);

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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}

