package com.xytsz.xytaj.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.ReplacePersonAdapter;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ForMyDis;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.global.Data;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/12/14.
 *
 *
 */
public class MySendDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SUCCESS = 300;
    @Bind(R.id.mysend_tv_detail_reporter)
    TextView mysendTvDetailReporter;
    @Bind(R.id.mysend_tv_detail_diseasename)
    TextView mysendTvDetailDiseasename;
    @Bind(R.id.mysend_tv_detail_grade)
    TextView mysendTvDetailGrade;
    @Bind(R.id.mysend_tv_detail_dealtype)
    TextView mysendTvDetailDealtype;
    @Bind(R.id.mysend_tv_detail_fatype)
    TextView mysendTvDetailFatype;
    @Bind(R.id.mysend_tv_detail_pbtype)
    TextView mysendTvDetailPbtype;
    @Bind(R.id.mysend_tv_detail_reporteplace)
    TextView mysendTvDetailReporteplace;
    @Bind(R.id.mysend_tv_detail_faname)
    TextView mysendTvDetailFaname;
    @Bind(R.id.mysend_tv_detail_reportetime)
    TextView mysendTvDetailReportetime;
    @Bind(R.id.mysend_tv_detail_sender)
    TextView mysendTvDetailSender;
    @Bind(R.id.mysend_tv_detail_replace_person)
    TextView mysendTvDetailReplacePerson;
    @Bind(R.id.mysend_tv_detail_requesttime)
    TextView mysendTvDetailRequesttime;
    @Bind(R.id.mysend_tv_send_detail_loca)
    TextView mysendTvSendDetailLoca;
    @Bind(R.id.mysend_tv_detail_problem_audio)
    TextView mysendTvDetailProblemAudio;
    @Bind(R.id.mysend_tv_detail_problem_loca)
    TextView mysendTvDetailProblemLoca;
    @Bind(R.id.mysend_tv_detail_diseasedes)
    TextView mysendTvDetailDiseasedes;
    @Bind(R.id.iv_detail_photo1)
    ImageView ivDetailPhoto1;
    @Bind(R.id.iv_detail_photo2)
    ImageView ivDetailPhoto2;
    @Bind(R.id.iv_detail_photo3)
    ImageView ivDetailPhoto3;
    @Bind(R.id.tv_mysend_state)
    TextView tvMysendState;
    @Bind(R.id.tv_state)
    TextView tvState;
    private ForMyDis detail;
    private List<ImageUrl> imageUrlReport;
    private AudioUrl audioUrl;
    private int id;
    private int requestid;
    private SoundUtil soundUtil;
    private List<String> personNamelist;
    private List<String> personIDlist;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GlobalContanstant.PERSONLISTSUCCESS:
                    personListBeen = (List<Person>) msg.obj;
                    break;
                case GlobalContanstant.PERSONLISTFAIL:
                    ToastUtil.shortToast(getApplicationContext(), "人员数据未获取");
                    return;

                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getApplicationContext(), "修改失败，请稍后");
                    break;
                case SUCCESS:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "修改成功");
                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), "修改失败，请稍后");
                    }
                    break;
            }
        }
    };
    private int personID;
    private List<Person> personListBeen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {

            detail = (ForMyDis) getIntent().getSerializableExtra("detail");

            imageUrlReport = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrlReport");

            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");
        }

        setContentView(R.layout.acitivty_mysenddetail);
        ButterKnife.bind(this);

        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initAcitionbar();
        initData();


    }

    private void initData() {

        getData();

        int phaseIndication = detail.getPhaseIndication();
        switch (phaseIndication) {

            case 1:
                tvMysendState.setText("未下派");
                break;
            case 2:
                tvMysendState.setText("未处置");
                break;
            case 3:
                tvMysendState.setText("处置完");
                break;
            case 5:
                tvMysendState.setText("已驳回");
                break;
        }


        String upload_person_id = detail.getUpload_Person_ID() + "";

        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
        personNamelist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST);
        personIDlist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST);

        for (int i = 0; i < personIDlist.size(); i++) {
            if (upload_person_id.equals(personIDlist.get(i))) {
                id = i;
            }
        }

        String requirementsCompletion_person_id = detail.getRequirementsComplete_Person_ID() + "";
        for (int i = 0; i < personIDlist.size(); i++) {
            if (requirementsCompletion_person_id.equals(personIDlist.get(i))) {
                requestid = i;
            }
        }

        String userName = personNamelist.get(id);
        mysendTvDetailReporter.setText(userName);
        int disposalLevel_id = detail.getDisposalLevel_ID() - 1;
        int level = detail.getLevel();
        mysendTvDetailDiseasename.setText(Data.pbname[level]);
        mysendTvDetailGrade.setText(Data.grades[disposalLevel_id]);

        mysendTvDetailFatype.setText(detail.getFacilityType_Name());
        mysendTvDetailDealtype.setText(detail.getDealType_Name());

        mysendTvDetailPbtype.setText(detail.getDiseaseType_Name());


        mysendTvDetailReporteplace.setText(detail.getStreetAddress_Name());


        mysendTvDetailFaname.setText(detail.getFacilityName_Name());

        String uploadTime = detail.getUploadTime();
        mysendTvDetailReportetime.setText(uploadTime);

        mysendTvDetailProblemLoca.setText(detail.getAddressDescription());
        mysendTvDetailDiseasedes.setText(detail.getDiseaseDescription());

        String acperson = personNamelist.get(requestid);
        mysendTvDetailSender.setText(acperson);
        mysendTvDetailRequesttime.setText(detail.getRequirementsCompleteTime());

        if (imageUrlReport.size() != 0) {
            if (imageUrlReport.size() == 1) {
                Glide.with(getApplicationContext()).load(imageUrlReport.get(0).getImgurl()).into(ivDetailPhoto1);
                ivDetailPhoto2.setVisibility(View.INVISIBLE);
                ivDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrlReport.size() == 2) {
                Glide.with(getApplicationContext()).load(imageUrlReport.get(0).getImgurl()).into(ivDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrlReport.get(1).getImgurl()).into(ivDetailPhoto2);
                ivDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrlReport.size() == 3) {
                Glide.with(getApplicationContext()).load(imageUrlReport.get(0).getImgurl()).into(ivDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrlReport.get(1).getImgurl()).into(ivDetailPhoto2);
                Glide.with(getApplicationContext()).load(imageUrlReport.get(2).getImgurl()).into(ivDetailPhoto3);
            }
        } else {

            ivDetailPhoto1.setVisibility(View.VISIBLE);
            ivDetailPhoto2.setVisibility(View.INVISIBLE);
            ivDetailPhoto3.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(ivDetailPhoto1);

        }


        ivDetailPhoto1.setOnClickListener(this);
        ivDetailPhoto2.setOnClickListener(this);
        ivDetailPhoto3.setOnClickListener(this);

        if (detail.getAddressDescription().isEmpty()) {
            if (audioUrl != null) {
                if (!audioUrl.getAudioUrl().equals("false")) {
                    if (!audioUrl.getTime().isEmpty()) {
                        mysendTvDetailProblemLoca.setVisibility(View.GONE);
                        mysendTvDetailProblemAudio.setVisibility(View.VISIBLE);
                        soundUtil = new SoundUtil();
                        mysendTvDetailProblemAudio.setText(audioUrl.getTime());
                        mysendTvDetailProblemAudio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                mysendTvDetailProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


                                soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                    @Override
                                    public void onFinish() {
                                        mysendTvDetailProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                                soundUtil.play(audioUrl.getAudioUrl());
                            }
                        });
                    }
                } else {
                    mysendTvDetailProblemLoca.setVisibility(View.VISIBLE);
                    mysendTvDetailProblemAudio.setVisibility(View.GONE);
                }
            } else {
                mysendTvDetailProblemLoca.setVisibility(View.VISIBLE);
                mysendTvDetailProblemAudio.setVisibility(View.GONE);
            }

        } else {
            mysendTvDetailProblemLoca.setVisibility(View.VISIBLE);
            mysendTvDetailProblemAudio.setVisibility(View.GONE);
        }


    }

    private void getData() {

        new Thread() {
            @Override
            public void run() {
                try {
                    String repairPersonList = getRepairPersonList();

                    List<Person> personListBeen = JsonUtil.jsonToBean(repairPersonList, new TypeToken<List<Person>>() {
                    }.getType());
                    Message message = Message.obtain();
                    message.obj = personListBeen;
                    message.what = GlobalContanstant.PERSONLISTSUCCESS;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.PERSONLISTFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private String getRepairPersonList() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getPersonList);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.getPersonList_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("我的下派");
        }
    }

    @OnClick({R.id.mysend_tv_detail_replace_person, R.id.mysend_tv_send_detail_loca, R.id.tv_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mysend_tv_detail_replace_person:
                final Dialog dialog = new AlertDialog.Builder(MySendDetailActivity.this).create();
                dialog.setCancelable(true);// 可以用“返回键”取消
                dialog.setCanceledOnTouchOutside(true);//
                dialog.show();

                dialog.setContentView(R.layout.mysend_replaceperson);
                ListView lv = (ListView) dialog.findViewById(R.id.lv_replaceperson);
                //获取人员的数据
                //TODO:  需要修改传入人员的list
                if (personListBeen != null && personListBeen.size() != 0) {
                    ReplacePersonAdapter replacePersonAdapter = new ReplacePersonAdapter(personListBeen);
                    lv.setAdapter(replacePersonAdapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            new AlertDialog.Builder(MySendDetailActivity.this).setTitle("修改维修人")
                                    .setMessage("确定替换维修人为：" + personNamelist.get(position) + "?")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //得到Id
                                    int personid = personListBeen.get(position).getId();
                                    updateMessage(personid, detail.getTaskNumber());
                                    //提交服务器。
                                    mysendTvDetailSender.setText(personListBeen.get(position).getName());
                                    dialog.dismiss();
                                }
                            }).create().show();
                           dialog.dismiss();
                        }
                    });
                }else {
                    ToastUtil.shortToast(getApplicationContext(), "人员数据未获取");
                }
                break;
            case R.id.mysend_tv_send_detail_loca:
                double longitude = detail.getLongitude();
                double latitude = detail.getLatitude();
                Intent intent = new Intent(MySendDetailActivity.this, MarkPositionActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);
                break;
            case R.id.tv_state:
                final Dialog alertDialog = new AlertDialog.Builder(MySendDetailActivity.this).create();
                alertDialog.setCancelable(true);// 可以用“返回键”取消
                alertDialog.setCanceledOnTouchOutside(true);//
                alertDialog.show();
                View inflate = LayoutInflater.from(MySendDetailActivity.this).inflate(R.layout.myreview_state_dialog, null);
                alertDialog.setContentView(inflate);

                TextView mtvReportTime = (TextView) inflate.findViewById(R.id.tv_report_time);
                TextView mtvReviewTime = (TextView) inflate.findViewById(R.id.tv_review_time);
                TextView mtvSendTime = (TextView) inflate.findViewById(R.id.tv_send_time);
                TextView mtvPostTime = (TextView) inflate.findViewById(R.id.tv_post_time);

                mtvReportTime.setText(detail.getUploadTime());
                mtvReviewTime.setText(detail.getReviewedTime());
                if (detail.getIssuedTime() == null) {
                    mtvSendTime.setText("未下派");
                } else {
                    mtvSendTime.setText(detail.getIssuedTime());
                }
                if (detail.getActualCompletionTime().equals("null") || detail.getActualCompletionTime().isEmpty()) {
                    mtvPostTime.setText("未处置");
                } else {
                    mtvPostTime.setText(detail.getActualCompletionTime());
                }
                break;
        }
    }

    private void updateMessage(final int personid, final String tasknumber) {
        new Thread() {
            @Override
            public void run() {

                try {
                    String upload = upload(personid, tasknumber);
                    Message message = Message.obtain();
                    message.what = SUCCESS;
                    message.obj = upload;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String upload(int personid, String tasknumber) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.replacePerson);
        soapObject.addProperty("personid", personid);
        soapObject.addProperty("TaskNumber", tasknumber);

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
    public void onClick(View v) {
        Intent intent = new Intent(MySendDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrlReport);
        startActivity(intent);
    }


}
