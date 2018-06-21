package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.FacilityHead;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/11.
 * </p>
 */
public class FacilityHeadAdapter extends PagerAdapter {
    private Context context;
    private List<FacilityHead.DataBean> data;
    private int parentId;
    private List<FacilityHead.DataBean> dataBeens = new ArrayList<>();

    public FacilityHeadAdapter(Context context,List<FacilityHead.DataBean> data,int parentId) {
        this.context = context;
        this.data = data;
        this.parentId = parentId;
        dataBeens.clear();
        for (FacilityHead.DataBean detail :
                data) {
            if (parentId == detail.getProductClassID()){
                dataBeens.add(detail);
            }
        }
    }

    @Override
    public int getCount() {
        return dataBeens.size()== 0?0:dataBeens.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.viewpager_imageview,null);
        RoundImageView imageview = (RoundImageView) view.findViewById(R.id.imageview);
//        position = position % data.size();
        
        Glide.with(context).load(NetUrl.AllURL +dataBeens.get(position).getCompanyImg())
                .placeholder(R.mipmap.holder_mid).into(imageview);
        container.addView(imageview);
        return imageview;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }





}
