package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/3/2.
 *
 */
public class PersonSignAdapter extends BaseQuickAdapter<String>{


    private int[] micons = new int[]{R.mipmap.morningsign,R.mipmap.trainsign};
    public PersonSignAdapter(List<String> data) {
        super(R.layout.item_more, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.more_tv,item);
        int layoutPosition = viewHolder.getLayoutPosition();
        viewHolder.setImageResource(R.id.more_icon,micons[layoutPosition]);
    }
}
