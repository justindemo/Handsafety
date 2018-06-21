package com.xytsz.xytaj.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.bean.CompanyCase;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.RoundImageView;

import java.util.List;

/**
 * Created by admin on 2018/6/7.
 *
 *
 */
public class CompanyCaseAdapter extends BaseQuickAdapter<CompanyCase.DataBean> {
    private Context context;

    public CompanyCaseAdapter(List<CompanyCase.DataBean> produces, Context context) {
        super(R.layout.item_supplycompany,produces);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyCase.DataBean item) {
        helper.setText(R.id.supply_tv,item.getCaseTitle());
        RoundImageView imageview = helper.getView(R.id.supply_icon);
        Glide.with(context).load(NetUrl.AllURL+item.getCasePicSmall()).
                placeholder(R.mipmap.holder_small).into(imageview);

    }

}
