package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.ArrayList;

/**
 * Created by admin on 2017/6/30.
 *
 *
 */
public class RoadNumberAdapter extends BaseQuickAdapter<String> {
    public RoadNumberAdapter(ArrayList<String> data) {

        super(R.layout.item_roadnumber,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_roadname,item);


    }
}
