package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.CheckStandardAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/9/29.
 * </p>
 */
public class CheckStandardActivity extends AppCompatActivity {

    @Bind(R.id.checkstandard_rv)
    RecyclerView checkstandardRv;
    private String name;
    private int deviceID;
    private List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null){
            name = getIntent().getStringExtra("name");
            deviceID = getIntent().getIntExtra("DeviceID",-1);
        }
        setContentView(R.layout.activity_checkstandard);
        ButterKnife.bind(this);
        initAcitionbar();
        initData();
    }

    private void initData() {
        names.clear();

        names.add("设备安装标准");
        names.add("设备操作标准");
        names.add("设备维护标准");
        names.add("设备隐患排查标准");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        checkstandardRv.setLayoutManager(linearLayoutManager);

        CheckStandardAdapter checkStandardAdapter = new CheckStandardAdapter(names);
        checkstandardRv.setAdapter(checkStandardAdapter);

        checkStandardAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CheckStandardActivity.this, CheckStandardDetailActivity.class);
                intent.putExtra("name",names.get(position));
                intent.putExtra("type",position+1);
                intent.putExtra("DeviceID",deviceID);
                startActivity(intent);
            }
        });

    }
    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(name);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
