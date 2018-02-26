package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/4/1.
 *
 */
public class PersonLocation implements Serializable {

    private List<PersonListBean> personList;

    public List<PersonListBean> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonListBean> personList) {
        this.personList = personList;
    }

    public static class PersonListBean {
        /**
         * ID : 4
         * userName : 张三
         * longitude : 116.24
         * latitude : 39.55
         * time : 2017/3/27 16:02:19
         */

        private int ID;
        private String userName;
        private double longitude;
        private double latitude;
        private String time;



        private String headurl;
        //职称：正式工
        private String position;
        //职位
        private String Title;

        public String getHeadurl() {
            return headurl;
        }

        public void setHeadurl(String headurl) {
            this.headurl = headurl;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }
    }
}
