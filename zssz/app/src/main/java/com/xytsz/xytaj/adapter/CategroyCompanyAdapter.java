package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.FacilityCategroyCompany;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/6/8.
 *
 *
 */
public class CategroyCompanyAdapter extends BaseQuickAdapter<FacilityCategroyCompany.CompanyList> {
    private List<FacilityCategroyCompany.CompanyList> data;
    private Context context;

    public CategroyCompanyAdapter(List<FacilityCategroyCompany.CompanyList> data, Context context) {
        super(R.layout.item_facility_list,data);
        this.data = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FacilityCategroyCompany.CompanyList item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_facilitycop_title,data.get(layoutPosition-1).getCompanyName());
        helper.setText(R.id.tv_facilitycop_msg,data.get(layoutPosition-1).getCompanyDesc());
        ImageView view = helper.getView(R.id.iv_facilitycop);
        Glide.with(context).load(NetUrl.AllURL+ data.get(layoutPosition-1).getCompanyPicList())
                .error(R.mipmap.holder_mid).into(view);
    }
}
