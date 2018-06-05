package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/5/29.
 * s
 */
public class FacilityCategroyAdapter extends BaseQuickAdapter<String> {
    private List<String> data;

    public FacilityCategroyAdapter(List<String> data) {
        super(R.layout.item_facility_list,data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_facilitycop_title,data.get(layoutPosition-1));
    }
}
