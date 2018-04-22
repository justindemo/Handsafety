package com.xytsz.xytaj.bean;


import java.io.Serializable;
import java.util.List;


/**
 * Created by admin on 2017/3/14.
 * 审核数据
 */
public class Review implements Serializable {


    /**
     * CheckInfo : 2
     * CheckPersonName : 王蒙蒙
     * CheckTime : /Date(1517992730040)/
     * DeciceCheckNum : 20180207163851
     * DeviceId : 2
     * ErrorInfo : ["灭火器是否清洁"]
     * Remarks :
     * State : 0
     * WXInfo : 维修信息
     * WXPersonName : 维修人员
     * WXTime : 维修时间
     * YSInfo : 验收信息
     * YSPersonName : 验收人员
     * YSTime :  验收时间
     * ZZCSInfo :  措施信息
     * ZZCSPersonName : 措施人
     * ZZCSSHInfo : 审核措施 意见
     * ZZCSSHPersonName : 审核措施人
     * ZZCSSHTime : 审核措施时间
     * ZZCSTime : 措施时间
     * ZZTZInfo : 通知信息
     * ZZTZPersonName :  通知人
     * ZZTZTime : 通知时间
     * id : 0
     */

    private int id;
    private String DeviceNum;
    private String DeciceCheckNum;
    private String AddressInfo;
    private String DeptName;
    private int DeviceId;
    private int CheckInfo;
    private String CheckPersonName;
    private String CheckTime;
    private String Remarks;
    private int State;
    private String WXInfo;
    private String WXPersonName;
    private String WXTime;
    private String YSInfo;
    private String YSPersonName;
    private String YSTime;
    private String ZZCSInfo;
    private String ZZCSPersonName;
    private String ZZCSSHInfo;
    private String ZZCSSHPersonName;
    private String ZZCSSHTime;
    private String ZZCSTime;
    private String ZZTZInfo;
    private String ZZTZPersonName;
    private String ZZTZTime;
    private String Administrator;
    private String DeviceName;
    private String sendCheckPerson;

    public int getSendCheckPersonId() {
        return sendCheckPersonId;
    }

    public void setSendCheckPersonId(int sendCheckPersonId) {
        this.sendCheckPersonId = sendCheckPersonId;
    }

    private int sendCheckPersonId;

    public String getSendCheckPerson() {
        return sendCheckPerson;
    }

    public void setSendCheckPerson(String sendCheckPerson) {
        this.sendCheckPerson = sendCheckPerson;
    }

    public String getAddressInfo() {
        return AddressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        AddressInfo = addressInfo;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        DeviceNum = deviceNum;
    }

    public String getAdministrator() {
        return Administrator;
    }

    public void setAdministrator(String administrator) {
        Administrator = administrator;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    private String RequirementsComplete_Person_Name;
    private String RequirementsCompleteTime;
    private List<String> ErrorInfo;

    public String getRequirementsCompleteTime() {
        return RequirementsCompleteTime;
    }

    public void setRequirementsCompleteTime(String requirementsCompleteTime) {
        RequirementsCompleteTime = requirementsCompleteTime;
    }

    public String getRequirementsComplete_Person_Name() {
        return RequirementsComplete_Person_Name;
    }

    public void setRequirementsComplete_Person_Name(String requirementsComplete_Person_Name) {
        RequirementsComplete_Person_Name = requirementsComplete_Person_Name;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        isMultiSelect = multiSelect;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    private int position;
    private boolean isMultiSelect;
    private boolean isCheck;
    private boolean isShow;
    private String advice;
    private String requestTime;
    private String sendPerson;

    public int getSendpersonID() {
        return sendpersonID;
    }

    public void setSendpersonID(int sendpersonID) {
        this.sendpersonID = sendpersonID;
    }

    private int sendpersonID;

    public String getSendPerson() {
        return sendPerson;
    }

    public void setSendPerson(String sendPerson) {
        this.sendPerson = sendPerson;
    }


    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public int getCheckInfo() {
        return CheckInfo;
    }

    public void setCheckInfo(int CheckInfo) {
        this.CheckInfo = CheckInfo;
    }

    public String getCheckPersonName() {
        return CheckPersonName;
    }

    public void setCheckPersonName(String CheckPersonName) {
        this.CheckPersonName = CheckPersonName;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String CheckTime) {
        this.CheckTime = CheckTime;
    }

    public String getDeciceCheckNum() {
        return DeciceCheckNum;
    }

    public void setDeciceCheckNum(String DeciceCheckNum) {
        this.DeciceCheckNum = DeciceCheckNum;
    }

    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int DeviceId) {
        this.DeviceId = DeviceId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getWXInfo() {
        return WXInfo;
    }

    public void setWXInfo(String WXInfo) {
        this.WXInfo = WXInfo;
    }

    public String getWXPersonName() {
        return WXPersonName;
    }

    public void setWXPersonName(String WXPersonName) {
        this.WXPersonName = WXPersonName;
    }

    public String getWXTime() {
        return WXTime;
    }

    public void setWXTime(String WXTime) {
        this.WXTime = WXTime;
    }

    public String getYSInfo() {
        return YSInfo;
    }

    public void setYSInfo(String YSInfo) {
        this.YSInfo = YSInfo;
    }

    public String getYSPersonName() {
        return YSPersonName;
    }

    public void setYSPersonName(String YSPersonName) {
        this.YSPersonName = YSPersonName;
    }

    public String getYSTime() {
        return YSTime;
    }

    public void setYSTime(String YSTime) {
        this.YSTime = YSTime;
    }

    public String getZZCSInfo() {
        return ZZCSInfo;
    }

    public void setZZCSInfo(String ZZCSInfo) {
        this.ZZCSInfo = ZZCSInfo;
    }

    public String getZZCSPersonName() {
        return ZZCSPersonName;
    }

    public void setZZCSPersonName(String ZZCSPersonName) {
        this.ZZCSPersonName = ZZCSPersonName;
    }

    public String getZZCSSHInfo() {
        return ZZCSSHInfo;
    }

    public void setZZCSSHInfo(String ZZCSSHInfo) {
        this.ZZCSSHInfo = ZZCSSHInfo;
    }

    public String getZZCSSHPersonName() {
        return ZZCSSHPersonName;
    }

    public void setZZCSSHPersonName(String ZZCSSHPersonName) {
        this.ZZCSSHPersonName = ZZCSSHPersonName;
    }

    public String getZZCSSHTime() {
        return ZZCSSHTime;
    }

    public void setZZCSSHTime(String ZZCSSHTime) {
        this.ZZCSSHTime = ZZCSSHTime;
    }

    public String getZZCSTime() {
        return ZZCSTime;
    }

    public void setZZCSTime(String ZZCSTime) {
        this.ZZCSTime = ZZCSTime;
    }

    public String getZZTZInfo() {
        return ZZTZInfo;
    }

    public void setZZTZInfo(String ZZTZInfo) {
        this.ZZTZInfo = ZZTZInfo;
    }

    public String getZZTZPersonName() {
        return ZZTZPersonName;
    }

    public void setZZTZPersonName(String ZZTZPersonName) {
        this.ZZTZPersonName = ZZTZPersonName;
    }

    public String getZZTZTime() {
        return ZZTZTime;
    }

    public void setZZTZTime(String ZZTZTime) {
        this.ZZTZTime = ZZTZTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(List<String> ErrorInfo) {
        this.ErrorInfo = ErrorInfo;
    }
}
