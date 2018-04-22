package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/4/20.
 *
 *
 */
public class SystemManageAdapter extends BaseQuickAdapter<String> {
    private int[] micons = new int[]{R.mipmap.system_boom,R.mipmap.system_control,R.mipmap.system_health,
            R.mipmap.system_envrio,R.mipmap.system_flood};

    public SystemManageAdapter(List<String> lists) {
        super(R.layout.item_more,lists);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.more_tv,item);
        int layoutPosition = helper.getLayoutPosition();
        helper.setImageResource(R.id.more_icon,micons[layoutPosition]);
    }
}
