package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.Company;

import java.util.List;

/**
 * Created by admin on 2018/5/28.
 *
 * 供应链首页的公司
 */
public class SupplyCompanyAdapter extends BaseQuickAdapter<String> {
    private Context context;

    private int[] mIcons = {R.mipmap.test1,R.mipmap.test2,R.mipmap.test3,R.mipmap.test1
            ,R.mipmap.test2,R.mipmap.test3};


    public SupplyCompanyAdapter(List<String> data, Context context) {
        super(R.layout.item_supplycompany,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.supply_tv,item.getCompanyName());
        helper.setText(R.id.supply_tv,item);
//        ImageView imageview = helper.getView(R.id.supply_icon);
//        Glide.with(context).load(item.getCompanyImgUrl()).into(imageview);
        int position = helper.getAdapterPosition();

        helper.setImageResource(R.id.supply_icon,mIcons[position]);
    }
}
