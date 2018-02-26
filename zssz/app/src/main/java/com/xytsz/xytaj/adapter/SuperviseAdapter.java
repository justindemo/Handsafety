package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2017/6/29.
 * 监管adapter
 *
 */
public class SuperviseAdapter extends BaseQuickAdapter<String> {

    private int[] mIcons = {R.mipmap.more_problem,R.mipmap.more_caroil,R.mipmap.more_fica,R.mipmap.more_person };
    private int role;

    public SuperviseAdapter(List<String> data , int role) {
        super(R.layout.item_more, data);
        this.role = role;

    }



    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.more_tv,item);
        int layoutPosition = viewHolder.getLayoutPosition();
        viewHolder.setImageResource(R.id.more_icon,mIcons[layoutPosition-1]);

    }
}
