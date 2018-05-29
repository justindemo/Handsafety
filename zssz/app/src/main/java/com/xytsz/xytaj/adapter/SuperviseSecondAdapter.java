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
    private int[] mIcons = {R.mipmap.sup_traintest ,R.mipmap.sup_contingencyplan,R.mipmap.sup_nocheck,
    R.mipmap.sup_meeting};
    private int number;
    private int nocheckNumber;
    private int meetingNumber;

    public SuperviseSecondAdapter( List<String> data,int number,int nocheckNumber,int meetingNumber) {
        super(R.layout.item_more, data);
        this.number = number;
        this.nocheckNumber = nocheckNumber;
        this.meetingNumber= meetingNumber;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.more_tv,item);
        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition == 0){
            if (number != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,number+"");
            }

        }
        if (layoutPosition == 2){
            if (nocheckNumber != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,nocheckNumber+"");
            }
        }
        if (layoutPosition == 3){
            if (meetingNumber != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,meetingNumber+"");
            }
        }
        helper.setImageResource(R.id.more_icon,mIcons[layoutPosition]);
    }
}
