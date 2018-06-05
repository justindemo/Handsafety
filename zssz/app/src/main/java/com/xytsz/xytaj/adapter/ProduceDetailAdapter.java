package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/5/31.
 *
 * 产品详细页
 */
public class ProduceDetailAdapter extends BaseQuickAdapter<String> {


    public ProduceDetailAdapter(List<String> data) {
        super(R.layout.item_producedetail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_producedetail,item);

    }
}
