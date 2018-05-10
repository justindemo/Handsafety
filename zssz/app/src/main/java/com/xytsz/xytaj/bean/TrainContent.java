package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/12.
 *
 *
 */
public class TrainContent implements Serializable{


    /**
     * id : 1
     * TrainNum : 仓储队CCD2018425-1
     * BeginTime : 2018/4/25 0:00:00
     * Title : t
     * Address : t
     * DeptName : 仓储队
     * AJTrainProgrammeUrl : www.baidu.com 实施方案
     * AJTrainSummaryUrl : www.baidu.com 总结
     * AJTrainContentUrl : www.baidu.com 内容
     * AJTrainRecordUrl : www.baidu.com 记录
     * AJTrainNoticeUrl : www.baidu.com 通知
     */

    private int id;
    private String TrainNum;
    private String BeginTime;
    private String Title;
    private String Address;
    private String DeptName;
    private String AJTrainProgrammeUrl;
    private String AJTrainSummaryUrl;
    private String AJTrainContentUrl;
    private String AJTrainRecordUrl;
    private String AJTrainNoticeUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainNum() {
        return TrainNum;
    }

    public void setTrainNum(String TrainNum) {
        this.TrainNum = TrainNum;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getAJTrainProgrammeUrl() {
        return AJTrainProgrammeUrl;
    }

    public void setAJTrainProgrammeUrl(String AJTrainProgrammeUrl) {
        this.AJTrainProgrammeUrl = AJTrainProgrammeUrl;
    }

    public String getAJTrainSummaryUrl() {
        return AJTrainSummaryUrl;
    }

    public void setAJTrainSummaryUrl(String AJTrainSummaryUrl) {
        this.AJTrainSummaryUrl = AJTrainSummaryUrl;
    }

    public String getAJTrainContentUrl() {
        return AJTrainContentUrl;
    }

    public void setAJTrainContentUrl(String AJTrainContentUrl) {
        this.AJTrainContentUrl = AJTrainContentUrl;
    }

    public String getAJTrainRecordUrl() {
        return AJTrainRecordUrl;
    }

    public void setAJTrainRecordUrl(String AJTrainRecordUrl) {
        this.AJTrainRecordUrl = AJTrainRecordUrl;
    }

    public String getAJTrainNoticeUrl() {
        return AJTrainNoticeUrl;
    }

    public void setAJTrainNoticeUrl(String AJTrainNoticeUrl) {
        this.AJTrainNoticeUrl = AJTrainNoticeUrl;
    }
}
