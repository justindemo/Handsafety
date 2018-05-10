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
 * Created by admin on 2018/5/9.
 * <p/>
 * 会议纪要
 */
public class SupMeetingActivity extends AppCompatActivity {

    @Bind(R.id.meeting_rv)
    RecyclerView meetingRv;
    private List<String> lists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supmeeting);
        ButterKnife.bind(this);

        initActionBar();
        initData();
        initView();
    }

    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
        meetingRv.setLayoutManager(manager);

        SystemManageAdapter systemManageAdapter = new SystemManageAdapter(lists,true);
        meetingRv.setAdapter(systemManageAdapter);
        systemManageAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SupMeetingActivity.this, SupMeetingListActivity.class);
                intent.putExtra("tag", position);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        lists = new ArrayList<>();
        lists.clear();
        lists.add("会议内容");
        lists.add("会议签到");
        lists.add("会议照片");


    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.supmeeting);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
