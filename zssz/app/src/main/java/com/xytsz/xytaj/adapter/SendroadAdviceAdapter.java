package com.xytsz.xytaj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.Person;
import com.xytsz.xytaj.bean.Review;

import java.util.List;

/**
 * Created by admin on 2018/2/8.
 *
 *
 */
public class SendroadAdviceAdapter  extends BaseAdapter{
    private List<Person> personlist;
    private Review review;

    public SendroadAdviceAdapter(List<Person> personlist, Review review) {

        this.personlist = personlist;
        this.review = review;
    }

    @Override
    public int getCount() {
        return personlist.size();
    }

    @Override
    public Object getItem(int position) {
        return personlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_sendroadadvice, null);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_adapter_person);
            holder.cb = (CheckBox) convertView.findViewById(R.id.adapter_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userName.setText(personlist.get(position).getName());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb.isChecked()){
                    holder.cb.setChecked(true);
                    review.setSendPerson(personlist.get(position).getName());
                    review.setSendpersonID(personlist.get(position).getId());
                    review.setCheck(true);
                }else {
                    holder.cb.setChecked(false);
                    review.setCheck(false);
                }
            }
        });


        return convertView;
    }
    static class ViewHolder{
        TextView userName;
        CheckBox cb;
    }
}
