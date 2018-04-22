package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/3/9.
 *
 */

public class TrainTestAdapter extends BaseQuickAdapter<String>{

    private int[] micons = new int[]{R.mipmap.train_scheme,R.mipmap.train_evaluation,R.mipmap.train_material,
            R.mipmap.train_record,R.mipmap.train_info,R.mipmap.train_sign,R.mipmap.train_photo,
            R.mipmap.train_test,R.mipmap.train_collect};

    public TrainTestAdapter(List<String> data) {
        super(R.layout.item_more,data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.more_tv,item);
        int layoutPosition = viewHolder.getLayoutPosition();
        viewHolder.setImageResource(R.id.more_icon,micons[layoutPosition]);
    }
}
