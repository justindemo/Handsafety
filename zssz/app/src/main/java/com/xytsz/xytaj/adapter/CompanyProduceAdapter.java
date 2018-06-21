package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.CompanyProduce;
import com.xytsz.xytaj.bean.CompanyProduceCallback;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/6/7.
 *
 */
public class CompanyProduceAdapter  extends BaseQuickAdapter<CompanyProduce.Produce>{
    private Context context;

    public CompanyProduceAdapter(List<CompanyProduce.Produce> produces, Context context) {
        super(R.layout.item_supplycompany,produces);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyProduce.Produce item) {
        helper.setText(R.id.supply_tv,item.getProductName());
        ImageView imageview = helper.getView(R.id.supply_icon);
        Glide.with(context).load(NetUrl.AllURL+item.getProductPicSmall()).
                placeholder(R.mipmap.holder_small).into(imageview);

    }
}
