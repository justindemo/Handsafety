package com.xytsz.xytaj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/2.
 * 检查项
 */
public class MoringCheckAdapter extends BaseAdapter {
    private List<String> checkItems;
    private DiseaseInformation diseaseInformation;
    private List<DiseaseInformation.CheckItem> checkItem  = new ArrayList<>();

    public MoringCheckAdapter(List<String> checkItems) {
        this.checkItems = checkItems;

    }
    public void setCheckItems(DiseaseInformation diseaseInformation){
        this.diseaseInformation = diseaseInformation;
        checkItem.clear();
        for (int i = 0; i < checkItems.size(); i++) {
            checkItem.add(new DiseaseInformation.CheckItem(false,1));
        }
        this.diseaseInformation.getCheckItems().addAll(checkItem);

    }

    public List<DiseaseInformation.CheckItem> getDiseaseInformation(){
        return this.diseaseInformation.getCheckItems();
    }


    @Override
    public int getCount() {
        return checkItems.size();
    }

    @Override
    public Object getItem(int position) {
        return checkItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_morningcheck, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_morningcheckname);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_morningcheck);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(checkItems.get(position));


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(true);
                    diseaseInformation.getCheckItems().get(position).setCheck(true);
                    diseaseInformation.getCheckItems().get(position).setPosition(0);
                }else {
                    holder.checkBox.setChecked(false);
                    diseaseInformation.getCheckItems().get(position).setCheck(false);
                    diseaseInformation.getCheckItems().get(position).setPosition(1);
                }
            }
        });


        return convertView;
    }

    static class ViewHolder{
        private CheckBox checkBox;
        private TextView tv;
    }

}
