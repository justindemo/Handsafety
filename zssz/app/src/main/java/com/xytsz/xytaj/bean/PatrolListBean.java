package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/26.
 *  排查每天需要完成的数据bean
 */
public class PatrolListBean implements Serializable{

    /**
     * id : 1
     * DeviceNum : CCD20183301
     * DeviceName : 灭火器001
     * AddressInfo : 1号位置
     * Dept_Name : 仓储队
     * Administrator : 王蒙蒙
     * AJDeviceType : 灭火器
     */


    private int id;
    private String DeviceNum;
    private String DeviceName;
    private String AddressInfo;
    private String Dept_Name;
    private String Administrator;
    private String AJDeviceType;


    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    private String State;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public void setDeviceNum(String DeviceNum) {
        this.DeviceNum = DeviceNum;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(String AddressInfo) {
        this.AddressInfo = AddressInfo;
    }

    public String getDept_Name() {
        return Dept_Name;
    }

    public void setDept_Name(String Dept_Name) {
        this.Dept_Name = Dept_Name;
    }

    public String getAdministrator() {
        return Administrator;
    }

    public void setAdministrator(String Administrator) {
        this.Administrator = Administrator;
    }

    public String getAJDeviceType() {
        return AJDeviceType;
    }

    public void setAJDeviceType(String AJDeviceType) {
        this.AJDeviceType = AJDeviceType;
    }
}
