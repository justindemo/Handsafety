package com.xytsz.xytaj.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.xytsz.xytaj.adapter.SupplyCompanyAdapter;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.CustomDialog;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/25.
 * <p/>
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
    private String companyName;
    private int id;


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_CALL_PHONE:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "4008652007"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

            }
        }
    };
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            companyName = getIntent().getStringExtra("companyName");
            id = getIntent().getIntExtra("Id", -1);
        }
        setContentView(R.layout.activity_membershow);
        ButterKnife.bind(this);
        if (companyName != null) {
            initActionbar(companyName);
        }

        initData();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.FAIL:
                    //取消loading
                    //展示没有加载的页面
                    break;
                case GlobalContanstant.MYSENDSUCCESS:
                    //取消loading
                    //展示内容
                    break;
            }
        }
    };

    private List<String> mTitles = new ArrayList<>();

    private void initData() {
        //展示 loading
//        getData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                companydetailProgressbar.setVisibility(View.GONE);
                companyLv.setVisibility(View.VISIBLE);
            }
        }, 1000);

        Glide.with(this).load("http://123.126.40.12:8081/AppPath/AJ/2(2).png").into(memberIvBg);

        LinearLayoutManager man = new LinearLayoutManager(this);
        man.setOrientation(LinearLayoutManager.HORIZONTAL);
        companyProduceRv.setLayoutManager(man);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        companyCaseRv.setLayoutManager(manager);

        mTitles.clear();
        mTitles.add("产品");
        mTitles.add("掌上市政");
        mTitles.add("掌上安监");

        SupplyCompanyAdapter supplyCompanyAdapter = new SupplyCompanyAdapter(mTitles, this);
        companyProduceRv.setAdapter(supplyCompanyAdapter);
        companyCaseRv.setAdapter(supplyCompanyAdapter);
        supplyCompanyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title", mTitles.get(position));
                ProduceDetailActivity.intent2Produce(MemberCompanyShowActivity.this, bundle);
            }
        });


    }

    private void getData() {
        new Thread() {
            @Override
            public void run() {

                try {
                    String serviceData = getServiceData();
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.MYSENDSUCCESS;
                    message.obj = serviceData;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getServiceData() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.getCompanyDetailData);
        soapObject.addProperty("ID", id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String json = object.getProperty(0).toString();

        return json;

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this,requestCode,permissions,grantResults,mPermissionGrant);
    }

    @OnClick({R.id.iv_company_phone, R.id.iv_company_wechat, R.id.iv_company_qq, R.id.iv_company_address, R.id.tv_produce_company, R.id.tv_case})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_company_phone:
                PermissionUtils.requestPermission(MemberCompanyShowActivity.this, PermissionUtils.CODE_CALL_PHONE,
                        mPermissionGrant);
                break;
            case R.id.iv_company_wechat:
                customDialog = new CustomDialog(MemberCompanyShowActivity.this);
                customDialog.setContentIcon(R.mipmap.iv_sup_wechat);
                customDialog.setDetial("zzmxyt20070628");
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
                        ClipData clipData = ClipData.newPlainText("wechat", "772546099");
                        cm.setPrimaryClip(clipData);
                        ToastUtil.shortToast(getApplicationContext(),"复制成功");
                        customDialog.dismiss();
                    }
                }, "复制");
                customDialog.show();

                break;
            case R.id.iv_company_qq:
                customDialog = new CustomDialog(MemberCompanyShowActivity.this);
                customDialog.setContentIcon(R.mipmap.iv_sup_qq);
                customDialog.setDetial("772546099");
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
                        ClipData clipData = ClipData.newPlainText("qq", "772546099");
                        cm.setPrimaryClip(clipData);
                        ToastUtil.shortToast(getApplicationContext(),"复制成功");
                        customDialog.dismiss();
                    }
                }, "复制");
                customDialog.show();
                break;
            case R.id.iv_company_address:
                bundle = new Bundle();
                bundle.putDouble("longitude",116.35607);
                bundle.putDouble("latitude",39.768245);
                bundle.putString("company","北京向阳天科技有限公司");

                MarkPositionActivity.intent2MarkPosition(MemberCompanyShowActivity.this, bundle);
                break;
            case R.id.tv_produce_company:
                bundle = null;
                bundle = new Bundle();
                bundle.putString("tag","服务硬件");
                ProduceListActivity.intent2Activity(MemberCompanyShowActivity.this,bundle);
                break;
            case R.id.tv_case:
                bundle = null;
                bundle = new Bundle();
                bundle.putString("tag","成功案例");
                ProduceListActivity.intent2Activity(MemberCompanyShowActivity.this,bundle);
                break;
        }
    }
}
