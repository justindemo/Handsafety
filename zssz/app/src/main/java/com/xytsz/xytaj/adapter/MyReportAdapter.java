package com.xytsz.xytaj.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.AudioUrl;
import com.xytsz.xytaj.bean.ForMyDis;
import com.xytsz.xytaj.bean.ImageUrl;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;

import java.util.List;

/**
 * Created by admin on 2017/5/26.
 * 我上报界面的显示数据
 */
public class MyReportAdapter extends BaseAdapter {

    private List<ForMyDis> details;
    private List<List<ImageUrl>> imageurlList;
    private List<AudioUrl> audioUrls;
    private SoundUtil soundUtil;


    public MyReportAdapter(List<ForMyDis> details, List<List<ImageUrl>> imageurlList, List<AudioUrl> audioUrls) {

        this.details = details;

        this.imageurlList = imageurlList;
        this.audioUrls = audioUrls;

        soundUtil = new SoundUtil();
    }

    @Override
    public int getCount() {

        return details.size();
    }

    @Override
    public Object getItem(int position) {
        return details.get(position);
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
            convertView = View.inflate(parent.getContext(), R.layout.item_myreport, null);
            holder.reporter = (TextView) convertView.findViewById(R.id.tv_my_reporter);
            holder.pbname = (TextView) convertView.findViewById(R.id.tv_my_pbname);
            holder.tvProblemAudio = (TextView) convertView.findViewById(R.id.tv_my_problem_audio);
            holder.time = (TextView) convertView.findViewById(R.id.tv_my_report_time);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_reporte);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ForMyDis forMyDis = details.get(position);

        String upload_person_id = forMyDis.getUpload_Person_ID() + "";
        //通过上报人的ID 拿到上报人的名字
        //获取到所有人的列表 把对应的 id 找出名字
        List<String> personNamelist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONNAMELIST);
        List<String> personIDlist = SpUtils.getStrListValue(parent.getContext(), GlobalContanstant.PERSONIDLIST);

        for (int i = 0; i < personIDlist.size(); i++) {
            if (upload_person_id.equals(personIDlist.get(i))) {
                id = i;
            }
        }

        String userName = personNamelist.get(id);

        holder.reporter.setText(userName);

        holder.pbname.setText(forMyDis.getAddressDescription());

        if (forMyDis.getAddressDescription().isEmpty()) {
            final AudioUrl audioUrl = audioUrls.get(position);
            if (audioUrl != null) {
                if (audioUrl.getAudioUrl() != null) {
                    if (!audioUrl.getAudioUrl().equals("false")) {
                        if (!audioUrl.getTime().isEmpty()) {
                            holder.pbname.setVisibility(View.GONE);
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

                        } else {
                            holder.pbname.setVisibility(View.VISIBLE);
                            holder.tvProblemAudio.setVisibility(View.GONE);
                        }
                    } else {
                        holder.pbname.setVisibility(View.VISIBLE);
                        holder.tvProblemAudio.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.pbname.setVisibility(View.VISIBLE);
                holder.tvProblemAudio.setVisibility(View.GONE);
            }
        } else {
            holder.pbname.setVisibility(View.VISIBLE);
            holder.tvProblemAudio.setVisibility(View.GONE);
        }


        holder.time.setText(forMyDis.getUploadTime());
        List<ImageUrl> imageUrlList = imageurlList.get(position);
        if (imageUrlList.size() != 0) {
            Glide.with(parent.getContext()).load(imageUrlList.get(0).getImgurl()).into(holder.ivIcon);
        } else {
            Glide.with(parent.getContext()).load(R.mipmap.prepost).into(holder.ivIcon);

        }
        return convertView;
    }


    private int id;

    static class ViewHolder {
        public TextView reporter;
        public TextView pbname;
        public TextView time;
        public TextView tvProblemAudio;
        public ImageView ivIcon;
    }
}
