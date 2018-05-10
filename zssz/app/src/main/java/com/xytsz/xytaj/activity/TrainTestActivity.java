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
import com.xytsz.xytaj.adapter.TrainTestAdapter;
import com.xytsz.xytaj.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2018/3/9.
 * 培训测试
 */
public class TrainTestActivity extends AppCompatActivity {


    @Bind(R.id.traintest_rv)
    RecyclerView traintestRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traintest);
        ButterKnife.bind(this);

        initActionbar();

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),3);
        traintestRv.setLayoutManager(manager);
        List<String> list = new ArrayList<>();
        list.clear();
        list.add("实施方案");
        list.add("总结评估");
        list.add("内容资料");
        list.add("培训记录");
        list.add("培训通知");
        list.add("培训签到");
        list.add("培训照片");
        list.add("培训考试");
        list.add("成绩汇总");

        TrainTestAdapter trainTestAdapter = new TrainTestAdapter(list);
        traintestRv.setAdapter(trainTestAdapter);

        trainTestAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(TrainTestActivity.this,TrainTestShowActivity.class);
                intent.putExtra("tag",position);
                startActivity(intent);

            }
        });


    }


    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.traintest);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
