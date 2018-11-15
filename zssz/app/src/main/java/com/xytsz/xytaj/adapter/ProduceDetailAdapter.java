package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.RoundImageView;

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
        int layoutPosition = helper.getLayoutPosition();
        RoundImageView view = helper.getView(R.id.iv_producedetail);
        if (b) {
            //描述的size>=图片的size
            item = item.replace("[br]","");
            helper.setText(R.id.tv_producedetail, "\u3000\u3000"+item);
            //当前条目如果 小于等于 图片的size
            //那么就展示
            if (layoutPosition <= imgs.size()-1) {
                Glide.with(context).load(NetUrl.AllURL + imgs.get(layoutPosition)).placeholder(R.mipmap.produce_big).into(view);
            }else {
//                多个描述 ，少图片的时候
                helper.setVisible(R.id.iv_producedetail,false);
            }
        }else {
            //描述的size<图片的size  item = 图片Url img 是文字
            //当前条目如果 小于等于 文字的size-1   size 5 ，，，0-4
            //那么久展示
            if (layoutPosition <= imgs.size()-1) {
                helper.setText(R.id.tv_producedetail, "\u3000\u3000" +imgs.get(layoutPosition).replace("[br]", ""));
            }else {
                helper.setVisible(R.id.tv_producedetail,false);
            }
            Glide.with(context).load(NetUrl.AllURL + item).placeholder(R.mipmap.produce_big).into(view);
        }
    }
}
