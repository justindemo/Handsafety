package com.xytsz.xytaj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.ProduceDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/31.
 *
 * 产品详情页
 */
public class ProduceDetailActivity extends AppCompatActivity {

    @Bind(R.id.producedeatil_rv)
    RecyclerView producedeatilRv;
    @Bind(R.id.producedeatil_progressbar)
    LinearLayout producedeatilProgressbar;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
        }
        setContentView(R.layout.activity_producedetail);
        ButterKnife.bind(this);

        if (title != null) {
            initActionbar(title);
        }
        initData();
    }

    private List<String> produceNames = new ArrayList<>();

    private void initData() {
        produceNames.add("安全风扇防护罩安全罩安全网宝宝安全工业风扇");
        produceNames.add("描述描述");
        produceNames.add("描述描述");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        producedeatilRv.setLayoutManager(manager);
        ProduceDetailAdapter produceDetailAdapter = new ProduceDetailAdapter(produceNames);
        producedeatilRv.setAdapter(produceDetailAdapter);


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

    public static void intent2Produce(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ProduceDetailActivity.class);
        intent.putExtra("title", bundle.getString("title"));
        context.startActivity(intent);

    }
}
