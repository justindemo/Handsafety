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

    private int[] mIcons = {R.mipmap.sup_moring,R.mipmap.sup_mytask,R.mipmap.train_insitution};

    private String number;
    public SuperviseAdapter(List<String> data ) {
        super(R.layout.item_more, data);

    }

    public void setNumber(String number){
        this.number = number;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.more_tv,item);

        int layoutPosition = viewHolder.getLayoutPosition();
        if (layoutPosition == 1){
            if (number != null && !number.equals("0")){
                viewHolder.setVisible(R.id.tv_mytask_number,true);
                viewHolder.setText(R.id.tv_mytask_number,number);
            }else {
                viewHolder.setVisible(R.id.tv_mytask_number,false);
            }

        }
        viewHolder.setImageResource(R.id.more_icon,mIcons[layoutPosition]);

    }
}
