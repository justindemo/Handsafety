package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.SearchAdapter;
import com.xytsz.xytaj.ui.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/30.
 * <p/>
 * 查询路名
 */
public class SearchRoadActivity extends AppCompatActivity implements SearchView.SearchViewListener {

    /**
     * 搜索结果列表view
     */
    private ListView lvResults;
    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;


    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<String> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 8;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    private  List<String> dbData = new ArrayList<>();
    private String[] roads;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null){
            roads = getIntent().getStringArrayExtra("road");
        }
        setContentView(R.layout.activity_searchroad);


        initView();
        initData();

    }

    private void initView() {

        searchView = (SearchView) findViewById(R.id.search_layout);
        lvResults = (ListView) findViewById(R.id.search_lv);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        //searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //返回给上报界面

                Intent intent = getIntent();
                intent.putExtra("roadname",resultData.get(position));
                setResult(500,intent);
                finish();
            }
        });
    }

    private void initData() {
        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        //getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    private void getDbData() {

        dbData.clear();
        for (int i = 0; i < roads.length; i++) {
            dbData.add(roads[i]);
        }

    }

    private void getAutoCompleteData(String text) {

        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i));
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private void getResultData(String text) {

        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<>();
        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).contains(text.trim())) {
                    resultData.add(dbData.get(i));
                }
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }





    @Override
    public void onRefreshAutoComplete(String text) {
            //更新数据
        getAutoCompleteData(text);
    }

    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if (lvResults.getAdapter() == null) {
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        } else {
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }

    }

}
