package com.xytsz.xytaj.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2017/7/7.
 *  主页recycle
 */
public class ItemMainFragmentAdapter extends BaseQuickAdapter<String> {

    public ItemMainFragmentAdapter(List data) {
        super(R.layout.item_more, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.more_tv,item);

    }
}
