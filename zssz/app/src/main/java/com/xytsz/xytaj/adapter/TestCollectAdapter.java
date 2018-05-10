package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.TestCollect;

import java.util.List;

/**
 * Created by admin on 2018/4/23.
 * 成绩汇总 适配器
 */
public class TestCollectAdapter extends BaseQuickAdapter<TestCollect> {
    private List<TestCollect> data;

    public TestCollectAdapter(List<TestCollect> data) {
        super(R.layout.item_testcollect,data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, TestCollect item) {
        int layoutPosition = helper.getLayoutPosition();

        helper.setText(R.id.tv_testcollect_name,data.get(layoutPosition-1).getUserName());
        helper.setText(R.id.tv_testcollect_dep,data.get(layoutPosition-1).getUserName());
        helper.setText(R.id.tv_testcollect_score,data.get(layoutPosition-1).getSumMark()+"");
        helper.setText(R.id.tv_testcollect_state,data.get(layoutPosition-1).getIson());


    }
}
