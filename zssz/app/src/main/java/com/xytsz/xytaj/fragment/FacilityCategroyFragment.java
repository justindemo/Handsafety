package com.xytsz.xytaj.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.MemberCompanyShowActivity;
import com.xytsz.xytaj.adapter.CategroyCompanyAdapter;
import com.xytsz.xytaj.adapter.FacilityCategroyAdapter;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.bean.FacilityCategroyCompany;
import com.xytsz.xytaj.bean.FacilityCategroyCompanyCallback;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.net.UrlUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by admin on 2018/5/28.
 * <p/>
 * 产品分类页
 */
public class FacilityCategroyFragment extends BaseFragment {


    private RecyclerView facilityCategroyRv;
    private SmartRefreshLayout facilityCategroyRefreshlayout;


    private static final String ARG_PARAM1 = "param1";
    private int id;
    private CategroyCompanyAdapter categroyCompanyAdapter;
    private List<FacilityCategroyCompany.CompanyList> topdata;
    private List<FacilityCategroyCompany.CompanyList> alldata = new ArrayList<>();
    private int personId;

    public FacilityCategroyFragment() {

    }

    public static FacilityCategroyFragment newInstance(int id) {
        FacilityCategroyFragment fragment = new FacilityCategroyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, id);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_facilitycategroy, null);
        facilityCategroyRefreshlayout = (SmartRefreshLayout) view.findViewById(R.id.facility_categroy_refreshlayout);
        facilityCategroyRv = (RecyclerView) view.findViewById(R.id.facility_categroy_rv);
        return view;
    }


    @Override
    public void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        facilityCategroyRv.setLayoutManager(manager);
        personId = SpUtils.getInt(getContext(), GlobalContanstant.PERSONID);
        getTopData();
        getrandomData();
        facilityCategroyRefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                ++pageNo;
                getrandomData();
            }
        });

    }

    private int pageNo = 1;

    private void getrandomData() {
        String url = NetUrl.SERVERURL2 + NetUrl.getFacilityCategroyData;
        params.clear();
        params.put("pageSize", 6);
        params.put("pageNo", pageNo);
        params.put("productClass", id);
        params.put("AJPersonID", personId);
        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils.get().url(spliceGetUrl).build().
                execute(new FacilityCategroyCompanyCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        facilityCategroyRefreshlayout.finishLoadMore(false);
                        ToastUtil.shortToast(getContext(), "nothing");
                    }

                    @Override
                    public void onResponse(FacilityCategroyCompany response, int id) {
                        facilityCategroyRefreshlayout.finishLoadMore(2000);
                        if (response != null) {
                            List<FacilityCategroyCompany.CompanyList> data = response.getData();
                            if (data != null && data.size() != 0) {
                                alldata.addAll(data);
                            } else {
                                return;
                            }
                            if (data.size() == 0) {
                                ToastUtil.shortToast(getContext(), "moredata = null");
                            } else {
                                if (pageNo == 1) {
                                    categroyCompanyAdapter = new CategroyCompanyAdapter(alldata, FacilityCategroyFragment.this.getContext());
                                    facilityCategroyRv.setAdapter(categroyCompanyAdapter);
                                    LayoutInflater inflater = FacilityCategroyFragment.this.getActivity().getLayoutInflater();
                                    View headView = inflater.inflate(R.layout.pop_tv, facilityCategroyRv, false);
                                    categroyCompanyAdapter.addHeaderView(headView);

                                } else {
                                    categroyCompanyAdapter.addData(data);
                                }

                                categroyCompanyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(FacilityCategroyFragment.this.getActivity(), MemberCompanyShowActivity.class);
                                        //传递Id；
                                        intent.putExtra("companyName", alldata.get(position).getCompanyName());
                                        intent.putExtra("companyID", alldata.get(position).getID());
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            ToastUtil.shortToast(getContext(), "response =null");
                        }
                    }

                });
    }

    private Map<String, Integer> params = new HashMap<>();

    private void getTopData() {
        //服务器获取数据  通过iD获取数据
        String url = NetUrl.SERVERURL2 + NetUrl.getFacilityCategroyData;
        params.clear();
        params.put("pageSize", 3);
        params.put("pageNo", 1);
        params.put("productClass", id);
        params.put("topIndex", GlobalContanstant.REVIEW);
        params.put("AJPersonID", personId);
        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils.get().url(spliceGetUrl).build().
                execute(new FacilityCategroyCompanyCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.shortToast(getContext(), "nothing");
                    }

                    @Override
                    public void onResponse(FacilityCategroyCompany response, int id) {
                        if (response != null) {
                            topdata = response.getData();
                            if (topdata != null && topdata.size() != 0) {
//                                ToastUtil.shortToast(getContext(), "top = ");
                                alldata.addAll(topdata);
                            }
                        }
                    }
                });
    }


}
