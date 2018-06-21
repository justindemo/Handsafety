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
 * <p/>
 * 产品详情页
 */
public class ProduceDetailActivity extends AppCompatActivity {

    @Bind(R.id.producedeatil_rv)
    RecyclerView producedeatilRv;
    @Bind(R.id.producedeatil_progressbar)
    LinearLayout producedeatilProgressbar;
    private String title;
    private String desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            desc = getIntent().getStringExtra("Desc");
        }
        setContentView(R.layout.activity_producedetail);
        ButterKnife.bind(this);

        if (title != null) {
            initActionbar(title);
        }
        initData();
    }

    private List<String> produceNames = new ArrayList<>();
    private List<String> produceImgs = new ArrayList<>();

    private void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        producedeatilRv.setLayoutManager(manager);
        if (desc != null && desc.contains("[img]")) {
            produceNames.clear();
            produceImgs.clear();
            //除去[img]
            desc = desc.substring(5, desc.length());
            //分割
            desc = desc.replace("[img]","|");
            String[] split = desc.split("\\|");
            if (split.length > 0) {

//                /upfile/201806/636639683536823591.png[/img]泄爆口也称为泄爆面积，
//                [br]比如房间的
//                  [img]/upfile/201806/636639683536823591.png
//                  泄爆口也称为泄爆面积，
//                [br]比如房间的
                for (int i = 0; i < split.length; i++) {
                    String str = split[i].replace("[/img]",";");
                    String[] split1 = str.split(";");

                    //如果大于2
                    if (split1.length == 2) {
                        //第一个是图片
                        produceImgs.add(split1[0]);
                        produceNames.add(split1[1]);

                    }else {
                        if (split1[0] != null && split[0].contains("png")) {
                            produceImgs.add(split1[0]);
                        }else {
                            produceNames.add(split1[0]);
                        }
                    }


                }
            }

        }


        if (produceNames.size() >= produceImgs.size()) {
            produceDetailAdapter = new ProduceDetailAdapter(produceNames, produceImgs,
                    this, true);

        } else {
            produceDetailAdapter = new ProduceDetailAdapter(produceImgs, produceNames, this, false);
        }
        producedeatilRv.setAdapter(produceDetailAdapter);


    }

    ProduceDetailAdapter produceDetailAdapter;

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
        intent.putExtra("Desc", bundle.getString("Desc"));
        intent.putExtra("title", bundle.getString("title"));
        context.startActivity(intent);

    }
}
