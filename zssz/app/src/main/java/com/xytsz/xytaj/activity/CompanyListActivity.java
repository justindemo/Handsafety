package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.FacilityCategroyAdapter;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.bean.CompanyListCallback;
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
 * Created by admin on 2018/5/30.
 * 公司列表
 */
public class CompanyListActivity extends AppCompatActivity {

    @Bind(R.id.companylist_rv)
    RecyclerView companylistRv;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String tag;

    private int companycategroy;
    private List<CompanyList.DataBean> data;
    private List<CompanyList.DataBean> allData = new ArrayList<>();
    private int personId;
    private int dataCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
        }
        setContentView(R.layout.activity_companylist);
        ButterKnife.bind(this);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        if (tag != null) {
            switch (tag) {
                case "company":
                    title = "企业会员";
                    companycategroy = 1;
                    break;
                case "thrid":
                    title = "第三方服务";
                    companycategroy = 2;
                    break;
            }
        }
        initActionBar();
        initView();

    }


    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        companylistRv.setLayoutManager(manager);
        getCompanyList();

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                if (dataCount != 0) {
                    int pagecount = dataCount / pageSize;
                    int max = dataCount % pageSize;
                    if (max >0) {
                        ++pagecount;
                    }
                    if (pageNo < pagecount) {
                        ++pageNo;
                        getCompanyList();
                    }else {
                        ToastUtil.shortToast(getApplicationContext(),"没有更多了");
                    }

                }
            }
        });

    }

    private Map<String, Integer> params = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 8;
    private FacilityCategroyAdapter facilityCategroyAdapter;


    private void getCompanyList() {
        String url = NetUrl.SERVERURL2 + NetUrl.getCompanyData;
        params.clear();
        params.put("pageSize",pageSize);
        params.put("pageNo", pageNo);
        params.put("companyClass", companycategroy);
        params.put("topIndex", -1);

        params.put("AJPersonID", personId);
        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);
        OkHttpUtils
                .get()
                .url(spliceGetUrl)
                .build()
                .execute(new CompanyListCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        refreshLayout.finishLoadMore(false);
                        ToastUtil.shortToast(getApplicationContext(), "未加载");
                    }

                    @Override
                    public void onResponse(CompanyList response, int id) {
                        if (response != null) {
                            data = response.getData();
                            dataCount = response.getDataCount();
                            if (data.size() == 0 && pageNo == 1){
                                ToastUtil.shortToast(getApplicationContext(),"没有入驻");
                            }else {
                                allData.addAll(data);
                                if (pageNo == 1) {
                                    facilityCategroyAdapter = new FacilityCategroyAdapter(data, CompanyListActivity.this);
                                    companylistRv.setAdapter(facilityCategroyAdapter);
                                    LayoutInflater inflater = CompanyListActivity.this.getLayoutInflater();
                                    View headView = inflater.inflate(R.layout.pop_tv, companylistRv, false);
                                    facilityCategroyAdapter.addHeaderView(headView);

                                }else {
                                    facilityCategroyAdapter.addData(data);
                                }

                                facilityCategroyAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(CompanyListActivity.this, MemberCompanyShowActivity.class);
                                        //传递Id；
                                        intent.putExtra("companyName", allData.get(position).getCompanyName());
//                                        intent.putExtra("companyDetail", allData.get(position));
                                        intent.putExtra("companyID", allData.get(position).getID());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }else {
                            ToastUtil.shortToast(getApplicationContext(),"response =null");
                        }
                    }
                });
    }

    private String title;

    private void initActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
