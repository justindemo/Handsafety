package com.xytsz.xytaj.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.DiseaseDetailActivity;
import com.xytsz.xytaj.activity.SendBigPhotoActivity;
import com.xytsz.xytaj.activity.UnCheckActivity;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.bean.Review;
import com.xytsz.xytaj.util.SoundUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/2/22.
 *
 */
public class PostRoadAdapter extends BaseAdapter {


    private List<Review> reviewRoad;
    private List<List<ImageUrl>> imageUrlLists;
    private List<AudioUrl> audioUrls;
    private String imgurl;
    private SoundUtil soundUtil;

    public PostRoadAdapter(List<Review> reviews, List<List<ImageUrl>> imageUrlLists, List<AudioUrl> audioUrls) {

        this.reviewRoad = reviews;
        this.imageUrlLists = imageUrlLists;
        this.audioUrls = audioUrls;

        soundUtil = new SoundUtil();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_postroad, null);
            holder.Vname = (TextView) convertView.findViewById(R.id.tv_send_Vname);
            holder.rlDetail = (RelativeLayout) convertView.findViewById(R.id.rl_post);
            holder.Pname = (TextView) convertView.findViewById(R.id.tv_send_Pname);
            holder.tvProblemAudio = (TextView) convertView.findViewById(R.id.tv_post_audio);
            holder.date = (TextView) convertView.findViewById(R.id.tv_send_date);
            holder.sendIcon = (ImageView) convertView.findViewById(R.id.iv_send_icon);
            holder.btPost = (Button) convertView.findViewById(R.id.bt_post_send);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //
        final Review reviewRoadDetail = reviewRoad.get(position);

        String uploadTime = reviewRoadDetail.getCheckTime();

        //String userName = SpUtils.getString(parent.getContext(), GlobalContanstant.USERNAME);
        holder.Vname.setText(reviewRoadDetail.getCheckPersonName());
        holder.Pname.setText(reviewRoadDetail.getRemarks());
        holder.date.setText(uploadTime);
        holder.btPost.setTag(position);

        holder.btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UnCheckActivity.class);
                intent.putExtra("reviewRoadDetail", reviewRoadDetail);
                v.getContext().startActivity(intent);
            }
        });

        if (imageUrlLists.size() != 0) {
            urlList = imageUrlLists.get(position);
            //显示的第一张图片
            if (urlList.size() != 0) {
                ImageUrl imageUrl = urlList.get(0);
                imgurl = imageUrl.getImgurl();
                Glide.with(parent.getContext()).load(imgurl).into(holder.sendIcon);
                holder.sendIcon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SendBigPhotoActivity.class);
                        intent.putExtra("imageurl", imageUrlLists.get(position).get(0).getImgurl());
                        v.getContext().startActivity(intent);
                    }
                });


            } else {
                Glide.with(parent.getContext()).load(R.mipmap.prepost).fitCenter().into(holder.sendIcon);
            }

        }


        if (reviewRoadDetail.getRemarks().isEmpty()) {
            final AudioUrl audioUrl = audioUrls.get(position);

            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("fasle")) {

                        if (!audioUrl.getTime().isEmpty()) {
                            holder.Pname.setVisibility(View.GONE);
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
                        holder.Pname.setVisibility(View.VISIBLE);
                        holder.tvProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.Pname.setVisibility(View.VISIBLE);
                holder.tvProblemAudio.setVisibility(View.GONE);
            }
        } else {
            holder.Pname.setVisibility(View.VISIBLE);
            holder.tvProblemAudio.setVisibility(View.GONE);
        }

        holder.rlDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DiseaseDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("detail", reviewRoad.get(position));
                intent.putExtra("audioUrl", audioUrls.get(position));
                intent.putExtra("imageUrls", (Serializable) imageUrlLists.get(position));
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }



    private List<ImageUrl> urlList;

    static class ViewHolder {
        public TextView Vname;
        public TextView date;
        public TextView tvProblemAudio;
        public TextView Pname;
        public ImageView sendIcon;
        public Button btPost;
        public RelativeLayout rlDetail;
    }

}
