package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.net.NetUrl;

import java.util.List;

/**
 * Created by admin on 2018/5/31.
 *
 * 产品详细页
 */
public class ProduceDetailAdapter extends BaseQuickAdapter<String> {


    private List<String> imgs;
    private Context context;
    private boolean b;

    public ProduceDetailAdapter(List<String> data, List<String> imgs, Context context,boolean b) {
        super(R.layout.item_producedetail, data);
        this.imgs = imgs;
        this.context = context;
        this.b = b;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int adapterPosition = helper.getLayoutPosition();
        ImageView view = helper.getView(R.id.iv_producedetail);
        if (b) {
            item = item.replace("[br]","");
            helper.setText(R.id.tv_producedetail, "\u3000\u3000"+item);
            if (adapterPosition <= imgs.size()-1) {
                Glide.with(context).load(NetUrl.AllURL + imgs.get(adapterPosition)).placeholder(R.mipmap.produce_big).into(view);
            }else {
                helper.setVisible(R.id.iv_producedetail,false);
            }
        }else {
            if (adapterPosition <= imgs.size()-1) {
                helper.setText(R.id.tv_producedetail, "\u3000\u3000" +imgs.get(adapterPosition).replace("[br]", ""));
            }else {
                helper.setVisible(R.id.tv_producedetail,false);
            }
            Glide.with(context).load(NetUrl.AllURL + item).placeholder(R.mipmap.produce_big).into(view);
        }
    }
}
