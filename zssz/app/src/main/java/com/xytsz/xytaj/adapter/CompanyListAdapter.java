package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.CompanyList;

import java.util.List;

/**
 * Created by admin on 2018/5/30.
 *
 * 列表页
 */
public class CompanyListAdapter extends BaseQuickAdapter<CompanyList> {
    public CompanyListAdapter(List<CompanyList> data) {
        super(R.layout.item_facility_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyList item) {

    }
}
