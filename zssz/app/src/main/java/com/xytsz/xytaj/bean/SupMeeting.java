package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/9.
 *
 *
 *
 */
public class SupMeeting implements Serializable {


    /**
     * MeetId : 1
     * Title : 仓储队全体安全会议
     * AddressInfo : 会议室
     * BeginTime : 2018-05-24 09:00:000
     * Url : http://aj.xytgps.com/EN/AJMeetingMobile.aspx
     */

    private int MeetId;
    private String Title;
    private String AddressInfo;
    private String BeginTime;
    private String Url;

    public int getMeetId() {
        return MeetId;
    }

    public void setMeetId(int MeetId) {
        this.MeetId = MeetId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(String AddressInfo) {
        this.AddressInfo = AddressInfo;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
