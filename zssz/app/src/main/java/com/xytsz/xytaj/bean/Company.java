package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/28.
 *
 * 公司企业
 */
public class Company implements Serializable {

    private int ID;
    private String companyName;
    private int companyCount;
    private String companyImgUrl;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(int companyCount) {
        this.companyCount = companyCount;
    }

    public String getCompanyImgUrl() {
        return companyImgUrl;
    }

    public void setCompanyImgUrl(String companyImgUrl) {
        this.companyImgUrl = companyImgUrl;
    }
}
