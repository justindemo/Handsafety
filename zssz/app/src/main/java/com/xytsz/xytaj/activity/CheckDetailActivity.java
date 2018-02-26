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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/3/7.
 * 验收详细单
 */
public class CheckDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ISCHECKPASS = 600001;
    private static final int ISUNCHECKPASS = 600002;
    @Bind(R.id.tv_check_detail_diseasedes)
    TextView tvCheckDetailDiseasedes;
    @Bind(R.id.tv_check_reporter)
    TextView tvCheckReporter;
    @Bind(R.id.tv_check_facility)
    TextView tvCheckFacility;
    @Bind(R.id.tv_check_facility_person)
    TextView tvCheckFacilityPerson;
    @Bind(R.id.tv_check_facility_problem)
    TextView tvCheckFacilityProblem;
    @Bind(R.id.tv_check_reviewer)
    TextView tvCheckReviewer;
    @Bind(R.id.tv_check_dealer)
    TextView tvCheckDealer;
    @Bind(R.id.tv_check_reportetime)
    TextView tvCheckReportetime;
    @Bind(R.id.tv_check_reviewtime)
    TextView tvCheckReviewtime;
    @Bind(R.id.tv_check_requesttime)
    TextView tvCheckRequesttime;
    @Bind(R.id.tv_check_resulttime)
    TextView tvCheckResulttime;
    @Bind(R.id.tv_check_facility_loca)
    TextView tvCheckFacilityLoca;
    @Bind(R.id.tv_check_facility_team)
    TextView tvCheckFacilityTeam;
    @Bind(R.id.tv_check_problem_audio)
    TextView tvCheckProblemAudio;
    @Bind(R.id.tv_check_problem_loca)
    TextView tvCheckProblemLoca;
    @Bind(R.id.tv_check_decs)
    TextView tvCheckDecs;
    private TextView mtvRoad;
    private ImageView mivReporte;
    private ImageView mivDealed;
    private TextView mtvBack;
    private TextView mtvPass;
    private int position;
    private Review detail;

    private int personID;
    private static final int SUCCESS = 500;
    private String videopath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ISCHECKPASS:
                    String isPass = msg.getData().getString("ispass");
                    int passposition = msg.getData().getInt("passpostion");
                    if (isPass != null) {
                        if (isPass.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "验收通过");
                            Intent intent = getIntent();
                            intent.putExtra("passposition", passposition);
                            setResult(GlobalContanstant.CHECKPASS, intent);
                            finish();
                        }
                    }

                    break;
                case ISUNCHECKPASS:
                    String isFail = msg.getData().getString("isfail");
                    int failposition = msg.getData().getInt("failpostion");
                    if (isFail != null) {
                        if (isFail.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "验收未通过");
                            Intent intent = getIntent();
                            intent.putExtra("failposition", failposition);
                            setResult(GlobalContanstant.CHECKFAIL, intent);
                            finish();

                        }
                    }

                    break;

                case SUCCESS:
                    videopath = (String) msg.obj;
                    if (videopath.isEmpty() || videopath == null || videopath.equals("false")) {
                        mllVideo.setVisibility(View.GONE);
                    } else {
                        mllVideo.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };
    private List<List<ImageUrl>> imageUrlReport;
    private List<List<ImageUrl>> imageUrlPost;
    private List<ImageUrl> imageUrl;
    private List<ImageUrl> imageUrlpost;
    private List<AudioUrl> audioUrls;
    private TextView mtvProblemLoca;
    private TextView mtvProblemAudio;
    private SoundUtil soundUtil;
    private TextView mtvfaname;
    private LinearLayout mllVideo;
    private ImageView mivPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", -1);
            detail = (Review) getIntent().getSerializableExtra("reviewRoad");
            imageUrlReport = (List<List<ImageUrl>>) getIntent().getSerializableExtra("imageUrlReport");
            imageUrlPost = (List<List<ImageUrl>>) getIntent().getSerializableExtra("imageUrlPost");
            audioUrls = (List<AudioUrl>) getIntent().getSerializableExtra("audioUrls");

        }

        setContentView(R.layout.activity_checkdetail);
        ButterKnife.bind(this);
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initAcitionbar();
        initView();

//        //请求是否有视屏
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    String videopath = SendRoadDetailActivity.getVideo(detail.getTaskNumber());
//                    Message message = Message.obtain();
//                    message.what = SUCCESS;
//                    message.obj = videopath;
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//
//                }
//            }
//        }.start();
        initData();
    }

    private void initView() {

        mtvPass = (TextView) findViewById(R.id.tv_check_pass);
        mtvBack = (TextView) findViewById(R.id.tv_check_back);
        mivReporte = (ImageView) findViewById(R.id.iv_check_detail_report);
        mivDealed = (ImageView) findViewById(R.id.iv_check_detail_dealed);

        mtvProblemLoca = (TextView) findViewById(R.id.tv_check_problem_loca);
        mtvProblemAudio = (TextView) findViewById(R.id.tv_check_problem_audio);

        mllVideo = (LinearLayout) findViewById(R.id.ll_video);
        mivPlay = (ImageView) findViewById(R.id.iv_play_video);
    }

    private void initData() {

        tvCheckReporter.setText(detail.getCheckPersonName());
        tvCheckFacility.setText(detail.getDeviceName());
        tvCheckFacilityPerson.setText(detail.getAdministrator());
        tvCheckFacilityTeam.setText(detail.getDeptName());
        tvCheckFacilityLoca.setText(detail.getAddressInfo());
        tvCheckReviewer.setText(detail.getZZCSSHPersonName());
        tvCheckDealer.setText(detail.getWXPersonName());
        tvCheckReportetime.setText(detail.getCheckTime());
        tvCheckReviewtime.setText(detail.getZZCSSHTime());
        tvCheckDecs.setText(detail.getWXInfo());
        tvCheckRequesttime.setText(detail.getZZCSTime());
        tvCheckResulttime.setText(detail.getWXTime());


        StringBuilder stringBuilder = new StringBuilder();
        List<String> errorInfo = detail.getErrorInfo();
        for (String s : errorInfo) {
            stringBuilder.append(s).append(";");
        }
        String problem = stringBuilder.toString().substring(0, stringBuilder.length() -1);
        tvCheckFacilityProblem.setText(problem);

                mtvProblemLoca.setText(detail.getRemarks());

        if (imageUrlReport != null) {
            imageUrl = imageUrlReport.get(position);
            if (imageUrl.size() != 0) {
                String imgurl = imageUrl.get(0).getImgurl();
                Glide.with(getApplicationContext()).load(imgurl).into(mivReporte);
            } else {

                Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(mivReporte);
            }
        }
        if (imageUrlPost != null) {
            imageUrlpost = imageUrlPost.get(position);
            if (imageUrlpost.size() != 0) {
                String imgurlpost = imageUrlpost.get(0).getImgurl();
                Glide.with(getApplicationContext()).load(imgurlpost).into(mivDealed);
            } else {
                Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(mivDealed);
            }
        }

        mivDealed.setOnClickListener(this);
        mivReporte.setOnClickListener(this);
        mtvPass.setOnClickListener(this);
        mtvBack.setOnClickListener(this);


        if (detail.getRemarks().isEmpty()) {
            final AudioUrl audioUrl = audioUrls.get(position);
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


        mivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CheckDetailActivity.this, PlayVideoActivity.class);
                intent1.putExtra("videoPath", videopath);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击显示大图
            case R.id.iv_check_detail_report:
                Intent intent = new Intent(v.getContext(), CheckReportBigPhotoActivity.class);
                intent.putExtra("imageurl", (Serializable) imageUrl);
                v.getContext().startActivity(intent);
                break;
            case R.id.iv_check_detail_dealed:
                Intent intent1 = new Intent(v.getContext(), CheckPostBigPhotoActivity.class);
                intent1.putExtra("imageUrlpost", (Serializable) imageUrlpost);
                v.getContext().startActivity(intent1);
                break;
            case R.id.tv_check_back:

                //记录当前状态
                //上传服务器
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
                            String result = toInspection(GlobalContanstant.GETUNCHECK, personID);
                            Message message = Message.obtain();
                            message.what = ISUNCHECKPASS;
                            Bundle bundle = new Bundle();
                            bundle.putInt("failpostion", position);
                            bundle.putString("isfail", result);
                            message.setData(bundle);
                            //message.obj =result;
                            handler.sendMessage(message);

                        } catch (Exception e) {

                        }
                    }
                }.start();

                break;
            case R.id.tv_check_pass:
                //记录当前状态

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
                            String result = toInspection(GlobalContanstant.GETPASSCHECK, personID);
                            Message message = Message.obtain();
                            message.what = ISCHECKPASS;
                            Bundle bundle = new Bundle();
                            bundle.putInt("passpostion", position);
                            bundle.putString("ispass", result);
                            message.setData(bundle);
                            handler.sendMessage(message);

                        } catch (Exception e) {

                        }
                    }
                }.start();


                break;
        }


    }


    private String toInspection(int phaseIndication, int personID) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);
        soapObject.addProperty("DeciceCheckNum", detail.getDeciceCheckNum());
        soapObject.addProperty("state", phaseIndication);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("opinion", "");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        envelope.bodyOut = soapObject;


        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);

        httpTransportSE.call(NetUrl.toInspection_SOAP_ACTION, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


    private void goHome() {
        Intent intent = new Intent(CheckDetailActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        startActivity(intent);
        finish();
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("作业详情");
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
