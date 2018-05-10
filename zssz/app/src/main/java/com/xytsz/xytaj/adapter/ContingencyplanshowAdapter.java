package com.xytsz.xytaj.adapter;

import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2018/4/20.
 * 应急预案子项
 */
public class ContingencyplanshowAdapter extends BaseQuickAdapter<String> {
    private int[] micons = new int[]{R.mipmap.sup_contingency,R.mipmap.sup_contingency_play};
    public ContingencyplanshowAdapter(List<String> data) {
        super(R.layout.item_text,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_text,item);

        int layoutPosition = helper.getLayoutPosition();
        helper.setImageResource(R.id.item_iv,micons[layoutPosition]);
    }
}
