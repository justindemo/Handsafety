package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.CompanyCase;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/6/8.
 *
 * 案例列表
 */
public class CaseListAdapter extends BaseQuickAdapter<CompanyCase.DataBean> {
    private Context context;

    public CaseListAdapter(List<CompanyCase.DataBean> data, Context context) {
        super(R.layout.item_producelist,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyCase.DataBean item) {
        helper.setText(R.id.tv_produce_title,item.getCaseTitle());
        helper.setText(R.id.tv_produce_companyname,item.getCompanyName());
        ImageView imageview = helper.getView(R.id.iv_producelist);
        Glide.with(context).load(NetUrl.AllURL+item.getCasePicList()).
                placeholder(R.mipmap.holder_mid).into(imageview);
    }
}
