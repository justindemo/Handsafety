package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.PatrolListBean;

import java.util.List;

/**
 * Created by admin on 2018/3/26.
 *
 * 每日任务的适配器
 */
public class PatrolListAdapter extends BaseQuickAdapter<PatrolListBean> {

    public PatrolListAdapter(List<PatrolListBean> data) {
        super(R.layout.item_patrollist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolListBean item) {
        helper.setText(R.id.tv_facility_name,item.getDeviceName());
        helper.setText(R.id.tv_facility_code,item.getDeviceNum());
        helper.setText(R.id.tv_facility_location,item.getAddressInfo());
        helper.setText(R.id.tv_facility_team,item.getDept_Name());
        helper.setText(R.id.tv_facility_state,item.getState());
    }
}
