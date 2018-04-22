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
import com.xytsz.xytaj.adapter.TrainInfoAdapter;
import com.xytsz.xytaj.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/4/12.
 * 培训通知
 *
 */
public class TrainInfoActivity extends AppCompatActivity {

    @Bind(R.id.traininfo_rv)
    RecyclerView traininfoRv;

    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traininfo);
        ButterKnife.bind(this);


        initactionbar();

        initData();
    }

    private void initData() {
        titles.clear();
        titles.add("培训通知");
        titles.add("实施方案");
        titles.add("总结评估");
        titles.add("内容资料");
        titles.add("培训记录");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);

        traininfoRv.setLayoutManager(gridLayoutManager);

        TrainInfoAdapter trainInfoAdapter = new TrainInfoAdapter(titles);
        traininfoRv.setAdapter(trainInfoAdapter);
        trainInfoAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TrainInfoActivity.this,TrainDemoActivity.class);
                intent.putExtra("title",titles.get(position));
                intent.putExtra("tag",position);
                startActivity(intent);
            }
        });
    }

    private void initactionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("培训通知");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
