package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/30.
 *
 */
public class CompanyList implements Serializable {

    private int Id;
    private String companyName;
    private String companycontents;
    private String companyImgurl;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanycontents() {
        return companycontents;
    }

    public void setCompanycontents(String companycontents) {
        this.companycontents = companycontents;
    }

    public String getCompanyImgurl() {
        return companyImgurl;
    }

    public void setCompanyImgurl(String companyImgurl) {
        this.companyImgurl = companyImgurl;
    }
}
