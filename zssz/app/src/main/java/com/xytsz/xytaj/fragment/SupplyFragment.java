package com.xytsz.xytaj.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.CompanyListActivity;
import com.xytsz.xytaj.activity.FacilityCategoryActivity;
import com.xytsz.xytaj.activity.MarkPositionActivity;
import com.xytsz.xytaj.activity.MemberCompanyShowActivity;
import com.xytsz.xytaj.adapter.SupplyCompanyAdapter;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.Company;

import com.xytsz.xytaj.bean.CompanyDetailCallback;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.bean.CompanyListCallback;
import com.xytsz.xytaj.bean.FacilityHead;
import com.xytsz.xytaj.bean.FacilityHeadCallback;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.net.UrlUtils;
import com.xytsz.xytaj.ui.CustomDialog;
import com.xytsz.xytaj.ui.ScrollViewPager;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by admin on 2018/5/23.
 * <p/>
 * 供应链
 */
public class SupplyFragment extends BaseFragment {

    private static final int SCROLLER = 1;
    private static final int COMPANY = 2;
    private static final int THIRD = 3;

    private TextView searchTvSupply;

    private LinearLayout supplyHeadVp;

    private LinearLayout llPoint;

    private ImageView ivSupPhone;

    private ImageView ivSupWechat;

    private ImageView ivSupQq;
    private ImageView ivSupAddress;
    private RecyclerView supplyCompanyRv;

    private RecyclerView supplyThridRv;

    private TextView tvSupplyCompany;

    private TextView tvSupplyThird;
    private RelativeLayout mrlBoom;
    private RelativeLayout mrlHealth;
    private RelativeLayout mrlFire;
    private RelativeLayout mrlEnvir;

    private MyListener listener = new MyListener();
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
    private List<CompanyList.DataBean> thirdDatas;
    private List<CompanyList.DataBean> companyDatas;
    private SupplyCompanyAdapter supplyCompanyAdapter;
    private int personId;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_supply, null);
        mrlBoom = (RelativeLayout) view.findViewById(R.id.rl_supply_boom);
        mrlHealth = (RelativeLayout) view.findViewById(R.id.rl_supply_health);
        mrlFire = (RelativeLayout) view.findViewById(R.id.rl_supply_fire);
        mrlEnvir = (RelativeLayout) view.findViewById(R.id.rl_supply_envir);

        searchTvSupply = (TextView) view.findViewById(R.id.search_tv_supply);
        tvSupplyCompany = (TextView) view.findViewById(R.id.tv_supply_company);
        tvSupplyThird = (TextView) view.findViewById(R.id.tv_supply_third);
        supplyHeadVp = (LinearLayout) view.findViewById(R.id.supply_head_vp);
        llPoint = (LinearLayout) view.findViewById(R.id.ll_point);

        supplyCompanyRv = (RecyclerView) view.findViewById(R.id.supply_company_rv);
        supplyThridRv = (RecyclerView) view.findViewById(R.id.supply_thrid_rv);

        ivSupPhone = (ImageView) view.findViewById(R.id.iv_sup_phone);
        ivSupAddress = (ImageView) view.findViewById(R.id.iv_sup_address);
        ivSupQq = (ImageView) view.findViewById(R.id.iv_sup_qq);
        ivSupWechat = (ImageView) view.findViewById(R.id.iv_sup_wechat);


        ivSupQq.setOnClickListener(listener);
        ivSupWechat.setOnClickListener(listener);
        ivSupAddress.setOnClickListener(listener);
        ivSupPhone.setOnClickListener(listener);
        tvSupplyCompany.setOnClickListener(listener);
        tvSupplyThird.setOnClickListener(listener);
        mrlBoom.setOnClickListener(listener);
        mrlHealth.setOnClickListener(listener);
        mrlFire.setOnClickListener(listener);
        mrlEnvir.setOnClickListener(listener);
        return view;
    }

    private List<View> mllDots = new ArrayList<>();

    @Override
    public void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        supplyCompanyRv.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        supplyThridRv.setLayoutManager(layoutManager2);
        getData();

    }


    private void getData() {

        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        //获取轮播公司列表
        getServiceData(NetUrl.SERVERURL2 + NetUrl.getScrollerData + NetUrl.tag + personId);

        //获取总公司的详细信息 1代表总公司
        getxytCompanyDate(NetUrl.SERVERURL2 + NetUrl.getCompanyDetailData + 1 + NetUrl.tag + personId);
        //获取首页显示的公司
        getCompanyList();
        //获取第三方服务公司列表
        getThirdList();

    }

    private void getThirdList() {
        String url = NetUrl.SERVERURL2 + NetUrl.getCompanyData;
        params.clear();
        params.put("pageSize", pageSize);
        params.put("pageNo", 1);
        params.put("companyClass", GlobalContanstant.NOTIFY);
        params.put("topIndex", GlobalContanstant.REVIEW);
        params.put("AJPersonID", personId);

        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils
                .get()
                .url(spliceGetUrl)
                .build()
                .execute(new CompanyListCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(CompanyList response, int id) {
                        if (response != null) {
                            int dataCount = response.getDataCount();
                            if (dataCount != 0) {
                                if (tvSupplyThird != null) {

                                    tvSupplyThird.setText(dataCount + "家");
                                }
                            }
                            thirdDatas = response.getData();
                            supplyCompanyAdapter = new SupplyCompanyAdapter(thirdDatas, getContext());
                            if (supplyThridRv != null) {
                                supplyThridRv.setAdapter(supplyCompanyAdapter);

                                supplyCompanyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(getContext(), MemberCompanyShowActivity.class);
                                        //传递Id；
                                        intent.putExtra("companyName", thirdDatas.get(position).getCompanyName());
//                                        intent.putExtra("companyDetail", thirdDatas.get(position));
                                        intent.putExtra("companyID", thirdDatas.get(position).getID());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private int pageSize = 6;

    private Map<String, Integer> params = new HashMap<>();

    private void getCompanyList() {
        String url = NetUrl.SERVERURL2 + NetUrl.getCompanyData;
        params.clear();
        params.put("pageSize", pageSize);
        params.put("pageNo", 1);
        params.put("companyClass", GlobalContanstant.REVIEW);
        params.put("topIndex", GlobalContanstant.REVIEW);
        params.put("AJPersonID", personId);

        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils
                .get()
                .url(spliceGetUrl)
                .build()
                .execute(new CompanyListCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(CompanyList response, int id) {
                        if (response != null) {
                            int dataCount = response.getDataCount();
                            if (dataCount != 0) {
                                if (tvSupplyCompany != null) {
                                    tvSupplyCompany.setText(dataCount + "家");
                                }
                            }
                            companyDatas = response.getData();
                            supplyCompanyAdapter = new SupplyCompanyAdapter(companyDatas, getContext());
                            if (supplyCompanyRv != null) {
                                supplyCompanyRv.setAdapter(supplyCompanyAdapter);

                                supplyCompanyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(getContext(), MemberCompanyShowActivity.class);
                                        //传递Id；
                                        intent.putExtra("companyName", companyDatas.get(position).getCompanyName());
                                        intent.putExtra("companyDetail", companyDatas.get(position));
                                        intent.putExtra("companyID", companyDatas.get(position).getID());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void getxytCompanyDate(String url) {
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
                        qq = response.getData().getCompanyQQ();
                        wechat = response.getData().getCompanyWeixin();
                        iphone = response.getData().getCompanyTel();
                        latitude = response.getData().getCompanyLatitude();
                        longitude = response.getData().getCompanyLongitude();
                        companyName = response.getData().getCompanyName();
                    }
                });
    }

    private String wechat;
    private String qq;
    private String iphone;
    private double latitude;
    private double longitude;
    private String companyName;


    private void getServiceData(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FacilityHeadCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(FacilityHead response, int id) {
                        if (response != null) {
                            initScroller(response);
                        }
                    }
                });
    }

    private List<FacilityHead.DataBean> scrollerDatas = new ArrayList<>();

    private void initScroller(FacilityHead response) {
        scrollerDatas.clear();
        if (response.getData() != null && response.getData().size() != 0) {
            for (FacilityHead.DataBean dataBean : response.getData()) {
                if (dataBean.getAdClass() == SCROLLER) {
                    scrollerDatas.add(dataBean);
                }
            }
            if (getActivity() != null) {
                initDot(scrollerDatas);
                ScrollViewPager scrollViewPager = new ScrollViewPager(getActivity(), mllDots);
                scrollViewPager.initImage(scrollerDatas);
                scrollViewPager.roll();
                //把滑动的view pager 放进容器中
                supplyHeadVp.removeAllViews();
                supplyHeadVp.addView(scrollViewPager);
            }
        }
    }


    private void initDot(List<FacilityHead.DataBean> response) {
        if (llPoint != null) {
            llPoint.removeAllViews();
            mllDots.clear();
            for (int i = 0; i < response.size(); i++) {
                if (SupplyFragment.this.getActivity() != null) {
                    View view = new View(SupplyFragment.this.getActivity().getApplicationContext());

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
        }
    }


    private CustomDialog customDialog = null;

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.search_tv_supply:
                    //搜索界面
                    break;
                case R.id.iv_sup_phone:
                    //打电话
                    PermissionUtils.requestPermission(SupplyFragment.this.getActivity(),
                            PermissionUtils.CODE_CALL_PHONE, mPermissionGrant);

                    break;
                case R.id.iv_sup_wechat:
                    //微信
                    customDialog = new CustomDialog(getActivity());
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
                            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("wechat", wechat);
                            cm.setPrimaryClip(clipData);
                            ToastUtil.shortToast(getContext(), "复制成功");
                            customDialog.dismiss();
                        }
                    }, "复制");
                    customDialog.show();

                    break;
                case R.id.iv_sup_qq:
                    //qq
                    customDialog = new CustomDialog(getActivity());
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
                            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("qq", qq);
                            cm.setPrimaryClip(clipData);
                            ToastUtil.shortToast(getContext(), "复制成功");
                            customDialog.dismiss();
                        }
                    }, "复制");
                    customDialog.show();

                    break;
                case R.id.iv_sup_address:
                    //地址
                    Bundle bundle = new Bundle();
                    bundle.putDouble("longitude", longitude);
                    bundle.putDouble("latitude", latitude);
                    bundle.putString("company", companyName);

                    MarkPositionActivity.intent2MarkPosition(getActivity(), bundle);
                    break;
                case R.id.tv_supply_company:
                    //公司企业  200家
                    intent = new Intent(getActivity(), CompanyListActivity.class);
                    intent.putExtra("tag", "company");
                    startActivity(intent);
                    break;
                case R.id.tv_supply_third:
                    // 第三方
                    Intent intent1 = new Intent(getContext(), CompanyListActivity.class);
                    intent1.putExtra("tag", "thrid");
                    startActivity(intent1);
                    break;
                case R.id.rl_supply_boom:
                    //粉尘防爆
                    intent = new Intent(getContext(), FacilityCategoryActivity.class);
                    intent.putExtra("category", GlobalContanstant.BOOM);
                    startActivity(intent);
                    break;
                case R.id.rl_supply_envir:
                    intent = new Intent(getContext(), FacilityCategoryActivity.class);
                    intent.putExtra("category", GlobalContanstant.ENVIR);
                    startActivity(intent);
//                    环境保护
                    break;
                case R.id.rl_supply_fire:
                    intent = new Intent(getContext(), FacilityCategoryActivity.class);
                    intent.putExtra("category", GlobalContanstant.FIRE);
                    startActivity(intent);
//                    消防安全
                    break;
                case R.id.rl_supply_health:
                    intent = new Intent(getContext(), FacilityCategoryActivity.class);
                    intent.putExtra("category", GlobalContanstant.HEALTH);
//                    职业卫生
                    startActivity(intent);
                    break;

            }



        }
    }


}
