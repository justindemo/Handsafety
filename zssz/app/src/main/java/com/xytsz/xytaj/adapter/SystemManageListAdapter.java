package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.SytemManageList;

import java.util.List;

/**
 * Created by admin on 2018/5/9.
 *
 *
 */
public class SystemManageListAdapter extends BaseQuickAdapter<SytemManageList>{
    public SystemManageListAdapter(List<SytemManageList> manageLists) {
        super(R.layout.item_systemangelist,manageLists);
    }

    @Override
    protected void convert(BaseViewHolder helper, SytemManageList item) {
        helper.setText(R.id.tv_manage_name,item.getTitle());
        helper.setText(R.id.tv_manage_team,item.getDeptName());

    }
}
