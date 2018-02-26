package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.ProblemLocation;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.util.JsonUtil;

import org.ksoap2.SoapEnvelope;
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
 * Created by admin on 2017/3/31.
 * 病害详细但
 */
public class ProcessDetailActivity extends AppCompatActivity {

    @Bind(R.id.tv_report_time)
    TextView tvReportTime;
    @Bind(R.id.tv_review_time)
    TextView tvReviewTime;
    @Bind(R.id.tv_deal_time)
    TextView tvDealTime;
    @Bind(R.id.tv_send_time)
    TextView tvSendTime;
    @Bind(R.id.tv_post_time)
    TextView tvPostTime;
    @Bind(R.id.tv_check_time)
    TextView tvCheckTime;
    @Bind(R.id.predeal_photo)
    ImageView predealPhoto;
    @Bind(R.id.dealing_photo)
    ImageView dealingPhoto;
    @Bind(R.id.dealed_photo)
    ImageView dealedPhoto;
    @Bind(R.id.tv_report_name)
    TextView tvReportName;
    @Bind(R.id.tv_review_name)
    TextView tvReviewName;

    @Bind(R.id.tv_deal_name)
    TextView tvDealName;

    @Bind(R.id.tv_check_name)
    TextView tvCheckName;
    @Bind(R.id.tv_report_tasknuber)
    TextView tvReportTasknuber;
    @Bind(R.id.iv_report_statu)
    ImageView ivReportStatu;
    @Bind(R.id.iv_report_statu1)
    ImageView ivReportStatu1;
    @Bind(R.id.iv_review_statu)
    ImageView ivReviewStatu;
    @Bind(R.id.iv_review_statu1)
    ImageView ivReviewStatu1;
    @Bind(R.id.iv_send_statu)
    ImageView ivSendStatu;
    @Bind(R.id.iv_send_statu1)
    ImageView ivSendStatu1;
    @Bind(R.id.iv_deal_statu)
    ImageView ivDealStatu;
    @Bind(R.id.iv_deal_statu1)
    ImageView ivDealStatu1;
    @Bind(R.id.iv_post_statu)
    ImageView ivPostStatu;
    @Bind(R.id.iv_post_statu1)
    ImageView ivPostStatu1;
    @Bind(R.id.iv_check_statu)
    ImageView ivCheckStatu;
    private ProblemLocation.ProblemListBean detail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            detail = (ProblemLocation.ProblemListBean) getIntent().getSerializableExtra("detail");
        }
        setContentView(R.layout.activity_processdetail);
        ButterKnife.bind(this);
        initAcitionbar();
        initData();
    }


    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.processdetail);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void initData() {

        int phaseindication = detail.getPhaseindication();
        String uploadtime = detail.getUploadtime();
        String reviewedTime = detail.getReviewedTime();
        String sendedTime = detail.getSendedTime();
        String dealedTime = detail.getDealedTime();
        String postedTime = detail.getPostedTime();
        //String checkedTime = detail.getCheckedTime();
        String tasknumber = detail.getTasknumber();
        String upload_person_name = detail.getUpload_Person_name();
        String reviewed_person_name = detail.getReviewed_Person_Name();
        String dealed_person_name = detail.getDealed_Person_name();

        //String checked_person_name = detail.getChecked_Person_Name();
        tvReportTasknuber.setText(tasknumber);

        switch (phaseindication) {
            //上报完  审核中  上报为红色 其他无
            case 0:
                ivReportStatu.setImageResource(R.mipmap.iv_complete);
                ivReportStatu1.setImageResource(R.mipmap.iv_complete_nor);
                break;
            //上报完  审核完= 红   下派中 上报为绿色 其他无
            case 1:
                ivReportStatu.setImageResource(R.mipmap.iv_complete);
                ivReportStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivReviewStatu.setImageResource(R.mipmap.iv_complete);
                ivReviewStatu1.setImageResource(R.mipmap.iv_complete_nor);
                break;
            //下派完了
            case 2:
                ivReportStatu.setImageResource(R.mipmap.iv_complete);
                ivReportStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivReviewStatu.setImageResource(R.mipmap.iv_complete);
                ivReviewStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivSendStatu.setImageResource(R.mipmap.iv_complete);
                ivSendStatu1.setImageResource(R.mipmap.iv_complete_nor);
                //如果没有处置时间，那么处置是红的
                if (!dealedTime.equals("")) {
                    if (postedTime.equals("")) {
                       ivDealStatu.setImageResource(R.mipmap.iv_complete);
                       ivDealStatu1.setImageResource(R.mipmap.iv_complete_nor);
                    } else {
                        ivPostStatu.setImageResource(R.mipmap.iv_complete);
                        ivPostStatu1.setImageResource(R.mipmap.iv_complete_nor);
                    }
                }
                break;

            //处置和报验是一样的
            case 3:
                ivReportStatu.setImageResource(R.mipmap.iv_complete);
                ivReportStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivReviewStatu.setImageResource(R.mipmap.iv_complete);
                ivReviewStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivSendStatu.setImageResource(R.mipmap.iv_complete);
                ivSendStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivDealStatu.setImageResource(R.mipmap.iv_complete);
                ivDealStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivPostStatu.setImageResource(R.mipmap.iv_complete);
                ivPostStatu1.setImageResource(R.mipmap.iv_complete_nor);
                ivCheckStatu.setImageResource(R.mipmap.iv_uncom);
                break;

        }


        tvReportTime.setText(uploadtime);
        tvReviewTime.setText(reviewedTime);
        tvSendTime.setText(sendedTime);
        tvDealTime.setText(dealedTime);
        tvPostTime.setText(postedTime);
        // tvCheckTime.setText(checkedTime);

        tvReportName.setText(upload_person_name);
        tvReviewName.setText(reviewed_person_name);
        tvDealName.setText(dealed_person_name);
        //tvCheckName.setText(checked_person_name);

        new Thread() {


            @Override
            public void run() {

                try {
                    String preImgUrl = getPreImgUrl(detail.getTasknumber());
                    String rngImgUrl = getRngImgUrl(detail.getTasknumber());
                    String postImgUrl = getPostImgUrl(detail.getTasknumber());
                    if (!TextUtils.equals(preImgUrl, "[]") && !TextUtils.equals(rngImgUrl, "[]") && !TextUtils.equals(postImgUrl, "[]")) {
                        final List<ImageUrl> preImgUrls = JsonUtil.jsonToBean(preImgUrl, new TypeToken<List<ImageUrl>>() {
                        }.getType());
                        final List<ImageUrl> rngImgUrls = JsonUtil.jsonToBean(rngImgUrl, new TypeToken<List<ImageUrl>>() {
                        }.getType());
                        final List<ImageUrl> postImgUrls = JsonUtil.jsonToBean(postImgUrl, new TypeToken<List<ImageUrl>>() {
                        }.getType());

                        allImageUrls.add(preImgUrls);
                        allImageUrls.add(rngImgUrls);
                        allImageUrls.add(postImgUrls);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //严谨性判断 ：如果没有到报验那步，就显示默认图片

                                if (preImgUrls != null && rngImgUrls != null && postImgUrls != null) {
                                    predealPhoto.setEnabled(true);
                                    dealedPhoto.setEnabled(true);
                                    dealingPhoto.setEnabled(true);
                                    Glide.with(getApplicationContext()).load(allImageUrls.get(0).get(0).getImgurl()).into(predealPhoto);
                                    Glide.with(getApplicationContext()).load(allImageUrls.get(1).get(0).getImgurl()).into(dealingPhoto);
                                    Glide.with(getApplicationContext()).load(allImageUrls.get(2).get(0).getImgurl()).into(dealedPhoto);

                                }
                            }
                        });
                    }

                } catch (Exception e) {

                }
            }
        }.start();

        //默认显示
        if (allImageUrls.size() == 0) {
            predealPhoto.setImageResource(R.mipmap.prepost);
            dealingPhoto.setImageResource(R.mipmap.prepost);
            dealedPhoto.setImageResource(R.mipmap.prepost);
            predealPhoto.setEnabled(false);
            dealedPhoto.setEnabled(false);
            dealingPhoto.setEnabled(false);
        }

    }


    private List<List<ImageUrl>> allImageUrls = new ArrayList<>();

    @OnClick({R.id.tv_report_time, R.id.tv_review_time, R.id.tv_deal_time, R.id.tv_send_time, R.id.tv_post_time, R.id.tv_check_time, R.id.predeal_photo, R.id.dealing_photo, R.id.dealed_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.predeal_photo:
                Intent intent1 = new Intent(view.getContext(), PrePhotoActivity.class);
                intent1.putExtra("imageurl", (Serializable) allImageUrls.get(0));
                startActivity(intent1);
                break;
            case R.id.dealing_photo:
                Intent intent2 = new Intent(view.getContext(), IngPhotoActivity.class);
                intent2.putExtra("imageurls", (Serializable) allImageUrls.get(1));
                startActivity(intent2);
                break;
            case R.id.dealed_photo:
                Intent intent3 = new Intent(view.getContext(), PostPhotoActivity.class);
                intent3.putExtra("imageurlss", (Serializable) allImageUrls.get(2));
                startActivity(intent3);
                break;
        }
    }

    private String getPreImgUrl(String taskNumber) throws Exception {
        SoapObject soapobject = new SoapObject(NetUrl.nameSpace, NetUrl.getPreImageURLmethodName);
        soapobject.addProperty("TaskNumber", taskNumber);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapobject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getPreImageURLSoap_Action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;
    }

    private String getRngImgUrl(String taskNumber) throws Exception {
        SoapObject soapobject = new SoapObject(NetUrl.nameSpace, NetUrl.getRngImageURLmethodName);
        soapobject.addProperty("TaskNumber", taskNumber);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapobject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getRngImageURLSoap_Action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;
    }

    private String getPostImgUrl(String taskNumber) throws Exception {
        SoapObject soapobject = new SoapObject(NetUrl.nameSpace, NetUrl.getPostImageURLmethodName);
        soapobject.addProperty("TaskNumber", taskNumber);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.bodyOut = soapobject;

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(NetUrl.getPostImageURL_SOAP_ACTION, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();

        return result;

    }


}
