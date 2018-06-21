package com.xytsz.xytaj.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.MoringMeeting;
import com.xytsz.xytaj.util.ToastUtil;

import java.util.List;

/**
 * Created by admin on 2018/4/21.
 *
 *
 */
public class MoringSignAdapter extends BaseQuickAdapter<MoringMeeting> {
    private Context context;
    private List<String> personlist;

    public MoringSignAdapter(List<MoringMeeting> data, Context context,List<String> personlist) {
        super(R.layout.item_moringsign,data);
        this.context = context;
        this.personlist = personlist;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MoringMeeting item) {
        helper.setText(R.id.tv_moringsign_team,item.getDeptName());
        helper.setText(R.id.tv_moringsign_stime,item.getBeginTime());
        helper.setText(R.id.tv_moringsign_etime,item.getEndTime());
        helper.setText(R.id.tv_moringsign_number,item.getCount()+"");
        helper.setText(R.id.tv_moringsign_totalnumber,item.getSumCount()+"");

        helper.setOnClickListener(R.id.tv_moringsign_titile, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(context).create();
                dialog.setCancelable(true);// 可以用“返回键”取消
                dialog.setCanceledOnTouchOutside(true);//
                dialog.show();
                dialog.setContentView(R.layout.moring_signeder);
                RecyclerView mRv = (RecyclerView) dialog.findViewById(R.id.moringsigned_rv);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                mRv.setLayoutManager(gridLayoutManager);
                MoringsignedAdapter moringsignedAdapter = new MoringsignedAdapter(personlist);
                mRv.setAdapter(moringsignedAdapter);
            }
        });
    }
}
