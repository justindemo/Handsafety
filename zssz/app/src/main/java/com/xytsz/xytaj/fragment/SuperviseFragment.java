package com.xytsz.xytaj.fragment;


import android.content.Intent;
import android.os.Bundle;
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
import com.xytsz.xytaj.activity.NoCheckActivity;
import com.xytsz.xytaj.activity.PatrolListActivity;
import com.xytsz.xytaj.activity.SupMeetingActivity;
import com.xytsz.xytaj.activity.SystemManageActivity;
import com.xytsz.xytaj.activity.TrainTestActivity;
import com.xytsz.xytaj.adapter.PatrolListAdapter;
import com.xytsz.xytaj.adapter.SuperviseAdapter;
import com.xytsz.xytaj.adapter.SuperviseSecondAdapter;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.PatrolListBean;
import com.xytsz.xytaj.bean.TrainContent;
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
 * <p/>
 * 监管界面
 */
public class SuperviseFragment extends BaseFragment {

    private static final int MYTASK = 1;
    private static final int SYSTEMMANAGE = 2;
    private static final int MEETING = 3;
    private RecyclerView recycleView;
    private RecyclerView recycleViewSecond;
    private int role;
    private static final int SIGN = 0;


    private TextView mActionbartext;
    private int personId;
    private SuperviseAdapter adapter;
    private SuperviseSecondAdapter secondAdapter;
    private List<TrainContent> trainContents;
    private int nocheckNumber;


    @Override
    public View initView() {

        View view = View.inflate(getActivity(), R.layout.fragment_supervise, null);
        recycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recycleViewSecond = (RecyclerView) view.findViewById(R.id.recycle_second);
        mActionbartext = (TextView) view.findViewById(R.id.actionbar_text);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 3);

        recycleView.setLayoutManager(gridLayoutManager);
        recycleViewSecond.setLayoutManager(gridLayoutManager1);
        return view;
    }

    private List<String> titles = new ArrayList<>();
    private List<String> titles1 = new ArrayList<>();
    private List<PatrolListBean> patrolListBeens;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.PATROLLISTSUCCESS:
                    String jsonData = (String) msg.obj;
                    if (!jsonData.equals("[]")) {
                        patrolListBeens = JsonUtil.jsonToBean(jsonData, new TypeToken<List<PatrolListBean>>() {
                        }.getType());

                        if (patrolListBeens != null) {
                            if (patrolListBeens.size() != 0) {
                                //展示
                                number = patrolListBeens.size();
                            }

                        }
                    } else {
                        number = 0;
                    }
                    adapter = new SuperviseAdapter(titles, role, number);
                    adapter.notifyDataSetChanged();
                    break;
                case GlobalContanstant.PATROLLISTFAIL:
                    ToastUtil.shortToast(getContext(), "数据未加载");
                    break;
                case GlobalContanstant.TRAINLISTSUCCESS:
                    Bundle data = msg.getData();
                    String trainList = data.getString("trainlist");
                    String nocheckData = data.getString("nocheckData");
                    if (!trainList.equals("[]")) {
                        trainContents = JsonUtil.jsonToBean(trainList, new TypeToken<List<TrainContent>>() {
                        }.getType());

                        if (trainContents != null) {
                            if (trainContents.size() != 0) {
                                //展示
                                listnumber = trainContents.size();


                            }

                        }
                    } else {
                        listnumber = 0;
                    }

                    if (!nocheckData.equals("[]")) {
                        List<PatrolListBean>  patrolListBeens1 = JsonUtil.jsonToBean(nocheckData, new TypeToken<List<PatrolListBean>>() {
                        }.getType());

                        if (patrolListBeens1 != null) {
                            if (patrolListBeens1.size() != 0) {
                                //展示
                                nocheckNumber = patrolListBeens1.size();

                            }

                        }
                    } else {
                        nocheckNumber = 0;
                    }
                    secondAdapter = new SuperviseSecondAdapter(titles1, role, listnumber, nocheckNumber);
                    secondAdapter.notifyDataSetChanged();
                    break;


            }
        }
    };

    private int number;
    private int listnumber;

    @Override
    public void initData() {
        titles1.clear();
        titles1.add("培训考试");
        titles1.add("应急预案");
        titles1.add("未排查任务");
        titles1.add("会议纪要");

        titles.clear();
        titles.add("早会签到");
        titles.add("我的任务");
        titles.add("制度管理");
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        role = SpUtils.getInt(getContext(), GlobalContanstant.ROLE);
        getData();

        mActionbartext.setText(R.string.supervise);


        if (adapter != null) {
            recycleView.setAdapter(adapter);
            adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (position) {
                        case SIGN:
                            IntentUtil.startActivity(SuperviseFragment.this.getActivity(), MoringSignListActivity.class);
                            break;

                        case MYTASK:
                            //我的任务
                            IntentUtil.startActivity(getContext(), PatrolListActivity.class);
                            break;
                        case SYSTEMMANAGE:
                            IntentUtil.startActivity(getContext(), SystemManageActivity.class);
                            break;
                    }
                }
            });
        }

        if (secondAdapter != null) {
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
                            //应急预案
                            IntentUtil.startActivity(getContext(), ContingencyPlanActivity.class);
                            break;
                        case SYSTEMMANAGE:
                            if (role != 1) {
                                IntentUtil.startActivity(getContext(), NoCheckActivity.class);
                            } else {
                                ToastUtil.shortToast(getContext(), "您没有权限");
                            }
                            break;
                        case MEETING:
                            IntentUtil.startActivity(getContext(), SupMeetingActivity.class);
                            break;


                    }
                }
            });

        }

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
                    String trainList = getTrainList();
                    String nocheckData = getUncheck();
                    if (jsonData != null) {
                        Message message1 = Message.obtain();
                        message1.what = GlobalContanstant.PATROLLISTSUCCESS;
                        message1.obj = jsonData;
                        handler.sendMessage(message1);
                    }
                    if (trainList != null && nocheckData != null) {
                        Message message2 = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("trainlist",trainList);
                        bundle.putString("nocheckData",nocheckData);
                        message2.what = GlobalContanstant.TRAINLISTSUCCESS;
                        message2.setData(bundle);
                        handler.sendMessage(message2);
                    }


                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.PATROLLISTFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getUncheck() throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getnoTaskByPersonID);
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

    private String getTrainList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.trainTestshowmethod);
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

