package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import butterknife.OnClick;

/**
 * Created by admin on 2017/6/16.
 * 病害详情单
 */
public class SendRoadDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SUCCESS = 500;

    @Bind(R.id.tv_send_detail_reportetime)
    TextView tvSendDetailReportetime;
    @Bind(R.id.tv_send_detail_address)
    TextView tvSendDetailAddress;
    @Bind(R.id.iv_send_detail_photo1)
    ImageView ivSendDetailPhoto1;
    @Bind(R.id.iv_send_detail_photo2)
    ImageView ivSendDetailPhoto2;
    @Bind(R.id.iv_send_detail_photo3)
    ImageView ivSendDetailPhoto3;
    @Bind(R.id.tv_send_problem_audio)
    TextView tvSendProblemAudio;
    @Bind(R.id.iv_play_video)
    ImageView ivPlayVideo;
    @Bind(R.id.ll_video)
    LinearLayout llVideo;
    @Bind(R.id.tv_send_detail_diseasedes)
    TextView tvSendDetailDiseasedes;
    @Bind(R.id.tv_send_detail_facility)
    TextView tvSendDetailFacility;
    @Bind(R.id.tv_send_detail_facility_person)
    TextView tvSendDetailFacilityPerson;
    @Bind(R.id.tv_send_detail_facility_problem)
    TextView tvSendDetailFacilityProblem;
    @Bind(R.id.tv_send_detail_advice)
    EditText tvSendDetailAdvice;
    @Bind(R.id.tv_send_pass)
    TextView tvSendPass;
    @Bind(R.id.tv_send_back)
    TextView tvSendBack;
    @Bind(R.id.tv_send_detail_reporter)
    TextView tvSendDetailReporter;
    @Bind(R.id.tv_send_detail_facility_loca)
    TextView tvSendDetailFacilityLoca;
    @Bind(R.id.tv_send_detail_facility_team)
    TextView tvSendDetailFacilityTeam;
    @Bind(R.id.ll_leader_advice)
    LinearLayout llLeaderAdvice;
    @Bind(R.id.tv_send_detail_leader_advice)
    TextView tvSendDetailLeaderAdvice;
    @Bind(R.id.ll_send_mine_advice)
    LinearLayout llSendMineAdvice;
    @Bind(R.id.ll_road_idea)
    LinearLayout llRoadIdea;

    private Review detail;
    private List<ImageUrl> imageUrls;
    private int id;
    private AudioUrl audioUrl;
    private SoundUtil soundUtil;
    private String videopath;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    videopath = (String) msg.obj;
                    if (videopath.isEmpty() || videopath == null || videopath.equals("false")) {
                        llVideo.setVisibility(View.GONE);
                    } else {
                        llVideo.setVisibility(View.VISIBLE);
                    }

                    break;
                case GlobalContanstant.CHECKROADPASS:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), "通知成功");
                            Intent intent = getIntent();
                            intent.putExtra("position", position);
                            setResult(301, intent);
                            finish();
                        }
                    }
                    break;
                case GlobalContanstant.CHECKROADFAIL:
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
    private String advice;
    private int position;
    private int tag;
    private int state;
    private int personID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            detail = (Review) getIntent().getSerializableExtra("detail");
            imageUrls = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrls");
            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");
            position = getIntent().getIntExtra("position", -1);
            tag = getIntent().getIntExtra("tag", -1);
        }

        setContentView(R.layout.activity_sendroaddetail);
        ButterKnife.bind(this);

        initAcitionbar();
        //请求是否有视屏
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    String videopath = getVideo(detail.getTaskNumber());
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

    public static String getVideo(String taskNumber) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getVideoMethodName);
        //传递的参数
        soapObject.addProperty("TaskNumber", taskNumber);

        Log.i("soapo", soapObject.toString());
        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String result = object.getProperty(0).toString();
        return result;

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

    private void initData() {
        personID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);

        tvSendDetailReporter.setText(detail.getCheckPersonName());
        tvSendDetailFacility.setText(detail.getDeviceName());
        tvSendDetailFacilityPerson.setText(detail.getAdministrator());
        tvSendDetailFacilityTeam.setText(detail.getDeptName());
        StringBuilder stringBuilder = new StringBuilder();
        List<String> errorInfo = detail.getErrorInfo();
        for (String s : errorInfo) {
            stringBuilder.append(s).append(";");
        }
        String problem = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        tvSendDetailFacilityProblem.setText(problem);

        tvSendDetailReportetime.setText(detail.getCheckTime());
        tvSendDetailAddress.setText(detail.getRemarks());
        tvSendDetailFacilityLoca.setText(detail.getAddressInfo());


        if (imageUrls.size() != 0) {
            if (imageUrls.size() == 1) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                ivSendDetailPhoto2.setVisibility(View.INVISIBLE);
                ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 2) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                ivSendDetailPhoto3.setVisibility(View.INVISIBLE);
            } else if (imageUrls.size() == 3) {
                Glide.with(getApplicationContext()).load(imageUrls.get(0).getImgurl()).into(ivSendDetailPhoto1);
                Glide.with(getApplicationContext()).load(imageUrls.get(1).getImgurl()).into(ivSendDetailPhoto2);
                Glide.with(getApplicationContext()).load(imageUrls.get(2).getImgurl()).into(ivSendDetailPhoto3);
            }
        } else {
            ivSendDetailPhoto1.setVisibility(View.VISIBLE);
            ivSendDetailPhoto1.setVisibility(View.INVISIBLE);
            ivSendDetailPhoto1.setVisibility(View.INVISIBLE);
            Glide.with(getApplicationContext()).load(R.mipmap.prepost).into(ivSendDetailPhoto1);

        }

        ivSendDetailPhoto1.setOnClickListener(this);
        ivSendDetailPhoto2.setOnClickListener(this);
        ivSendDetailPhoto3.setOnClickListener(this);


        if (detail.getRemarks().isEmpty()) {
            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {

                        if (!audioUrl.getTime().isEmpty()) {
                            tvSendDetailAddress.setVisibility(View.GONE);
                            tvSendProblemAudio.setVisibility(View.VISIBLE);
                            soundUtil = new SoundUtil();
                            tvSendProblemAudio.setText(audioUrl.getTime());

                            tvSendProblemAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                    final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                    tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


                                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                        @Override
                                        public void onFinish() {
                                            tvSendProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
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
                        tvSendDetailAddress.setVisibility(View.VISIBLE);
                        tvSendProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                tvSendDetailAddress.setVisibility(View.VISIBLE);
                tvSendProblemAudio.setVisibility(View.GONE);
            }

        } else {
            tvSendDetailAddress.setVisibility(View.VISIBLE);
            tvSendProblemAudio.setVisibility(View.GONE);
        }

        switch (tag) {
            case 1:
                state = GlobalContanstant.GETSEND;
                llLeaderAdvice.setVisibility(View.VISIBLE);
                llSendMineAdvice.setVisibility(View.GONE);
                //然后显示对应的节点的上传的意见
                break;
            case 2:
                state = GlobalContanstant.GETPOST;
                llRoadIdea.setVisibility(View.GONE);
                tvSendDetailLeaderAdvice.setText(detail.getZZTZInfo());
                break;
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SendRoadDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrls);
        startActivity(intent);
    }

    @OnClick({R.id.iv_play_video, R.id.tv_send_pass, R.id.tv_send_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_play_video:
                Intent intent1 = new Intent(SendRoadDetailActivity.this, PlayVideoActivity.class);
                intent1.putExtra("videoPath", videopath);
                startActivity(intent1);
                break;
            case R.id.tv_send_pass:
                //拿到意见
                advice = tvSendDetailAdvice.getText().toString();
                UpData(advice);

                break;
            case R.id.tv_send_back:
                advice = tvSendDetailAdvice.getText().toString();
                UpData(advice);
                break;
        }
    }

    private void UpData(final String advice) {
        new Thread() {
            @Override
            public void run() {

                try {
                    String data = toNet(advice);
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKROADPASS;
                    message.obj = data;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKROADFAIL;
                    handler.sendMessage(message);
                }

            }
        }.start();
    }

    //选择一个
    private String toNet(String advice) throws Exception {
        //发通知节点//哪个单子，状态，通知人是谁，意见是什么,时间是否需要。
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reviewmethodName);

        soapObject.addProperty("DeciceCheckNum", detail.getDeciceCheckNum());
        soapObject.addProperty("state", state);
        soapObject.addProperty("personId", personID);
        soapObject.addProperty("opinion", advice);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String data = object.getProperty(0).toString();

        return data;
    }


}
