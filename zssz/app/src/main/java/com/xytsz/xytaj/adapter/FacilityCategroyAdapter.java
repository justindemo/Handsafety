package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.CompanyList;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/5/29.
 * s
 */
public class FacilityCategroyAdapter extends BaseQuickAdapter<CompanyList.DataBean> {
    private List<CompanyList.DataBean> data;
    private Context context;

    public FacilityCategroyAdapter(List<CompanyList.DataBean> data, Context context) {
        super(R.layout.item_facility_list,data);
        this.data = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompanyList.DataBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_facilitycop_title,data.get(layoutPosition-1).getCompanyName());
        helper.setText(R.id.tv_facilitycop_msg,data.get(layoutPosition-1).getCompanyDesc());
        ImageView view = helper.getView(R.id.iv_facilitycop);
        Glide.with(context).load(NetUrl.AllURL+ data.get(layoutPosition-1).getCompanyPicList())
        .error(R.mipmap.holder_mid).into(view);
    }
}
