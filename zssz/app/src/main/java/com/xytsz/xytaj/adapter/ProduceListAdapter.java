package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.CompanyProduce;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/5/31.
 *
 *
 */
public class ProduceListAdapter extends BaseQuickAdapter<CompanyProduce.Produce> {
    private Context context;

    public ProduceListAdapter(List<CompanyProduce.Produce> produceNames, Context context) {
        super(R.layout.item_producelist,produceNames);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyProduce.Produce item) {
        helper.setText(R.id.tv_produce_title,item.getProductName());
        helper.setText(R.id.tv_produce_companyname,item.getCompanyName());
        ImageView imageview = helper.getView(R.id.iv_producelist);
        Glide.with(context).load(NetUrl.AllURL+item.getProductPicList()).
                placeholder(R.mipmap.holder_mid).into(imageview);
    }
}
