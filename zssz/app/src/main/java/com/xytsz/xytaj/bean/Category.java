package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/6/7.
 * 产品分类
 */
public class Category implements Serializable {


    /**
     * Properties : {}
     * IsSuccess : true
     * Data : [{"ID":2,"ClassName":"消防安全","ClassAtrribute":"型号;重量;颜色","ParentID":0,"ItemLevel":1,"ItemIndex":2},{"ID":1,"ClassName":"消火栓","ClassAtrribute":"型号;重量;颜色","ParentID":2,"ItemLevel":2,"ItemIndex":2.1},{"ID":3,"ClassName":"粉尘防爆","ClassAtrribute":"型号;重量;颜色","ParentID":0,"ItemLevel":1,"ItemIndex":3},{"ID":4,"ClassName":"职业卫生","ClassAtrribute":"型号;重量;颜色","ParentID":0,"ItemLevel":1,"ItemIndex":4},{"ID":5,"ClassName":"环境保护","ClassAtrribute":"型号;重量;颜色","ParentID":0,"ItemLevel":1,"ItemIndex":5}]
     * DataCount : 5
     * Message :
     * Code : null
     */

    private PropertiesBean Properties;
    private boolean IsSuccess;
    private int DataCount;
    private String Message;
    private Object Code;
    private List<Detail> Data;

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

    public List<Detail> getData() {
        return Data;
    }

    public void setData(List<Detail> Data) {
        this.Data = Data;
    }

    public static class PropertiesBean {
    }

    public static class Detail {
        /**
         * ID : 2
         * ClassName : 消防安全
         * ClassAtrribute : 型号;重量;颜色
         * ParentID : 0
         * ItemLevel : 1
         * ItemIndex : 2
         */

        private int ID;
        private String ClassName;
        private String ClassAtrribute;
        private int ParentID;
        private int ItemLevel;
        private double ItemIndex;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String ClassName) {
            this.ClassName = ClassName;
        }

        public String getClassAtrribute() {
            return ClassAtrribute;
        }

        public void setClassAtrribute(String ClassAtrribute) {
            this.ClassAtrribute = ClassAtrribute;
        }

        public int getParentID() {
            return ParentID;
        }

        public void setParentID(int ParentID) {
            this.ParentID = ParentID;
        }

        public int getItemLevel() {
            return ItemLevel;
        }

        public void setItemLevel(int ItemLevel) {
            this.ItemLevel = ItemLevel;
        }

        public double getItemIndex() {
            return ItemIndex;
        }

        public void setItemIndex(double ItemIndex) {
            this.ItemIndex = ItemIndex;
        }
    }
}
