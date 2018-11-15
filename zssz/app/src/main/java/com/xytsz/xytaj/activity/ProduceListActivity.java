package com.xytsz.xytaj.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.CaseListAdapter;
import com.xytsz.xytaj.adapter.ProduceListAdapter;
import com.xytsz.xytaj.bean.CompanyCase;
import com.xytsz.xytaj.bean.CompanyCaseCallback;
import com.xytsz.xytaj.bean.CompanyProduce;
import com.xytsz.xytaj.bean.CompanyProduceCallback;
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
 * Created by admin on 2018/5/31.
 * 产品 案例列表
 */
public class ProduceListActivity extends AppCompatActivity {

    @Bind(R.id.producelist_rv)
    RecyclerView producelistRv;
    @Bind(R.id.producelist_refreshlayout)
    SmartRefreshLayout producelistRefreshlayout;

    private String tag;


    private ProduceListAdapter produceListAdapter;
    private List<CompanyProduce.Produce> data;
    private int companyId;
    private String url;
    private List<CompanyCase.DataBean> casedata;

    private List<CompanyProduce.Produce> data1= new ArrayList<>();
    private List<CompanyCase.DataBean> data2= new ArrayList<>();
    private int personId;
    private int datacount;
    private int pageSize = 8;
    private int caseCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            companyId = getIntent().getIntExtra("ID", -1);
        }
        setContentView(R.layout.activity_producelist);
        ButterKnife.bind(this);
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        if (tag != null) {

            initActionbar(tag);
            initData();
        }
    }


    private void initData() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        producelistRv.setLayoutManager(manager);

        switch (tag) {
            case "产品服务":
                url = NetUrl.SERVERURL2 + NetUrl.getProduceList;
                getproduceList(1);
                producelistRefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshLayout) {
                        producelistRefreshlayout.finishLoadMore(2000);
                        if (datacount != 0){
                            int pagecount = datacount / pageSize;
                            int max = datacount % pageSize;
                            if (max >0) {
                                ++pagecount;
                            }
                            if (pageNo < pagecount) {
                                ++pageNo;
                                getproduceList(1);
                            }else {
                                ToastUtil.shortToast(getApplicationContext(),"没有更多了");
                            }
                        }



                    }
                });

                break;
            case "成功案例":
                url = NetUrl.SERVERURL2 + NetUrl.getCaseList;
                getproduceList(2);
                producelistRefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshLayout) {
                        producelistRefreshlayout.finishLoadMore(2000);
                        if (caseCount != 0){
                            int pagecount = caseCount / pageSize;
                            int max = caseCount % pageSize;
                            if (max >0) {
                                ++pagecount;
                            }
                            if (pageNo < pagecount) {
                                ++pageNo;
                                getproduceList(2);
                            }else {
                                ToastUtil.shortToast(getApplicationContext(),"没有更多了");
                            }
                        }



                    }
                });

                break;
        }



    }

    private int pageNo = 1;
    private Map<String, Integer> params = new HashMap<>();
    private  CaseListAdapter caseListAdapter;
    private void getproduceList(final int categroy) {

        params.clear();
        params.put("pageSize",pageSize);
        params.put("pageNo", pageNo);
        params.put("companyID", companyId);
        params.put("AJPersonID", personId);

        String spliceGetUrl = UrlUtils.spliceGetUrl(url, params);

        if (categroy == 1) {
            OkHttpUtils
                    .get()
                    .url(spliceGetUrl)
                    .build()
                    .execute(new CompanyProduceCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            producelistRefreshlayout.finishLoadMore(false);
                            ToastUtil.shortToast(getApplicationContext(), "未加载");
                        }

                        @Override
                        public void onResponse(CompanyProduce response, int id) {

                            if (response != null) {
                                data = response.getData();
                                datacount = response.getDataCount();
                                if (data.size() == 0) {
                                    if (pageNo == 1) {
                                        ToastUtil.shortToast(getApplicationContext(), "noting");
                                    }
                                } else {
                                    data1.addAll(data);
                                    if (pageNo == 1) {
                                        produceListAdapter = new ProduceListAdapter(data, ProduceListActivity.this);
                                        producelistRv.setAdapter(produceListAdapter);
                                    } else {
                                        produceListAdapter.addData(data);
                                    }

                                    produceListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Desc", data1.get(position).getProductDesc());
                                            bundle.putString("title", data1.get(position).getProductName());
                                            ProduceDetailActivity.intent2Produce(ProduceListActivity.this, bundle);
                                        }
                                    });

                                }


                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "response =null");
                            }
                        }
                    });
        }else if (categroy == 2){
            OkHttpUtils
                    .get()
                    .url(spliceGetUrl)
                    .build()
                    .execute(new CompanyCaseCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            producelistRefreshlayout.finishLoadMore(2000);
                            ToastUtil.shortToast(getApplicationContext(), "未加载");
                        }

                        @Override
                        public void onResponse(CompanyCase response, int id) {

                            if (response != null) {
                                casedata = response.getData();
                                caseCount = response.getDataCount();
                                if (casedata.size() == 0) {
                                    if (pageNo == 1) {
                                        ToastUtil.shortToast(getApplicationContext(), "noting");
                                    }
                                } else {

                                    data2.addAll(casedata);
                                    if (pageNo == 1) {
                                        caseListAdapter = new CaseListAdapter(casedata, ProduceListActivity.this);
                                        producelistRv.setAdapter(caseListAdapter);

                                    } else {
                                        caseListAdapter.addData(casedata);
                                    }


                                    caseListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Desc", data2.get(position).getCaseDesc());
                                            bundle.putString("title", data2.get(position).getCaseTitle());
                                            ProduceDetailActivity.intent2Produce(ProduceListActivity.this, bundle);
                                        }
                                    });

                                }


                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "response =null");
                            }
                        }
                    });
        }
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

    public static void intent2Activity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ProduceListActivity.class);
        intent.putExtra("tag", bundle.getString("tag"));
        intent.putExtra("ID", bundle.getInt("ID"));
        context.startActivity(intent);
    }
}
