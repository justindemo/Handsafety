package com.xytsz.xytaj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.Person;

import java.util.List;

/**
 * Created by admin on 2017/12/14.
 * 替换人员的列表
 */
public class ReplacePersonAdapter extends BaseAdapter{
    private List<Person> personNamelist;

    public ReplacePersonAdapter(List<Person> personNamelist) {

        this.personNamelist = personNamelist;
    }

    @Override
    public int getCount() {
        return personNamelist.size();
    }

    @Override
    public Object getItem(int position) {
        return personNamelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_replaceperson,null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_replacename);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(personNamelist.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        private TextView tv;
    }
}
