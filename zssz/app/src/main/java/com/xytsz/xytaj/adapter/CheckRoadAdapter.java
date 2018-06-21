package com.xytsz.xytaj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;

import java.util.List;

/**
 * Created by admin on 2017/2/22
 */
public class CheckRoadAdapter extends BaseAdapter {


    private List<Review> reviewRoad;
    private List<List<ImageUrl>> imageUrlLists;
    private List<List<ImageUrl>> imageUrlPostLists;

    private List<Review> reviewRoadDetails;

    public CheckRoadAdapter(List<Review> reviewRoad, List<List<ImageUrl>> imageUrlLists,
                            List<List<ImageUrl>> imageUrlPostLists) {

        this.reviewRoad = reviewRoad;
        this.imageUrlLists = imageUrlLists;
        this.imageUrlPostLists = imageUrlPostLists;
        reviewRoadDetails = reviewRoad;
    }

    /**
     * 通知adapter更新
     *
     * @param reviewRoadDetailss gebg
     */
    public void updateAdapter(List<Review> reviewRoadDetailss) {
        this.reviewRoadDetails = reviewRoadDetailss;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return reviewRoad.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewRoad.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_checkroad, null);
            holder.ivReport = (ImageView) convertView.findViewById(R.id.iv_check_report);
            holder.ivDealed = (ImageView) convertView.findViewById(R.id.iv_check_dealed);
            holder.tvReporter = (TextView) convertView.findViewById(R.id.tv_check_reporter);
            holder.tvReviewer = (TextView) convertView.findViewById(R.id.tv_check_reviewer);
            holder.tvDealer = (TextView) convertView.findViewById(R.id.tv_check_dealer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示图片
        //false  显示的是否通过
        Review detail = reviewRoad.get(position);


        String userName = detail.getCheckPersonName();
        String acName = detail.getWXPersonName();
        holder.tvReporter.setText(userName);

        holder.tvDealer.setText(acName);


        //获取到当前点击的URL集合
        urlList = imageUrlLists.get(position);
        //显示的第一张图片
        if (urlList.size() != 0) {
            ImageUrl imageUrl = urlList.get(0);
            String imgurl = imageUrl.getImgurl();

            Glide.with(parent.getContext()).load(imgurl).into(holder.ivReport);
        }

        List<ImageUrl> urlpostList = imageUrlPostLists.get(position);

        if (urlpostList.size() != 0) {
            ImageUrl imageUrlpost = urlpostList.get(0);
            String imgurlPost = imageUrlpost.getImgurl();
            Glide.with(parent.getContext()).load(imgurlPost).into(holder.ivDealed);
        }

        return convertView;
    }
    private List<ImageUrl> urlList;
    static class ViewHolder {
        public TextView tvReporter;
        public TextView tvDealer;
        public TextView tvReviewer;

        public ImageView ivReport;
        public ImageView ivDealed;

    }
}
