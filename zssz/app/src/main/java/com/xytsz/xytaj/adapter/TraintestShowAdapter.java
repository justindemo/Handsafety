package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.TrainContent;

import java.util.List;

/**
 * Created by admin on 2018/4/12.
 *
 * 展示
 */
public class TraintestShowAdapter extends BaseQuickAdapter<TrainContent>{

    public TraintestShowAdapter(List<TrainContent> data) {
        super(R.layout.item_trainshowcontent,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrainContent item) {

    }
}
