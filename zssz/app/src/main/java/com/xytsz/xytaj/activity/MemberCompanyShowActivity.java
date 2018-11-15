package com.xytsz.xytaj.activity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.CompanyCaseAdapter;
import com.xytsz.xytaj.adapter.CompanyProduceAdapter;
import com.xytsz.xytaj.bean.Company;
import com.xytsz.xytaj.bean.CompanyCase;
import com.xytsz.xytaj.bean.CompanyCaseCallback;
import com.xytsz.xytaj.bean.CompanyDetailCallback;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.bean.CompanyProduce;
import com.xytsz.xytaj.bean.CompanyProduceCallback;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.net.UrlUtils;
import com.xytsz.xytaj.ui.CustomDialog;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2018/5/25.
 * <p>
 * 会员公司
 */
public class MemberCompanyShowActivity extends AppCompatActivity {

    @Bind(R.id.member_iv_bg)
    ImageView memberIvBg;
    @Bind(R.id.tv_company_intro)
    TextView tvCompanyIntro;
    @Bind(R.id.iv_company_phone)
    ImageView ivCompanyPhone;
    @Bind(R.id.iv_company_wechat)
    ImageView ivCompanyWechat;
    @Bind(R.id.iv_company_qq)
    ImageView ivCompanyQq;
    @Bind(R.id.iv_company_address)
    ImageView ivCompanyAddress;
    @Bind(R.id.tv_produce_company)
    TextView tvProduceCompany;
    @Bind(R.id.company_produce_rv)
    RecyclerView companyProduceRv;
    @Bind(R.id.tv_case)
    TextView tvCase;
    @Bind(R.id.company_case_rv)
    RecyclerView companyCaseRv;
    @Bind(R.id.company_lv)
    LinearLayout companyLv;
    @Bind(R.id.companydetail_progressbar)
    LinearLayout companydetailProgressbar;
    @Bind(R.id.tv_info_detail)
    TextView tvInfoDetail;
    private String companyName;


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CALL_PHONE:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + iphone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

            }
        }
    };
    private Bundle bundle;
    private CompanyList.DataBean companyDetail;
    private String iphone = "";
    private String qq = "";
    private String wechat = "";
    private double latitude;
    private double longitude;
    private int companyID;
    private int fromID;
    private int personId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            companyName = getIntent().getStringExtra("companyName");
            companyDetail = (CompanyList.DataBean) getIntent().
                    getSerializableExtra("companyDetail");
            companyID = getIntent().getIntExtra("companyID", -1);
            fromID = getIntent().getIntExtra("fromID", 0);

        }
        setContentView(R.layout.activity_membershow);
        ButterKnife.bind(this);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        if (companyName != null) {
            initActionbar(companyName);
        }
        if (companyDetail != null) {
            companyID = companyDetail.getID();
            iphone = companyDetail.getCompanyTel();
            qq = companyDetail.getCompanyQQ();
            wechat = companyDetail.getCompanyWeixin();
            latitude = companyDetail.getCompanyLatitude();
            longitude = companyDetail.getCompanyLongitude();
            Glide.with(getApplicationContext()).load(NetUrl.AllURL + companyDetail.getCompanyPicBack()).
                    placeholder(R.mipmap.holder_big)
                    .error(R.mipmap.holder_big)
                    .into(memberIvBg);
            String string = companyDetail.getCompanyDesc();
            string = string.replace("[br][br]", "\n");
            tvCompanyIntro.setText("\u3000\u3000" + string);
        } else {
            if (companyID == -1) {
                ToastUtil.shortToast(getApplicationContext(), "数据未加载");
                Glide.with(getApplicationContext()).load(R.mipmap.holder_big)
                        .into(memberIvBg);
                companydetailProgressbar.setVisibility(View.GONE);
                return;
            } else {
                //获取这个公司的详细信息。
                getCompanyDetail();

            }

        }

        initData();


    }


    private void getCompanyDetail() {
        String url = NetUrl.SERVERURL2 + NetUrl.getCompanyDetailData + companyID
                + "?f=" + fromID + "&AJPersonID=" + personId;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new CompanyDetailCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Company response, int id) {
                        companyName = response.getData().getCompanyName();
                        qq = response.getData().getCompanyQQ();
                        wechat = response.getData().getCompanyWeixin();
                        iphone = response.getData().getCompanyTel();
                        latitude = response.getData().getCompanyLatitude();
                        longitude = response.getData().getCompanyLongitude();
                        Glide.with(getApplicationContext())
                                .load(NetUrl.AllURL + response.getData().getCompanyPicBack())
                                .placeholder(R.mipmap.holder_big)
                                .error(R.mipmap.holder_big)
                                .into(memberIvBg);
                        String string = response.getData().getCompanyDesc();
                        string = string.replace("[br][br]", "\n");
                        tvCompanyIntro.setText("\u3000\u3000" + string);

                        initActionbar(companyName);
                    }
                });
    }

    private CompanyProduceAdapter companyProduceAdapter;
    private CompanyCaseAdapter companyCaseAdapter;
    private List<CompanyProduce.Produce> produces;
    private List<CompanyCase.DataBean> cases;

    private void initData() {
        companydetailProgressbar.setVisibility(View.VISIBLE);
        companyLv.setVisibility(View.VISIBLE);
        LinearLayoutManager man = new LinearLayoutManager(this);
        man.setOrientation(LinearLayoutManager.HORIZONTAL);
        companyProduceRv.setLayoutManager(man);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        companyCaseRv.setLayoutManager(manager);

        getProduceList(NetUrl.SERVERURL2 + NetUrl.getProduceList);
        getcaseList(NetUrl.SERVERURL2 + NetUrl.getCaseList);


    }

    private Map<String, Integer> params = new HashMap<>();

    private void getProduceList(String url) {
        params.clear();
        params.put("pageSize", 5);
        params.put("pageNo", 1);
        params.put("companyID", companyID);
        params.put("topIndex", 1);

        params.put("AJPersonID", personId);
        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils.get().url(spliceGetUrl)
                .build().execute(new CompanyProduceCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                companydetailProgressbar.setVisibility(View.GONE);
                ToastUtil.shortToast(getApplicationContext(), "数据未加载");
            }

            @Override
            public void onResponse(CompanyProduce response, int id) {
                companydetailProgressbar.setVisibility(View.GONE);
                if (response != null) {
                    produces = response.getData();
                    companyProduceAdapter = new CompanyProduceAdapter(produces, MemberCompanyShowActivity.this);
                    companyProduceRv.setAdapter(companyProduceAdapter);
                    companyProduceAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                            OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("Desc", produces.get(position).getProductDesc());
                            bundle.putString("title", produces.get(position).getProductName());
                            ProduceDetailActivity.intent2Produce(MemberCompanyShowActivity.this, bundle);
                        }
                    });
                }
            }
        });

    }

    private void getcaseList(String url) {
        params.clear();
        params.put("pageSize", 5);
        params.put("pageNo", 1);
        params.put("companyID", companyID);
        params.put("topIndex", 1);
        params.put("AJPersonID", personId);
        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils.get().url(spliceGetUrl)
                .build().execute(new CompanyCaseCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                companydetailProgressbar.setVisibility(View.GONE);
                ToastUtil.shortToast(getApplicationContext(), "数据未加载");
            }

            @Override
            public void onResponse(CompanyCase response, int id) {
                companydetailProgressbar.setVisibility(View.GONE);
                if (response != null) {
                    cases = response.getData();
                    companyCaseAdapter = new CompanyCaseAdapter(cases, MemberCompanyShowActivity.this);
                    companyCaseRv.setAdapter(companyCaseAdapter);
                    companyCaseAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.
                            OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("Desc", cases.get(position).getCaseDesc());
                            bundle.putString("title", cases.get(position).getCaseTitle());
                            ProduceDetailActivity.intent2Produce(MemberCompanyShowActivity.this, bundle);
                        }
                    });
                }

            }
        });
    }


    private void initActionbar(String title) {

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


    private CustomDialog customDialog;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    @OnClick({R.id.iv_company_phone, R.id.iv_company_wechat, R.id.iv_company_qq, R.id.iv_company_address,
            R.id.tv_produce_company, R.id.tv_case,R.id.tv_info_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_company_phone:
                PermissionUtils.requestPermission(MemberCompanyShowActivity.this, PermissionUtils.CODE_CALL_PHONE,
                        mPermissionGrant);
                break;
            case R.id.iv_company_wechat:
                customDialog = new CustomDialog(MemberCompanyShowActivity.this);
                customDialog.setContentIcon(R.mipmap.iv_sup_wechat);
                customDialog.setDetial(wechat);
                customDialog.setLeftOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                }, "取消");
                customDialog.setRightOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //复制
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("wechat", wechat);
                        cm.setPrimaryClip(clipData);
                        ToastUtil.shortToast(getApplicationContext(), "复制成功");
                        customDialog.dismiss();
                    }
                }, "复制");
                customDialog.show();

                break;
            case R.id.iv_company_qq:
                customDialog = new CustomDialog(MemberCompanyShowActivity.this);
                customDialog.setContentIcon(R.mipmap.iv_sup_qq);
                customDialog.setDetial(qq);
                customDialog.setLeftOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                }, "取消");
                customDialog.setRightOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //复制
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("qq", qq);
                        cm.setPrimaryClip(clipData);
                        ToastUtil.shortToast(getApplicationContext(), "复制成功");
                        customDialog.dismiss();
                    }
                }, "复制");
                customDialog.show();
                break;
            case R.id.iv_company_address:
                bundle = new Bundle();
                bundle.putDouble("longitude", longitude);
                bundle.putDouble("latitude", latitude);
                bundle.putString("company", companyName);

                MarkPositionActivity.intent2MarkPosition(MemberCompanyShowActivity.this, bundle);
                break;
            case R.id.tv_produce_company:
                bundle = null;
                bundle = new Bundle();
                bundle.putString("tag", "产品服务");
                bundle.putInt("ID", companyID);
                ProduceListActivity.intent2Activity(MemberCompanyShowActivity.this, bundle);
                break;
            case R.id.tv_case:
                bundle = null;
                bundle = new Bundle();
                bundle.putString("tag", "成功案例");
                bundle.putInt("ID", companyID);
                ProduceListActivity.intent2Activity(MemberCompanyShowActivity.this, bundle);
                break;

            case R.id.tv_info_detail:

                break;
        }
    }


}
