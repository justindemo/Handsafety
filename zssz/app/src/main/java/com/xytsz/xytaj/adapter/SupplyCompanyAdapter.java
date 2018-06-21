package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.Company;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.RoundImageView;

import java.util.List;

/**
 * Created by admin on 2018/5/28.
 *
 * 供应链首页的公司
 */
public class SupplyCompanyAdapter extends BaseQuickAdapter<CompanyList.DataBean> {
    private Context context;

    public SupplyCompanyAdapter(List<CompanyList.DataBean> data, Context context) {
        super(R.layout.item_supplycompany,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyList.DataBean item) {

        helper.setText(R.id.supply_tv,item.getCompanyName());
        RoundImageView imageview = helper.getView(R.id.supply_icon);
        Glide.with(context).load(NetUrl.AllURL+item.getCompanyLogo()).placeholder(R.mipmap.holder_small).
                into(imageview);


    }
}
