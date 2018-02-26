package com.xytsz.xytaj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xytsz.xytaj.R;

/**
 * Created by admin on 2017/1/6.
 *
 */
public class MoreAdapter extends BaseAdapter{
    //桌面图片
    private int[] mIcons = {R.mipmap.more_person,R.mipmap.more_problem,R.mipmap.more_lib,R.mipmap.more_flood,R.mipmap.more_caroil};

    // 桌面标题
    private String[] mTitles = { "人员","病害","井盖", "防汛","公告"};

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_more, null);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.more_icon);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.more_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示图片
        holder.ivIcon.setImageResource(mIcons[position]);
        holder.tvTitle.setText(mTitles[position]);
        return convertView;
    }
    static class ViewHolder
    {
        public TextView tvTitle;
        public ImageView ivIcon;

    }
}
