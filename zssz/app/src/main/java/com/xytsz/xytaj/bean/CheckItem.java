package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/21.
 * 检查项
 */
public class CheckItem implements Serializable {


    /**
     * id : 1
     * SignDicInfo : 是否佩戴工号牌
     */

    private int id;
    private String SignDicInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignDicInfo() {
        return SignDicInfo;
    }

    public void setSignDicInfo(String SignDicInfo) {
        this.SignDicInfo = SignDicInfo;
    }
}
