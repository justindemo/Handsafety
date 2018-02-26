package com.xytsz.xytaj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xytsz.xytaj.R;

import java.util.List;

/**
 * Created by admin on 2017/9/30.
 *
 *
 */
public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> resultData;

    public SearchAdapter(Context context, List<String> resultData) {

        this.context = context;
        this.resultData = resultData;
    }

    @Override
    public int getCount() {
        return resultData.size();
    }

    @Override
    public Object getItem(int position) {
        return resultData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null){
            hold = new ViewHold();
            convertView = View.inflate(context, R.layout.item_search_road,null);
            hold.tvRoadname = (TextView) convertView.findViewById(R.id.tv_road);
            convertView.setTag(hold);
        }else {
            hold = (ViewHold) convertView.getTag();
        }


        hold.tvRoadname.setText(resultData.get(position));



        return convertView;
    }

    class ViewHold{
        public TextView tvRoadname;
    }
}
