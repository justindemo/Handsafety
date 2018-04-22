package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/4/12.
 *
 * 第二层
 *
 */
public class SuperviseSecondAdapter extends BaseQuickAdapter<String> {
    private int[] mIcons = {R.mipmap.sup_traintest ,R.mipmap.sup_contingencyplan};

    public SuperviseSecondAdapter( List<String> data,int role) {
        super(R.layout.item_more, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.more_tv,item);
        int layoutPosition = helper.getLayoutPosition();
        helper.setImageResource(R.id.more_icon,mIcons[layoutPosition]);
    }
}
