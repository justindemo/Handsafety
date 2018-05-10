package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/5/4.
 *
 *
 *
 */
public class MoringsignedAdapter extends BaseQuickAdapter<String> {
    public MoringsignedAdapter(List<String> personList) {
        super(R.layout.item_moringsigned,personList);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_moringsigneder,item);
    }
}
