package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/21.
 *
 * 早会签到
 */
public class MoringMeeting implements Serializable{


    /**
     * ID : 6
     * SignInfo : 早会签到表
     * BeginTime : null
     * EndTime : null
     * DeptName : 辉煌队
     * State : 进行中
     * Count : 0
     */

    private int ID;
    private String SignInfo;
    private Object BeginTime;
    private Object EndTime;
    private String DeptName;
    private String State;
    private int Count;

    public int getSumCount() {
        return SumCount;
    }

    public void setSumCount(int sumCount) {
        SumCount = sumCount;
    }

    private int SumCount;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSignInfo() {
        return SignInfo;
    }

    public void setSignInfo(String SignInfo) {
        this.SignInfo = SignInfo;
    }

    public String getBeginTime() {
        return String.valueOf(BeginTime);
    }

    public void setBeginTime(Object BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getEndTime() {
        return String.valueOf(EndTime);
    }

    public void setEndTime(Object EndTime) {
        this.EndTime = EndTime;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }
}
