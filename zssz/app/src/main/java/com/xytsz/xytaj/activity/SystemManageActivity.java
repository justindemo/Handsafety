package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SystemManageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/20.
 * 制度管理
 */
public class SystemManageActivity extends AppCompatActivity {

    @Bind(R.id.system_rv)
    RecyclerView systemRv;

    private List<String> lists;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmanage);
        ButterKnife.bind(this);

        initActionBar();
        initData();
        initView();

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.systemmanage);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),3);
        systemRv.setLayoutManager(manager);

        SystemManageAdapter systemManageAdapter = new SystemManageAdapter(lists,false);
        systemRv.setAdapter(systemManageAdapter);
        systemManageAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SystemManageActivity.this,SystemManageListActivity.class);
                intent.putExtra("tag",position);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        lists = new ArrayList<>();
        lists.clear();
        lists.add("职业卫生");
        lists.add("粉尘防爆");
        lists.add("消防安全");
        lists.add("环境保护");
        lists.add("安全责任书");
        lists.add("法律法规");

    }
}
