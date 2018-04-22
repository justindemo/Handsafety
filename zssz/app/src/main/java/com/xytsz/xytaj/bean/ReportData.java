package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/1/31.
 *
 * 上报数据
 */
public class ReportData implements Serializable{


    /**
     * AddressInfo : 仓储队仓库2号位置
     * Administrator : 王蒙蒙
     * CheckType : 1
     * DeviceName : 灭火器002
     * DeviceNum : CC_2018_002
     * ID : 2
     * List : [{"CheckDic_ID":1,"CheckInfo":"灭火器是否清洁"},{"CheckDic_ID":2,"CheckInfo":"灭火器是否完好"},{"CheckDic_ID":3,"CheckInfo":"灭火器是否正常"},{"CheckDic_ID":4,"CheckInfo":"压力是否正常"}]
     */

    private String AddressInfo;
    private String Administrator;
    private int CheckType;
    private String DeviceName;
    private String DeviceNum;
    private int ID;
    private java.util.List<Problem> List;

    public String getChargeperson2() {
        return Chargeperson2;
    }

    public void setChargeperson2(String chargeperson2) {
        Chargeperson2 = chargeperson2;
    }

    private String Chargeperson2;

    public String getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(String AddressInfo) {
        this.AddressInfo = AddressInfo;
    }

    public String getAdministrator() {
        return Administrator;
    }

    public void setAdministrator(String Administrator) {
        this.Administrator = Administrator;
    }

    public int getCheckType() {
        return CheckType;
    }

    public void setCheckType(int CheckType) {
        this.CheckType = CheckType;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public void setDeviceNum(String DeviceNum) {
        this.DeviceNum = DeviceNum;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Problem> getList() {
        return List;
    }

    public void setList(List<Problem> List) {
        this.List = List;
    }

    public static class Problem {
        /**
         * CheckDic_ID : 1
         * CheckInfo : 灭火器是否清洁
         */

        private int CheckDic_ID;
        private String CheckInfo;

        public int getCheckDic_ID() {
            return CheckDic_ID;
        }

        public void setCheckDic_ID(int CheckDic_ID) {
            this.CheckDic_ID = CheckDic_ID;
        }

        public String getCheckInfo() {
            return CheckInfo;
        }

        public void setCheckInfo(String CheckInfo) {
            this.CheckInfo = CheckInfo;
        }
    }
}
