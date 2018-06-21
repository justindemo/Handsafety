package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/6/7.
 *
 * 公司案例
 */
public class CompanyCase implements Serializable {


    /**
     * Properties : {}
     * IsSuccess : true
     * Data : [{"ID":2,"CompanyID":4,"CaseTitle":"案例名称777","CaseDesc":"[br]","CasePic":"","AddTime":"/Date(1528022859280)/","UpdateTime":"/Date(1528241841970)/","State":1,"AddUserID":0,"CasePicSmall":"","CasePicList":"/upfile/201806/636638356622470392.jpg","CompanyName":"aaaaaaa444"},{"ID":1,"CompanyID":6,"CaseTitle":" 案例名称111","CaseDesc":"fdsafdsafdsa[br]fdsafdsafdsa","CasePic":"/upfile/201806/636638656308032292.jpg;/upfile/201806/636638656328442427.jpg","AddTime":"/Date(1528022835237)/","UpdateTime":"/Date(1528240060327)/","State":1,"AddUserID":0,"CasePicSmall":"","CasePicList":"","CompanyName":"aaaaaaa666"}]
     * DataCount : 2
     * Message :
     * Code : null
     */

    private PropertiesBean Properties;
    private boolean IsSuccess;
    private int DataCount;
    private String Message;
    private Object Code;
    private List<DataBean> Data;

    public PropertiesBean getProperties() {
        return Properties;
    }

    public void setProperties(PropertiesBean Properties) {
        this.Properties = Properties;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public int getDataCount() {
        return DataCount;
    }

    public void setDataCount(int DataCount) {
        this.DataCount = DataCount;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Object getCode() {
        return Code;
    }

    public void setCode(Object Code) {
        this.Code = Code;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class PropertiesBean {
    }

    public static class DataBean implements Serializable {
        /**
         * ID : 2
         * CompanyID : 4
         * CaseTitle : 案例名称777
         * CaseDesc : [br]
         * CasePic :
         * AddTime : /Date(1528022859280)/
         * UpdateTime : /Date(1528241841970)/
         * State : 1
         * AddUserID : 0
         * CasePicSmall :
         * CasePicList : /upfile/201806/636638356622470392.jpg
         * CompanyName : aaaaaaa444
         */

        private int ID;
        private int CompanyID;
        private String CaseTitle;
        private String CaseDesc;
        private String CasePic;
        private String AddTime;
        private String UpdateTime;
        private int State;
        private int AddUserID;
        private String CasePicSmall;
        private String CasePicList;
        private String CompanyName;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getCompanyID() {
            return CompanyID;
        }

        public void setCompanyID(int CompanyID) {
            this.CompanyID = CompanyID;
        }

        public String getCaseTitle() {
            return CaseTitle;
        }

        public void setCaseTitle(String CaseTitle) {
            this.CaseTitle = CaseTitle;
        }

        public String getCaseDesc() {
            return CaseDesc;
        }

        public void setCaseDesc(String CaseDesc) {
            this.CaseDesc = CaseDesc;
        }

        public String getCasePic() {
            return CasePic;
        }

        public void setCasePic(String CasePic) {
            this.CasePic = CasePic;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public int getAddUserID() {
            return AddUserID;
        }

        public void setAddUserID(int AddUserID) {
            this.AddUserID = AddUserID;
        }

        public String getCasePicSmall() {
            return CasePicSmall;
        }

        public void setCasePicSmall(String CasePicSmall) {
            this.CasePicSmall = CasePicSmall;
        }

        public String getCasePicList() {
            return CasePicList;
        }

        public void setCasePicList(String CasePicList) {
            this.CasePicList = CasePicList;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }
    }
}
