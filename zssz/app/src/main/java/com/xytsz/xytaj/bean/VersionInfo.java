package com.xytsz.xytaj.bean;

/**
 * Created by admin on 2017/4/6.
 *
 */
public class VersionInfo {


    private String	versionName;//版本名
    private int		versionCode;//版本号
    private String  message;//版本描述信息内容
    private String	downloadUrl;//新版本的下载路径
    private String versionSize;//版本大小


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }




}
