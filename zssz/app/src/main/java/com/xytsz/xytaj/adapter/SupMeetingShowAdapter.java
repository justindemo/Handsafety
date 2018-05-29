package com.xytsz.xytaj.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.bean.SupMeeting;

import java.util.List;

/**
 * Created by admin on 2018/5/9.
 *
 * 会议列表
 */
public class SupMeetingShowAdapter extends BaseQuickAdapter<SupMeeting>{
    public SupMeetingShowAdapter(List<SupMeeting> data) {
        super(R.layout.item_supmeeting,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupMeeting item) {
        helper.setText(R.id.tv_meeting_title,item.getTitle());
        helper.setText(R.id.tv_meeting_adress,item.getAddressInfo());
        helper.setText(R.id.tv_meeting_stime,item.getBeginTime());
    }
}
