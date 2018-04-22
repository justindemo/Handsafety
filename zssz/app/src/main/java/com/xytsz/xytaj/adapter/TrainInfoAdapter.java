package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/4/12.
 *
 * 通知列别
 */
public class TrainInfoAdapter extends BaseQuickAdapter<String> {
    private int[] mIcons = {R.mipmap.train_info,R.mipmap.train_scheme,R.mipmap.train_evaluation,R.mipmap.train_material,R.mipmap.train_record};
    public TrainInfoAdapter(List<String> data) {
        super(R.layout.item_more, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.more_tv,item);
        int layoutPosition = helper.getLayoutPosition();
        helper.setImageResource(R.id.more_icon,mIcons[layoutPosition]);
    }
}
