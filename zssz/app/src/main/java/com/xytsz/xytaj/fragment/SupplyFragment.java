package com.xytsz.xytaj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.FacilityCategoryActivity;
import com.xytsz.xytaj.activity.MemberCompanyShowActivity;
import com.xytsz.xytaj.adapter.SupplyCompanyAdapter;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.Company;
import com.xytsz.xytaj.bean.Scroller;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.ScrollViewPager;
import com.xytsz.xytaj.util.JsonUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/5/23.
 * <p/>
 * 供应链
 */
public class SupplyFragment extends BaseFragment {

    private static final int SCROLLER = 1;
    private static final int COMPANY = 2;
    private static final int THIRD = 3;
    @Bind(R.id.search_tv_supply)
    TextView searchTvSupply;
    @Bind(R.id.supply_head_vp)
    LinearLayout supplyHeadVp;
    @Bind(R.id.ll_point)
    LinearLayout llPoint;
    @Bind(R.id.iv_sup_phone)
    ImageView ivSupPhone;
    @Bind(R.id.iv_sup_wechat)
    ImageView ivSupWechat;
    @Bind(R.id.iv_sup_qq)
    ImageView ivSupQq;
    @Bind(R.id.iv_sup_address)
    ImageView ivSupAddress;
    @Bind(R.id.supply_company_rv)
    RecyclerView supplyCompanyRv;
    @Bind(R.id.supply_thrid_rv)
    RecyclerView supplyThridRv;
    @Bind(R.id.tv_supply_company)
    TextView tvSupplyCompany;
    @Bind(R.id.tv_supply_third)
    TextView tvSupplyThird;
    private RelativeLayout mrlBoom;
    private RelativeLayout mrlHealth;
    private RelativeLayout mrlFire;
    private RelativeLayout mrlEnvir;

    private MyListener listener = new MyListener();
    private List<Scroller> scrollers;
    private List<Company> companies = new ArrayList<>();

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_supply, null);
        mrlBoom = (RelativeLayout) view.findViewById(R.id.rl_supply_boom);
        mrlHealth = (RelativeLayout) view.findViewById(R.id.rl_supply_health);
        mrlFire = (RelativeLayout) view.findViewById(R.id.rl_supply_fire);
        mrlEnvir = (RelativeLayout) view.findViewById(R.id.rl_supply_envir);

        mrlBoom.setOnClickListener(listener);
        mrlHealth.setOnClickListener(listener);
        mrlFire.setOnClickListener(listener);
        mrlEnvir.setOnClickListener(listener);



        return view;
    }

    private List<View> mllDots = new ArrayList<>();
    private List<String> mImageUrls = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        supplyCompanyRv.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        supplyThridRv.setLayoutManager(layoutManager2);
//        getData();

        mImageUrls.clear();

        mImageUrls.add("http://ww2.sinaimg.cn/large/610dc034jw1f3q5semm0wj20qo0hsacv.jpg");
        mImageUrls.add("http://123.126.40.12:8081/AppPath/AJ/ivvv.png");
        mImageUrls.add("http://123.126.40.12:8081/AppPath/AJ/banner3-01(1).jpg");
        initDot();
        ScrollViewPager scrollViewPager = new ScrollViewPager(getActivity(), mllDots);
        scrollViewPager.initImage(mImageUrls);
        scrollViewPager.roll();
        //把滑动的view pager 放进容器中
        supplyHeadVp.removeAllViews();
        supplyHeadVp.addView(scrollViewPager);


        mTitles.clear();
        mTitles.add("向阳天");
        mTitles.add("品质");
        mTitles.add("分公司");

        SupplyCompanyAdapter supplyCompanyAdapter = new SupplyCompanyAdapter(mTitles,getContext());
        supplyCompanyRv.setAdapter(supplyCompanyAdapter);
//        supplyThridRv.setAdapter(supplyCompanyAdapter);
        mTitles.add("科能文化");
        mTitles.add("卡尔玛分公司");
        supplyCompanyAdapter.notifyDataSetChanged();
        supplyThridRv.setAdapter(supplyCompanyAdapter);

        supplyCompanyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), MemberCompanyShowActivity.class);
                //传递Id；
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GlobalContanstant.MYSENDSUCCESS:
                    Bundle data = msg.getData();
                    String scroller = data.getString("scroller");
                    String company = data.getString("company");
                    String third = data.getString("third");
                    if (scroller != null && company != null && third != null){
                        if (!scroller.equals("[]")){
                            scrollers = JsonUtil.jsonToBean(scroller,
                                    new TypeToken<List<Scroller>>() {}.getType());
                            initDot();
                            ScrollViewPager scrollViewPager = new ScrollViewPager(getActivity(), mllDots);
//                            scrollViewPager.initImage(scrollers);
                            scrollViewPager.roll();
                            //把滑动的view pager 放进容器中
                            supplyHeadVp.removeAllViews();
                            supplyHeadVp.addView(scrollViewPager);
                        }
                        if (!company.equals("[]")){
                            companies.clear();
                            companies = JsonUtil.jsonToBean(company,
                                    new TypeToken<List<Company>>() {}.getType());
                            //bean 需要修改
                            tvSupplyCompany.setText(10+"家");
//                            SupplyCompanyAdapter supplyCompanyAdapter = new SupplyCompanyAdapter(companies,getContext());

//                            supplyCompanyRv.setAdapter(supplyCompanyAdapter);
                        }

                        if (!third.equals("[]")){
                            companies.clear();
                            companies = JsonUtil.jsonToBean(third,
                                    new TypeToken<List<Company>>() {}.getType() );

                            tvSupplyThird.setText(30+"家");
//                            SupplyCompanyAdapter supplyCompanyAdapter = new SupplyCompanyAdapter(companies,getContext());

//                            supplyThridRv.setAdapter(supplyCompanyAdapter);
                        }
                    }

                    break;
            }
        }
    };


    private void getData() {
        new Thread(){
            @Override
            public void run() {
                try {
                    String scrollerData = getServiceData(SCROLLER);
                    String companyData = getServiceData(COMPANY);
                    String thridData = getServiceData(THIRD);
                    if (scrollerData != null && companyData != null && thridData != null){
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        Bundle bundle = new Bundle();
                        bundle.putString("scroller",scrollerData);
                        bundle.putString("company",companyData);
                        bundle.putString("third",thridData);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {

                }
            }
        }.start();
    }

    private String getServiceData(int type) throws Exception{
        String methodName ="";
        switch (type){
            case SCROLLER:
                methodName = NetUrl.getScrollerData;
                break;
            case COMPANY:
                methodName = NetUrl.getCompanyData;
                break;
            case THIRD:
                methodName = NetUrl.getthirdData;
                break;
        }
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, methodName);

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


    private void initDot() {

        llPoint.removeAllViews();
        mllDots.clear();
        for (int i = 0; i < mImageUrls.size(); i++) {
            View view = new View(getActivity());

            if (i == 0) {
                view.setBackgroundResource(R.mipmap.dot_focus);

            } else {
                view.setBackgroundResource(R.mipmap.dot_normal);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(0, 0, 6, 0);

            view.setLayoutParams(params);
            llPoint.addView(view);

            mllDots.add(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.search_tv_supply, R.id.iv_sup_phone, R.id.iv_sup_wechat, R.id.iv_sup_qq,
            R.id.iv_sup_address,R.id.tv_supply_company, R.id.tv_supply_third})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_tv_supply:
                //搜索界面
                break;
            case R.id.iv_sup_phone:
                //打电话
                break;
            case R.id.iv_sup_wechat:
                //微信
                break;
            case R.id.iv_sup_qq:
                //qq
                break;
            case R.id.iv_sup_address:
                //地址
                break;
            case R.id.tv_supply_company:
                //公司企业  200家

                break;
            case R.id.tv_supply_third:
                // 第三方
                break;
        }
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getContext(), FacilityCategoryActivity.class);
            switch (v.getId()){
                case R.id.rl_supply_boom:
                    //粉尘防爆

                    intent.putExtra("category",GlobalContanstant.BOOM);
                    break;
                case R.id.rl_supply_envir:
                    intent.putExtra("category",GlobalContanstant.ENVIR);
//                    环境保护
                    break;
                case R.id.rl_supply_fire:
                    intent.putExtra("category",GlobalContanstant.FIRE);
//                    消防安全
                    break;
                case R.id.rl_supply_health:
                    intent.putExtra("category",GlobalContanstant.HEALTH);
//                    职业卫生
                    break;

            }
            startActivity(intent);
        }
    }


}
