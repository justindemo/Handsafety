package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.PersonSignAdapter;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/3/1.
 * 防汛
 */
public class PersonSignActivity extends AppCompatActivity {
    @Bind(R.id.personsign_rv)
    RecyclerView personsignRv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personsign);
        ButterKnife.bind(this);
        initActionbar();

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),2);
        personsignRv.setLayoutManager(manager);
        List<String> list = new ArrayList<>();
        list.clear();
        list.add("早会签到");
        list.add("培训签到");

        PersonSignAdapter personSignAdapter = new PersonSignAdapter(list);
        personsignRv.setAdapter(personSignAdapter);

        personSignAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PersonSignActivity.this,MoringSignActivity.class);
                if (position == 1){
                    intent.putExtra("tag","moreSign");
                }else {
                    intent.putExtra("tag","trainsign");
                }
                startActivity(intent);
            }
        });



    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.personsign);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
