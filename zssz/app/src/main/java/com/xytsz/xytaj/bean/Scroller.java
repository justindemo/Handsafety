package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/25.
 *
 *
 */
public class Scroller implements Serializable {

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getScrollerUrl() {
        return scrollerUrl;
    }

    public void setScrollerUrl(String scrollerUrl) {
        this.scrollerUrl = scrollerUrl;
    }

    private String scrollerUrl;
    private int ID;
    private String companyName;


}
