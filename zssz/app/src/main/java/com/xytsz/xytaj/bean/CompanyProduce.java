package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2018/6/7.
 *
 * 公司产品
 */
public class CompanyProduce implements Serializable {


    /**
     * Properties : {}
     * IsSuccess : true
     * Data : [{"ID":6,"CompanyID":4,"ProductName":"泄爆口","ProductDesc":"[img]/upfile/201806/636639683536823591.png[/img]泄爆口也称为泄爆面积，指有可能产生爆炸的房间或者箱体为了避免爆炸而设的排泄压力或者能量的通道。[br]比如房间的窗口、门，压力设备也会专门设计一个泄爆口。","ProductPic":"/upfile/201806/636639683536823591.png","ProductClass":3,"ProductAttribute":";;;","AddTime":"/Date(1528342760020)/","UpdateTime":"/Date(1528342760020)/","AddUserID":1,"State":1,"ProductPrice":0,"ProductPicSmall":"/upfile/201806/636639683053528790.png","ProductPicList":"/upfile/201806/636639683385562113.png","ClassName":"粉尘防爆","ClassAtrribute":"型号;重量;颜色","CompanyName":"重庆云赛环保科技有限公司"},{"ID":5,"CompanyID":4,"ProductName":"火花探测仪","ProductDesc":"[img]/upfile/201806/636639681487403450.png[/img]火花探测器能够探测到最小的火花和炽热颗粒，具有高度的灵敏性。能够穿过高密度的物料、尘埃进行探测。采用光电检测技术。不对被检测对象有任何的干扰，不影响生产流程。光电探测器相对于其他探测器，具有灵敏度高、稳定性好、低功耗、寿命长等优点，适合工业级的不间断工作。防尘防水，可直接用于户外环境。[br][img]/upfile/201806/636639681557942678.png[/img]","ProductPic":"/upfile/201806/636639681487403450.png;/upfile/201806/636639681557942678.png","ProductClass":3,"ProductAttribute":";;;","AddTime":"/Date(1528342596180)/","UpdateTime":"/Date(1528342596180)/","AddUserID":1,"State":1,"ProductPrice":0,"ProductPicSmall":"/upfile/201806/636639681940440308.png","ProductPicList":"/upfile/201806/636639681856417411.png","ClassName":"粉尘防爆","ClassAtrribute":"型号;重量;颜色","CompanyName":"重庆云赛环保科技有限公司"},{"ID":4,"CompanyID":4,"ProductName":"磁旋装置","ProductDesc":"","ProductPic":"","ProductClass":3,"ProductAttribute":";;;","AddTime":"/Date(1528342420633)/","UpdateTime":"/Date(1528342511770)/","AddUserID":1,"State":1,"ProductPrice":0,"ProductPicSmall":"","ProductPicList":"","ClassName":"粉尘防爆","ClassAtrribute":"型号;重量;颜色","CompanyName":"重庆云赛环保科技有限公司"},{"ID":3,"CompanyID":4,"ProductName":"中央集尘器","ProductDesc":"[img]/upfile/201806/636639044638026247.png[/img]由吸尘主机、管网、吸尘插口、吸尘组件组成。吸尘主机置于建筑物的机房、阳台、车库、设备间内或室外）。主机通过管网与每个房间的吸口相连接，在进行清洁工作时将吸尘组件插入吸口，有害气体与灰尘通过真空吸尘管道，将室内且有害的灰尘吸到大容量的垃圾袋中。操作简单，避免了噪音对室内的影响，避免了灰尘带来的二次污染且彻底的清除了体积较小污染。[br][img]/upfile/201806/636639045233128479.png[/img]","ProductPic":"/upfile/201806/636639044638026247.png;/upfile/201806/636639045233128479.png","ProductClass":3,"ProductAttribute":";;;","AddTime":"/Date(1528278934920)/","UpdateTime":"/Date(1528278934920)/","AddUserID":1,"State":1,"ProductPrice":0,"ProductPicSmall":"/upfile/201806/636639044556861599.png","ProductPicList":"/upfile/201806/636639044592856134.png","ClassName":"粉尘防爆","ClassAtrribute":"型号;重量;颜色","CompanyName":"重庆云赛环保科技有限公司"}]
     * DataCount : 4
     * Message :
     * Code : null
     */

    private PropertiesBean Properties;
    private boolean IsSuccess;
    private int DataCount;
    private String Message;
    private Object Code;
    private List<Produce> Data;

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

    public List<Produce> getData() {
        return Data;
    }

    public void setData(List<Produce> Data) {
        this.Data = Data;
    }

    public static class PropertiesBean {
    }

    public static class Produce implements Serializable {
        /**
         * ID : 6
         * CompanyID : 4
         * ProductName : 泄爆口
         * ProductDesc : [img]/upfile/201806/636639683536823591.png[/img]泄爆口也称为泄爆面积，指有可能产生爆炸的房间或者箱体为了避免爆炸而设的排泄压力或者能量的通道。[br]比如房间的窗口、门，压力设备也会专门设计一个泄爆口。
         * ProductPic : /upfile/201806/636639683536823591.png
         * ProductClass : 3
         * ProductAttribute : ;;;
         * AddTime : /Date(1528342760020)/
         * UpdateTime : /Date(1528342760020)/
         * AddUserID : 1
         * State : 1
         * ProductPrice : 0.0
         * ProductPicSmall : /upfile/201806/636639683053528790.png
         * ProductPicList : /upfile/201806/636639683385562113.png
         * ClassName : 粉尘防爆
         * ClassAtrribute : 型号;重量;颜色
         * CompanyName : 重庆云赛环保科技有限公司
         */

        private int ID;
        private int CompanyID;
        private String ProductName;
        private String ProductDesc;
        private String ProductPic;
        private int ProductClass;
        private String ProductAttribute;
        private String AddTime;
        private String UpdateTime;
        private int AddUserID;
        private int State;
        private double ProductPrice;
        private String ProductPicSmall;
        private String ProductPicList;
        private String ClassName;
        private String ClassAtrribute;
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

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getProductDesc() {
            return ProductDesc;
        }

        public void setProductDesc(String ProductDesc) {
            this.ProductDesc = ProductDesc;
        }

        public String getProductPic() {
            return ProductPic;
        }

        public void setProductPic(String ProductPic) {
            this.ProductPic = ProductPic;
        }

        public int getProductClass() {
            return ProductClass;
        }

        public void setProductClass(int ProductClass) {
            this.ProductClass = ProductClass;
        }

        public String getProductAttribute() {
            return ProductAttribute;
        }

        public void setProductAttribute(String ProductAttribute) {
            this.ProductAttribute = ProductAttribute;
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

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public double getProductPrice() {
            return ProductPrice;
        }

        public void setProductPrice(double ProductPrice) {
            this.ProductPrice = ProductPrice;
        }

        public String getProductPicSmall() {
            return ProductPicSmall;
        }

        public void setProductPicSmall(String ProductPicSmall) {
            this.ProductPicSmall = ProductPicSmall;
        }

        public String getProductPicList() {
            return ProductPicList;
        }

        public void setProductPicList(String ProductPicList) {
            this.ProductPicList = ProductPicList;
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

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }
    }
}
