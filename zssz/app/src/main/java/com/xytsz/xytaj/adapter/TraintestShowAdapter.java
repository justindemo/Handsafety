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
        helper.setText(R.id.tv_train_title,item.getTitle());
        helper.setText(R.id.tv_train_adress,item.getAddress());
        helper.setText(R.id.tv_train_team,item.getDeptName());
        helper.setText(R.id.tv_train_stime,item.getBeginTime());

    }
}
