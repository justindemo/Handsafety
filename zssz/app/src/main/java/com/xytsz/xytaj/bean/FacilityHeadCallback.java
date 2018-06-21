package com.xytsz.xytaj.bean;

import com.xytsz.xytaj.util.JsonUtil;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by admin on 2018/6/6.
 *
 */
public abstract class FacilityHeadCallback extends Callback<FacilityHead> {
    @Override
    public FacilityHead parseNetworkResponse(Response response, int id) throws Exception {
        String json = response.body().string();
        FacilityHead company = JsonUtil.jsonToBean(json,FacilityHead.class);
        return company;
    }
}
