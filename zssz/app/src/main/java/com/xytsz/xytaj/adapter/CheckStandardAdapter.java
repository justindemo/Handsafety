package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/9/29.
 * </p>
 */
public class CheckStandardAdapter extends BaseQuickAdapter<String> {

    private int[] mimages = new int[]{R.mipmap.check_install, R.mipmap.check_operate,
            R.mipmap.check_vindicate, R.mipmap.check_partol};

    public CheckStandardAdapter(List<String> data) {
        super(R.layout.item_general_horizontal,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_patrol,item);
        helper.setImageResource(R.id.iv_patrol,mimages[helper.getLayoutPosition()]);
    }
}
