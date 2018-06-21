package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/28.
 *
 * 公司企业
 */
public class Company implements Serializable {


    /**
     * Properties : {}
     * IsSuccess : true
     * Data : {"ID":1,"CompanyName":"北京向阳天科技有限公司","CompanyLogo":"/upfile/201806/636638753559552032.jpg","CompanyURL":"网址","CompanyAddress":"北京奥宇英巢3号楼","CompanyTel":"电话","CompanyEmail":"电子邮件","CompanyQQ":"QQ","CompanyWeixin":"微信","CompanyLongitude":116.356077,"CompanyLatitude":39.768222,"CompanyDesc":"fdsafdsafdsa[br]fdsafdsafdsa[br]fdsafdsafdsa","CompanyPic":"","AddTime":"/Date(1528175514677)/","UpdateTime":"/Date(1528250000460)/","AddUserID":1,"TopIndex":0,"CompanyClass":3,"State":1,"CompanyPicList":"/upfile/201806/636638622260974060.jpg","CompanyPicBack":"/upfile/201806/636638753515704353.jpg"}
     * DataCount : null
     * Message : null
     * Code : null
     */

    private PropertiesBean Properties;
    private boolean IsSuccess;
    private DataBean Data;
    private int DataCount;
    private String Message;
    private int Code;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
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

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public static class PropertiesBean {
    }

    public static class DataBean implements Serializable {
        /**
         * ID : 1
         * CompanyName : 北京向阳天科技有限公司
         * CompanyLogo : /upfile/201806/636638753559552032.jpg
         * CompanyURL : 网址
         * CompanyAddress : 北京奥宇英巢3号楼
         * CompanyTel : 电话
         * CompanyEmail : 电子邮件
         * CompanyQQ : QQ
         * CompanyWeixin : 微信
         * CompanyLongitude : 116.356077
         * CompanyLatitude : 39.768222
         * CompanyDesc : fdsafdsafdsa[br]fdsafdsafdsa[br]fdsafdsafdsa
         * CompanyPic :
         * AddTime : /Date(1528175514677)/
         * UpdateTime : /Date(1528250000460)/
         * AddUserID : 1
         * TopIndex : 0
         * CompanyClass : 3
         * State : 1
         * CompanyPicList : /upfile/201806/636638622260974060.jpg
         * CompanyPicBack : /upfile/201806/636638753515704353.jpg
         */

        private int ID;
        private String CompanyName;
        private String CompanyLogo;
        private String CompanyURL;
        private String CompanyAddress;
        private String CompanyTel;
        private String CompanyEmail;
        private String CompanyQQ;
        private String CompanyWeixin;
        private double CompanyLongitude;
        private double CompanyLatitude;
        private String CompanyDesc;
        private String CompanyPic;
        private String AddTime;
        private String UpdateTime;
        private int AddUserID;
        private int TopIndex;
        private int CompanyClass;
        private int State;
        private String CompanyPicList;
        private String CompanyPicBack;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }

        public String getCompanyLogo() {
            return CompanyLogo;
        }

        public void setCompanyLogo(String CompanyLogo) {
            this.CompanyLogo = CompanyLogo;
        }

        public String getCompanyURL() {
            return CompanyURL;
        }

        public void setCompanyURL(String CompanyURL) {
            this.CompanyURL = CompanyURL;
        }

        public String getCompanyAddress() {
            return CompanyAddress;
        }

        public void setCompanyAddress(String CompanyAddress) {
            this.CompanyAddress = CompanyAddress;
        }

        public String getCompanyTel() {
            return CompanyTel;
        }

        public void setCompanyTel(String CompanyTel) {
            this.CompanyTel = CompanyTel;
        }

        public String getCompanyEmail() {
            return CompanyEmail;
        }

        public void setCompanyEmail(String CompanyEmail) {
            this.CompanyEmail = CompanyEmail;
        }

        public String getCompanyQQ() {
            return CompanyQQ;
        }

        public void setCompanyQQ(String CompanyQQ) {
            this.CompanyQQ = CompanyQQ;
        }

        public String getCompanyWeixin() {
            return CompanyWeixin;
        }

        public void setCompanyWeixin(String CompanyWeixin) {
            this.CompanyWeixin = CompanyWeixin;
        }

        public double getCompanyLongitude() {
            return CompanyLongitude;
        }

        public void setCompanyLongitude(double CompanyLongitude) {
            this.CompanyLongitude = CompanyLongitude;
        }

        public double getCompanyLatitude() {
            return CompanyLatitude;
        }

        public void setCompanyLatitude(double CompanyLatitude) {
            this.CompanyLatitude = CompanyLatitude;
        }

        public String getCompanyDesc() {
            return CompanyDesc;
        }

        public void setCompanyDesc(String CompanyDesc) {
            this.CompanyDesc = CompanyDesc;
        }

        public String getCompanyPic() {
            return CompanyPic;
        }

        public void setCompanyPic(String CompanyPic) {
            this.CompanyPic = CompanyPic;
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

        public int getAddUserID() {
            return AddUserID;
        }

        public void setAddUserID(int AddUserID) {
            this.AddUserID = AddUserID;
        }

        public int getTopIndex() {
            return TopIndex;
        }

        public void setTopIndex(int TopIndex) {
            this.TopIndex = TopIndex;
        }

        public int getCompanyClass() {
            return CompanyClass;
        }

        public void setCompanyClass(int CompanyClass) {
            this.CompanyClass = CompanyClass;
        }

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public String getCompanyPicList() {
            return CompanyPicList;
        }

        public void setCompanyPicList(String CompanyPicList) {
            this.CompanyPicList = CompanyPicList;
        }

        public String getCompanyPicBack() {
            return CompanyPicBack;
        }

        public void setCompanyPicBack(String CompanyPicBack) {
            this.CompanyPicBack = CompanyPicBack;
        }
    }
}
