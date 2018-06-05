package com.xytsz.xytaj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.ProduceDetailAdapter;
import com.xytsz.xytaj.adapter.ProduceListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/5/31.
 *
 *
 */
public class ProduceListActivity extends AppCompatActivity {

    @Bind(R.id.producelist_rv)
    RecyclerView producelistRv;
    @Bind(R.id.producedeatil_progressbar)
    LinearLayout producedeatilProgressbar;
    private String tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
        }
        setContentView(R.layout.activity_producelist);
        ButterKnife.bind(this);

        if (tag != null) {
            initActionbar(tag);
        }
        initData();
    }


    private List<String> produceNames = new ArrayList<>();

    private void initData() {
        produceNames.add("灭火器标题");
        produceNames.add("消防栓标题");
        produceNames.add("消防器材标题");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        producelistRv.setLayoutManager(manager);
        ProduceListAdapter produceListAdapter = new ProduceListAdapter(produceNames);
        producelistRv.setAdapter(produceListAdapter);
        produceListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("title",produceNames.get(position));
                ProduceDetailActivity.intent2Produce(ProduceListActivity.this,bundle);
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

    public static void intent2Activity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ProduceListActivity.class);
        intent.putExtra("tag", bundle.getString("tag"));
        context.startActivity(intent);
    }
}
