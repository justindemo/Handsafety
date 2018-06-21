package com.xytsz.xytaj.bean;

import java.util.List;

/**
 * Created by admin on 2018/6/11.
 * </p>
 */
public class FacilityHead  {


    /**
     * Properties : {}
     * IsSuccess : true
     * Data : [{"ID":1,"CompanyImg":"/upfile/201806/636639855600497498.png","CompanyID":13,"Sort":0,"AdClass":1,"ProductClassID":0,"CompanyName":null,"ClassName":null},{"ID":2,"CompanyImg":"/upfile/201806/636640680008477099.jpg","CompanyID":11,"Sort":0,"AdClass":2,"ProductClassID":2,"CompanyName":null,"ClassName":null}]
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

    public static class DataBean {
        /**
         * ID : 1
         * CompanyImg : /upfile/201806/636639855600497498.png
         * CompanyID : 13
         * Sort : 0
         * AdClass : 1
         * ProductClassID : 0
         * CompanyName : null
         * ClassName : null
         */

        private int ID;
        private String CompanyImg;
        private int CompanyID;
        private int Sort;
        private int AdClass;
        private int ProductClassID;
        private Object CompanyName;
        private Object ClassName;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getCompanyImg() {
            return CompanyImg;
        }

        public void setCompanyImg(String CompanyImg) {
            this.CompanyImg = CompanyImg;
        }

        public int getCompanyID() {
            return CompanyID;
        }

        public void setCompanyID(int CompanyID) {
            this.CompanyID = CompanyID;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public int getAdClass() {
            return AdClass;
        }

        public void setAdClass(int AdClass) {
            this.AdClass = AdClass;
        }

        public int getProductClassID() {
            return ProductClassID;
        }

        public void setProductClassID(int ProductClassID) {
            this.ProductClassID = ProductClassID;
        }

        public Object getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(Object CompanyName) {
            this.CompanyName = CompanyName;
        }

        public Object getClassName() {
            return ClassName;
        }

        public void setClassName(Object ClassName) {
            this.ClassName = ClassName;
        }
    }
}
