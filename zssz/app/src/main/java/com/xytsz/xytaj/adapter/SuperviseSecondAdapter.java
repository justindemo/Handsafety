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
    private int[] mIcons = {R.mipmap.sup_traintest ,R.mipmap.sup_contingencyplan,R.mipmap.sup_nopatrol,
    R.mipmap.sup_meeting,R.mipmap.sup_nocheck};
    private int number;
    private int nocheckNumber;
    private int nopatorlNumber;
    private int meetingNumber;
    private List<String> data;

    public SuperviseSecondAdapter( List<String> data) {
        super(R.layout.item_more, data);

        this.data = data;
    }

    public void setNumber(List<Integer> numbers){
        if (numbers != null&& numbers.size() != 0 && numbers.size() ==3){
            this.number = numbers.get(0);
            this.nopatorlNumber = numbers.get(1);
            this.meetingNumber= numbers.get(2);
//            this.nocheckNumber= numbers.get(3);
        }

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
            if (nopatorlNumber != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,nopatorlNumber+"");
            }
        }
        if (layoutPosition == 3){
            if (meetingNumber != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,meetingNumber+"");
            }
        }
        if (layoutPosition == 4){
            if (nocheckNumber != 0){
                helper.setVisible(R.id.tv_mytask_number,true);
                helper.setText(R.id.tv_mytask_number,nocheckNumber+"");
            }
        }
        helper.setImageResource(R.id.more_icon,mIcons[layoutPosition]);
    }
}
