package com.xytsz.xytaj.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ForMyDis;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.global.Data;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/5/31.
 *
 *
 */
public class MyReporteDetailActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.tv_mydetail_name)
    TextView tvMydetailName;
    @Bind(R.id.tv_mydetail_reporter)
    TextView tvMydetailReporter;
    @Bind(R.id.tv_mydetail_diseasename)
    TextView tvMydetailDiseasename;
    @Bind(R.id.tv_mydetail_grade)
    TextView tvMydetailGrade;
    @Bind(R.id.tv_mydetail_dealtype)
    TextView tvMydetailDealtype;
    @Bind(R.id.tv_mydetail_fatype)
    TextView tvMydetailFatype;
    @Bind(R.id.tv_mydetail_pbtype)
    TextView tvMydetailPbtype;
    @Bind(R.id.tv_mydetail_reporteplace)
    TextView tvMydetailReporteplace;
    @Bind(R.id.tv_mydetail_faname)
    TextView tvMydetailFaname;
    @Bind(R.id.tv_mydetail_fasize)
    TextView tvMydetailFasize;
    @Bind(R.id.tv_mydetail_reportetime)
    TextView tvMydetailReportetime;
    @Bind(R.id.iv_detail_photo1)
    ImageView ivDetailPhoto1;
    @Bind(R.id.iv_detail_photo2)
    ImageView ivDetailPhoto2;
    @Bind(R.id.iv_detail_photo3)
    ImageView ivDetailPhoto3;
    @Bind(R.id.ll_iv)
    LinearLayout llIv;
    @Bind(R.id.tv_my_problem_audio)
    TextView tvMyProblemAudio;
    @Bind(R.id.tv_my_detail_address)
    TextView tvMyDetailAddress;
    @Bind(R.id.tv_detail_diseasedes)
    TextView tvDetailDiseasedes;
    @Bind(R.id.tv_mydetail_loca)
    TextView tvMydetailLoca;
    @Bind(R.id.tv_myreview_state)
    TextView tvMyreviewState;
    @Bind(R.id.tv_state)
    TextView tvState;
    private ForMyDis detail;
    private List<ImageUrl> imageUrlReport;
    private int id;
    private AudioUrl audioUrl;
    private SoundUtil soundUtil;
    private int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {

            detail = (ForMyDis) getIntent().getSerializableExtra("detail");

            imageUrlReport = (List<ImageUrl>) getIntent().getSerializableExtra("imageUrlReport");
            audioUrl = (AudioUrl) getIntent().getSerializableExtra("audioUrl");

            flag = getIntent().getIntExtra("flag", -1);
        }


        setContentView(R.layout.activity_myreportdetail);
        ButterKnife.bind(this);


        String reporte = getString(R.string.myreported);
        String review = getString(R.string.myreviewed);

        switch (flag) {
            case 1:
                initAcitionbar(reporte);
                break;
            case 2:
                initAcitionbar(review);
                break;
        }
        initData();


    }


    private void initData() {


        int phaseIndication = detail.getPhaseIndication();
        switch (phaseIndication) {

            case 1:
                tvMyreviewState.setText("未下派");
                break;
            case 2:
                tvMyreviewState.setText("未处置");
                break;
            case 3:
                tvMyreviewState.setText("处置完");
                break;
            case 5:
                tvMyreviewState.setText("已驳回");
                break;
        }



        String upload_person_id = detail.getUpload_Person_ID() + "";

        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
        List<String> personNamelist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST);
        List<String> personIDlist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST);

        for (int i = 0; i < personIDlist.size(); i++) {
            if (upload_person_id.equals(personIDlist.get(i))) {
                id = i;
            }
        }

        String userName = personNamelist.get(id);
        tvMydetailReporter.setText(userName);
        int disposalLevel_id = detail.getDisposalLevel_ID() - 1;
        int level = detail.getLevel();
        tvMydetailDiseasename.setText(Data.pbname[level]);
        tvMydetailGrade.setText(Data.grades[disposalLevel_id]);

        tvMydetailFatype.setText(detail.getFacilityType_Name());
        tvMydetailDealtype.setText(detail.getDealType_Name());

        tvMydetailPbtype.setText(detail.getDiseaseType_Name());


        tvMydetailReporteplace.setText(detail.getStreetAddress_Name());

        tvDetailDiseasedes.setText(detail.getDiseaseDescription());

        tvMydetailFaname.setText(detail.getFacilityName_Name());

        tvMyDetailAddress.setText(detail.getAddressDescription());
        tvMydetailFasize.setText(detail.getFacilitySpecifications_Name());

        String uploadTime = detail.getUploadTime();
        tvMydetailReportetime.setText(uploadTime);
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
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {
                        if (!audioUrl.getTime().isEmpty()) {
                            tvMyDetailAddress.setVisibility(View.GONE);
                            tvMyProblemAudio.setVisibility(View.VISIBLE);
                            soundUtil = new SoundUtil();

                            tvMyProblemAudio.setText(audioUrl.getTime());
                            tvMyProblemAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Drawable drawable = getResources().getDrawable(R.mipmap.pause);
                                    final Drawable drawableRight = getResources().getDrawable(R.mipmap.play);

                                    tvMyProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);


                                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                        @Override
                                        public void onFinish() {
                                            tvMyProblemAudio.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
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
                        tvMyDetailAddress.setVisibility(View.VISIBLE);
                        tvMyProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                tvMyDetailAddress.setVisibility(View.VISIBLE);
                tvMyProblemAudio.setVisibility(View.GONE);
            }

        } else {
            tvMyDetailAddress.setVisibility(View.VISIBLE);
            tvMyProblemAudio.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MyReporteDetailActivity.this, BigPictureActivity.class);
        intent.putExtra("imageUrls", (Serializable) imageUrlReport);
        startActivity(intent);
    }

    private void initAcitionbar(String title) {
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

    @OnClick({R.id.tv_mydetail_loca, R.id.tv_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mydetail_loca:
                double longitude = detail.getLongitude();
                double latitude = detail.getLatitude();
                Intent intent = new Intent(MyReporteDetailActivity.this, MarkPositionActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);
                break;
            case R.id.tv_state:
                final Dialog dialog = new AlertDialog.Builder(MyReporteDetailActivity.this).create();
                dialog.setCancelable(true);// 可以用“返回键”取消
                dialog.setCanceledOnTouchOutside(true);//
                dialog.show();
                View inflate = LayoutInflater.from(MyReporteDetailActivity.this).inflate(R.layout.myreview_state_dialog, null);
                dialog.setContentView(inflate);

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
}
