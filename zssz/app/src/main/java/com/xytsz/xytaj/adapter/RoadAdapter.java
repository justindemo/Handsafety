package com.xytsz.xytaj.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.activity.PhotoShowActivity;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.util.SoundUtil;

import java.io.Serializable;
import java.util.List;


/**
 * Created by admin on 2017/1/11.
 * 审批界面
 *
 */
public class RoadAdapter extends BaseAdapter {

    private List<Review> reviews;
    private List<List<ImageUrl>> imageUrlLists;
    private List<AudioUrl> audioUrls;
    private List<ImageUrl> urlList;
    private SoundUtil soundUtil;



    public RoadAdapter(List<Review> reviews, List<List<ImageUrl>> imageUrlLists, List<AudioUrl> audioUrls) {

        this.reviews = reviews;
        //返回的URl 集合
        this.imageUrlLists = imageUrlLists;
        this.audioUrls = audioUrls;

        soundUtil = new SoundUtil();
    }


    @Override
    public int getCount() {

        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_road, null);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_swipe_icon);
            holder.tvProblemlocaname = (TextView) convertView.findViewById(R.id.tv_problem_loca);
            holder.tvProblemAudio = (TextView) convertView.findViewById(R.id.tv_problem_audio);
            holder.tvProblemreporter = (TextView) convertView.findViewById(R.id.tv_problem_reporter);
            holder.tvProblemtime = (TextView) convertView.findViewById(R.id.tv_problem_reportertime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示图片 //
        Review review = reviews.get(position);
        String  userName  = review.getCheckPersonName();


        holder.tvProblemtime.setText(review.getCheckTime());


        //获取到当前点击的URL集合
        /**
         * 如果size 不为空
         */
        if (imageUrlLists.size() != 0) {
            urlList = imageUrlLists.get(position);
            //显示的第一张图片
            if (urlList.size() != 0) {
                ImageUrl imageUrl = urlList.get(0);
                String imgurl = imageUrl.getImgurl();
                Glide.with(parent.getContext()).load(imgurl).fitCenter().into(holder.ivIcon);
                holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PhotoShowActivity.class);
                        intent.putExtra("imageUrllist", (Serializable) imageUrlLists.get(position));
                        v.getContext().startActivity(intent);

                    }
                });
            } else {
                Glide.with(parent.getContext()).load(R.mipmap.prepost).fitCenter().into(holder.ivIcon);
            }
        }

        holder.tvProblemlocaname.setText(review.getRemarks());

        //判断是否有语音
        if (review.getRemarks().isEmpty()) {
            final AudioUrl audioUrl = audioUrls.get(position);
            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() !=null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {
                        if (!audioUrl.getTime().isEmpty()) {
                            holder.tvProblemlocaname.setVisibility(View.GONE);
                            holder.tvProblemAudio.setVisibility(View.VISIBLE);
                            holder.tvProblemAudio.setText(audioUrl.getTime());
                            holder.tvProblemAudio.setOnClickListener(new View.OnClickListener() {


                                @Override
                                public void onClick(View v) {

                                    Drawable drawable = parent.getContext().getResources().getDrawable(R.mipmap.pause);
                                    final Drawable drawableRight = parent.getContext().getResources().getDrawable(R.mipmap.play);
                                    final TextView tv = (TextView) v;
                                    tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

                                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                                        @Override
                                        public void onFinish() {
                                            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });

                                    soundUtil.play(audioUrl.getAudioUrl());
                                }
                            });
                        }
                    } else {
                        holder.tvProblemlocaname.setVisibility(View.VISIBLE);
                        holder.tvProblemAudio.setVisibility(View.GONE);
                    }
                }else {
                    holder.tvProblemlocaname.setVisibility(View.VISIBLE);
                    holder.tvProblemAudio.setVisibility(View.GONE);
                }
            } else {
                holder.tvProblemlocaname.setVisibility(View.VISIBLE);
                holder.tvProblemAudio.setVisibility(View.GONE);
            }
        } else {
            holder.tvProblemlocaname.setVisibility(View.VISIBLE);
            holder.tvProblemAudio.setVisibility(View.GONE);
        }

        holder.tvProblemreporter.setText(userName);



        return convertView;
    }

    private int time;
    private int id;

    static class ViewHolder {
        private TextView tvProblemlocaname;
        private TextView tvProblemAudio;
        private TextView tvProblemreporter;
        private TextView tvProblemtime;
        private ImageView ivIcon;

    }




}

