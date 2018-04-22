package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.TraintestShowAdapter;
import com.xytsz.xytaj.bean.TrainContent;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/12.
 * 展示内容
 */
public class TrainTestShowActivity extends AppCompatActivity {

    @Bind(R.id.traintestshow_rv)
    RecyclerView traintestshowRv;
    @Bind(R.id.traintestshow_progressbar)
    LinearLayout traintestshowProgressbar;
    private int tag;
    private String title;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.FAIL:
                    traintestshowProgressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(),"获取数据异常");
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    traintestshowProgressbar.setVisibility(View.GONE);
                    String json = (String) msg.obj;
                    if (json != null && !json.equals("[]")){
                        final List<TrainContent> trainContents = JsonUtil.jsonToBean(json, new TypeToken<List<TrainContent>>() {
                        }.getType());

                        if (trainContents.size()!= 0){
                            TraintestShowAdapter traintestShowAdapter = new TraintestShowAdapter(trainContents);
                            traintestshowRv.setAdapter(traintestShowAdapter);
                            traintestShowAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (tag){
                                        case 1:
                                            //传递哪一场培训，
                                            intent2show(position, trainContents,MoringSignActivity.class,true);
                                            break;
                                        case 2:
                                            intent2show(position,trainContents,TrainPhotoActivity.class,false);
                                            break;
                                        case 3:
                                            intent2show(position,trainContents,TestActivity.class,false);
                                            break;
                                        case 4:
                                            intent2show(position,trainContents,TestCollectActivity.class,false);
                                            break;
                                    }
                                }


                            });
                        }else {
                            ToastUtil.shortToast(getApplicationContext(),"当前没有培训");
                        }
                    }
                    break;
            }
        }
    };

    private void intent2show(int position, List<TrainContent> trainContents, Class<?> activity,boolean t) {
        Intent intent = new Intent(TrainTestShowActivity.this,activity);
        if (t){
            intent.putExtra("tag","trainsign");
        }
        //intent.putExtra("traintag",trainContents.get(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traintestshow);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);
        }

        switch (tag) {
            case 0:
                title = "实施方案";
                break;
            case 1:
                title = "总结评估";
                break;
            case 2:
                title = "内容资料";
                break;
            case 3:
                title = "培训记录";
                break;
            case 4:
                title = "培训通知";
                break;
            case 5:
                title = "培训签到";
                break;
            case 6:
                title = "培训照片";
                break;
            case 7:
                title = "培训考试";
                break;
            case 8:
                title = "成绩汇总";
                break;
        }

        initactionbar(title);
        initData();
    }

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        traintestshowRv.setLayoutManager(manager);
        traintestshowProgressbar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    String data = getData(tag);
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MYSENDSUCCESS;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;

                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private String getData(int tag)throws Exception{
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.trainTestshowmethod);
//        soapObject.addProperty("login_ID", loginID);
//        soapObject.addProperty("pwd", pWD);

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

    private void initactionbar(String title) {
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
}


