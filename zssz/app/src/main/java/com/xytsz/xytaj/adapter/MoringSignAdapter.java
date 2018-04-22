package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.MoringMeeting;

import java.util.List;

/**
 * Created by admin on 2018/4/21.
 *
 *
 */
public class MoringSignAdapter extends BaseQuickAdapter<MoringMeeting> {
    public MoringSignAdapter(List<MoringMeeting> data) {
        super(R.layout.item_moringsign,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoringMeeting item) {
        helper.setText(R.id.tv_moringsign_team,item.getDeptName());
        helper.setText(R.id.tv_moringsign_stime,item.getBeginTime());
        helper.setText(R.id.tv_moringsign_etime,item.getEndTime());
        helper.setText(R.id.tv_moringsign_number,item.getCount()+"");
        helper.setText(R.id.tv_moringsign_state,item.getState());
        helper.setText(R.id.tv_moringsign_totalnumber,item.getSumCount()+"");
    }
}
