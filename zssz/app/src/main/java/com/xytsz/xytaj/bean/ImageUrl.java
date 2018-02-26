package com.xytsz.xytaj.bean;


import java.io.Serializable;

/**
 * Created by admin on 2017/3/27.
 *
 *
 */
public class ImageUrl implements Serializable {


    /**
     * Imgurl : http://123.126.40.12:8081/UpLoadMedia/aj/20180208143106.jpg
     * urlId : url0
     */

    private String Imgurl;
    private String urlId;

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String Imgurl) {
        this.Imgurl = Imgurl;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }
}
