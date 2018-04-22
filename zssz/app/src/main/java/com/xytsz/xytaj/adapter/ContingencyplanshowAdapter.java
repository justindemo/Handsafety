package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/4/20.
 * 应急预案子项
 */
public class ContingencyplanshowAdapter extends BaseQuickAdapter<String> {
    public ContingencyplanshowAdapter(List<String> data) {
        super(R.layout.item_text,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_text,item);
    }
}
