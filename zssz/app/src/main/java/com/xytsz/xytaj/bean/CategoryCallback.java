package com.xytsz.xytaj.bean;

import com.xytsz.xytaj.util.JsonUtil;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by admin on 2018/6/6.
 *
 */
public abstract class CategoryCallback extends Callback<Category> {
    @Override
    public Category parseNetworkResponse(Response response, int id) throws Exception {
        String json = response.body().string();
        Category company = JsonUtil.jsonToBean(json,Category.class);
        return company;
    }
}
