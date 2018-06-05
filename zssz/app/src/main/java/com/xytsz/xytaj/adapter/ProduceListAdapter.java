package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/5/31.
 *
 *
 */
public class ProduceListAdapter extends BaseQuickAdapter<String> {
    public ProduceListAdapter(List<String> produceNames) {
        super(R.layout.item_producelist,produceNames);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_produce_title,item);
    }
}
