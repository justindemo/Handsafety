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

    private boolean isVisiable;

    public PatrolListAdapter(List<PatrolListBean> data, boolean isVisiable) {
        super(R.layout.item_patrollist, data);
        this.isVisiable = isVisiable;
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolListBean item) {

        //添加检查人1 和二
        helper.setText(R.id.tv_facility_name,item.getDeviceName());
        helper.setText(R.id.tv_facility_code,item.getDeviceNum());
        helper.setText(R.id.tv_facility_location,item.getAddressInfo());
        helper.setText(R.id.tv_facility_team,item.getDept_Name());
        helper.setText(R.id.tv_facility_state,item.getState());
        helper.setVisible(R.id.lv_check_title,isVisiable);
        //如果是false，不显示  true  显示，替换控件。
        if (isVisiable) {
            helper.setText(R.id.tv_facility_checkperson1, item.getCheckPersonName());
            helper.setText(R.id.tv_facility_checkperson2, item.getCheckPersonName2());
        }else {
            helper.setText(R.id.tv_facility_person1, item.getCheckPersonName());
            helper.setText(R.id.tv_facility_person2, item.getCheckPersonName2());
        }

    }
}
