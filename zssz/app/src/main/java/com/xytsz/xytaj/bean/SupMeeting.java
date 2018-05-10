package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/9.
 *
 *
 *
 */
public class SupMeeting implements Serializable {

    private int id;
    private String MeetingNum;
    private String BeginTime;
    private String Title;
    private String Address;
    private String DeptName;
    private String AJMeetingContentUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeetingNum() {
        return MeetingNum;
    }

    public void setMeetingNum(String meetingNum) {
        MeetingNum = meetingNum;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getAJMeetingContentUrl() {
        return AJMeetingContentUrl;
    }

    public void setAJMeetingContentUrl(String AJMeetingContentUrl) {
        this.AJMeetingContentUrl = AJMeetingContentUrl;
    }
}
