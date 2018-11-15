package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/3/2.
 * 地图上的显示详细信息
 */
public class DiseaseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_detail_facility)
    TextView tvDetailFacility;
    @Bind(R.id.tv_detail_facility_person)
    TextView tvDetailFacilityPerson;
    @Bind(R.id.tv_detail_facility_problem)
    TextView tvDetailFacilityProblem;
    @Bind(R.id.tv_detail_reportetime)
    TextView tvDetailReportetime;
    @Bind(R.id.tv_detail_requesttime)
    TextView tvDetailRequesttime;
    @Bind(R.id.tv_detail_facility_loca)
    TextView tvDetailFacilityLoca;
    @Bind(R.id.tv_detail_facility_team)
    TextView tvDetailFacilityTeam;
    @Bind(R.id.tv_detail_requestname)
    TextView tvDetailRequestname;
    @Bind(R.id.tv_detail_plan)
    TextView tvDetailPlan;
    @Bind(R.id.tv_deal_pass)
    TextView tvDealPass;
    @Bind(R.id.tv_deal_back)
    TextView tvDealBack;
    @Bind(R.id.tv_detail_planer)
    TextView tvDetailPlaner;
    @Bind(R.id.ll_road_idea)
    LinearLayout llRoadIdea;
    @Bind(R.id.tv_detail_sendadvice)
    TextView tvDetailSendadvice;
    @Bind(R.id.ll_post_facility)
    LinearLayout llPostFacility;
    @Bind(R.id.ll_post_department)
    LinearLayout llPostDepartment;
    @Bind(R.id.tv_detail_facility_way)
    TextView tvDetailFacilityWay;
    private ImageView mivPhoto1;
    private ImageView mivPhoto2;
    private ImageView mivPhoto3;
    private Review detail;
    private List<ImageUrl> imageUrls;
    private AudioUrl audioUrl;
    private TextView mtvProblemLoca;
    private TextView mtvProblemAudio;
    private SoundUtil soundUtil;
    private LinearLayout mllVideo;
    private ImageView mivPlay;
    private static final int SUCCESS = 501;
    private String videopath;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    videopath = (String) msg.obj;
                    if (videopath.isEmpty() || videopath == null || videopath.equals("false")) {
                        mllVideo.setVisibility(View.GONE);
                    } else {
                        mllVideo.setVisibility(View.VISIBLE);
                    }
                    break;

                case GlobalContanstant.REVIEWPASS:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "审核成功");
                            Intent intent = getIntent();
                            intent.putExtra("position", position);
                            setResult(301, intent);
                            finish();
                        }
                    }
                    break;
                case GlobalContanstant.REVIEWFAIL:
                    String cancle = (String) msg.obj;
                    if (cancle != null) {
                        if (cancle.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "未通过");
                            Intent intent = getIntent();
                            intent.putExtra("position", position);
                            setResult(302, intent);
                            finish();
                        }
                    }
                    break;
            }
        }
    };
    private int position;
    private int personId;
    private int tag;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            detail = (Review) getIntent().getSerializableExtra("detail");

            imageUrls = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrls");

            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");
            position = getIntent().getIntExtra("position", -1);


        }
        setContentView(R.layout.activity_diseasedetail);
        ButterKnife.bind(this);
        initAcitionbar();
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initView();
        initData();
    }

    private void initView() {

        mivPhoto1 = (ImageView) findViewById(R.id.iv_detail_photo1);
        mivPhoto2 = (ImageView) findViewById(R.id.iv_detail_photo2);
        mivPhoto3 = (ImageView) findViewById(R.id.iv_detail_photo3);

        mtvProblemLoca = (TextView) findViewById(R.id.tv_detail_problem_loca);
        mtvProblemAudio = (TextView) findViewById(R.id.tv_detail_problem_audio);


    }


    private void initData() {
        //赋值

        if (detail != null) {

            if (detail.getCheckType() == 2) {
                llPostDepartment.setVisibility(View.GONE);
                llPostFacility.setVisibility(View.VISIBLE);
                tvDetailFacilityWay.setText(getResources().getString(R.string.randomreprote));
            } else {
                llPostDepartment.setVisibility(View.VISIBLE);
                llPostFacility.setVisibility(View.VISIBLE);
                tvDetailFacilityWay.setText(getResources().getString(R.string.standardreprote));
            }

            tvDetailFacility.setText(detail.getDeviceName());
            tvDetailFacilityPerson.setText(detail.getCheckPersonName());
            tvDetailFacilityTeam.setText(detail.getDeptName());

            tvDetailSendadvice.setText(detail.getZZCSSHInfo());
            tvDetailFacilityLoca.setText(detail.getAddressInfo());
            StringBuilder stringBuilder = new StringBuilder();
            List<String> errorInfo = detail.getErrorInfo();
            for (String s : errorInfo) {
                stringBuilder.append(s).append(";");
            }
            String problem = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
            tvDetailFacilityProblem.setText(problem);

            tvDetailReportetime.setText(detail.getCheckTime());
            tvDetailRequestname.setText(detail.getRequirementsComplete_Person_Name());
            tvDetailRequesttime.setText(detail.getRequirementsCompleteTime());
            tvDetailPlan.setText(detail.getZZCSInfo());
            tvDetailPlaner.setText(detail.getZZCSPersonName());

            mtvProblemLoca.setText(detail.getRemarks());


            if (imageUrls.size() != 0) {
                if (imageUrls.size() == 1) {
                    Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(mivPhoto1);
                    mivPhoto2.setVisibility(View.INVISIBLE);
                    mivPhoto3.setVisibility(View.INVISIBLE);
                } else if (imageUrls.size() == 2) {
                    Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(mivPhoto1);
                    Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(mivPhoto2);
                    mivPhoto3.setVisibility(View.INVISIBLE);
                } else if (imageUrls.size() == 3) {
                    Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(mivPhoto1);
                    Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(mivPhoto2);
                    Glide.with(getApplicationContext()).load(imageUrls.get(2).getImgurl()).into(mivPhoto3);
                }
            } else {

                mivPhoto1.setVisibility(View.VISIBLE);
                mivPhoto2.setVisibility(View.INVISIBLE);
                mivPhoto3.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(mivPhoto1);

            }
            mivPhoto1.setOnClickListener(this);
            mivPhoto2.setOnClickListener(this);
            mivPhoto3.setOnClickListener(this);


            if (detail.getRemarks().isEmpty()) {
                if (audioUrl != null) {
                    if (audioUrl.getAudioUrl() != null) {
                        if (!audioUrl.getAudioUrl().equals("false")) {
                            if (!audioUrl.getTime().isEmpty()) {
                                mtvProblemLoca.setVisibility(View.GONE);
                                mtvProblemAudio.setVisibility(View.VISIBLE);
                                soundUtil = new SoundUtil();
                                mtvProblemAudio.setText(audioUrl.getTime());

                                mtvProblemAudio.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                        final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                        mtvProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                                        //soundUtil.play(audioUrl);

                                        soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                            @Override
                                            public void onFinish() {
                                                mtvProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
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
                            mtvProblemLoca.setVisibility(View.VISIBLE);

                            mtvProblemAudio.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mtvProblemLoca.setVisibility(View.VISIBLE);

                    mtvProblemAudio.setVisibility(View.GONE);
                }
            } else {
                mtvProblemLoca.setVisibility(View.VISIBLE);
                mtvProblemAudio.setVisibility(View.GONE);
            }
        } else {
            ToastUtil.shortToast(getApplicationContext(), "数据未获取");
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DiseaseDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrls);
        startActivity(intent);
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.problem_detail);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @OnClick({R.id.tv_deal_pass, R.id.tv_deal_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_deal_pass:
                UpData();

                break;
            case R.id.tv_deal_back:
                UpData();
                break;
        }
    }

    private void UpData() {
        new Thread() {
            @Override
            public void run() {

                try {
                    String data = toNet();
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.REVIEWPASS;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.REVIEWFAIL;
                    handler.sendMessage(message);
                }

            }
        }.start();
    }

    private List<HeaderProperty> headerList = new ArrayList<>();

    private String toNet() throws Exception {
        //发通知节点//哪个单子，状态，通知人是谁，意见是什么,时间是否需要。
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);

        soapObject.addProperty("DeciceCheckNum", detail.getDeciceCheckNum());
        soapObject.addProperty("state", GlobalContanstant.GETPOST);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("opinion", "");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        //添加cookie
//        private List<HeaderProperty> headerList = new ArrayList<>();
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie, SpUtils.getString(getApplicationContext(), GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);
        httpTransportSE.call(null, envelope/*,headerList*/);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String data = object.getProperty(0).toString();

        return data;
    }
}
