package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.ContingencyplanshowAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/20.
 * 应急预案 子项
 */
public class ContingencyPlanShowActivity extends AppCompatActivity {

    @Bind(R.id.contingencyplan_rv)
    RecyclerView contingencyplanRv;

    private String title;
    private int tag;
    private List<String> lists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contingencyplan);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            tag = getIntent().getIntExtra("tag", -1);

        }
        switch (tag) {
            case 0:
                title = "粉尘防爆";
                break;
            case 1:
                title = "消防安全";
                break;
            case 2:
                title = "职业卫生";
                break;
            case 3:
                title = "环境保护";
                break;
            case 4:
                title = "防洪演练";
                break;


        }
        initActionBar(title);

        initData();


    }

    private void initData() {
        lists = new ArrayList<>();
        lists.clear();
        lists.add("应急预案");
        lists.add("应急演练");

        LinearLayoutManager manager = new LinearLayoutManager(ContingencyPlanShowActivity.this);
        contingencyplanRv.setLayoutManager(manager);
        ContingencyplanshowAdapter contingencyplanshowAdapter = new ContingencyplanshowAdapter(lists);
        contingencyplanRv.setAdapter(contingencyplanshowAdapter);
        contingencyplanshowAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ContingencyPlanShowActivity.this, InstitutionShowActivity.class);
                intent.putExtra("tag",tag);
                //代表展示的预案 或者演练
                intent.putExtra("tagdetail",position);
                startActivity(intent);
            }
        });

    }

    private void initActionBar(String title) {
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


}
