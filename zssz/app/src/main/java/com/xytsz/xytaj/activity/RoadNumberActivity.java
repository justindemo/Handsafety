package com.xytsz.xytaj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.RoadNumberAdapter;
import com.xytsz.xytaj.util.IntentUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/6/29.
 *  监管道路台账
 *
 */
public class RoadNumberActivity extends AppCompatActivity {

    @Bind(R.id.road_rv)
    RecyclerView roadRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadnumber);
        ButterKnife.bind(this);

        LinearLayoutManager manager =  new LinearLayoutManager(this);
        roadRv.setLayoutManager(manager);
        initData();
    }


    private ArrayList<String> list = new ArrayList<>();
    private void initData() {
        list.clear();
        for (int i = 0; i < 30; i++) {
            list.add("金星路"+i);
        }
        RoadNumberAdapter adapter = new RoadNumberAdapter(list);
        roadRv.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtil.startActivity(RoadNumberActivity.this,RoadMapActivity.class);
            }
        });
    }
}
