package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/3/27.
 *
 */
public class ProblemLocation {


    private List<ProblemListBean> problemList;

    public List<ProblemListBean> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<ProblemListBean> problemList) {
        this.problemList = problemList;
    }

    public static class ProblemListBean implements Serializable {
        /**
         * tasknumber : 20170330171525
         * di_id : 1
         * phaseindication : 3
         * uploadtime : 2017/3/30 17:15:33
         * reviewedTime :
         * sendedTime :
         * dealedTime :
         * postedTime :
         * checkedTime : 2017/3/31 11:10:25
         * upload_Person_name :
         * reviewed_Person_Name :
         * dealed_Person_name :
         * checked_Person_Name :
         * longitude : 116.355957
         * latitude : 39.768515
         */

        private String tasknumber;
        private int di_id;
        private int phaseindication;
        private String uploadtime;
        private String reviewedTime;
        private String sendedTime;
        private String dealedTime;
        private String postedTime;
        private String checkedTime;
        private String upload_Person_name;
        private String reviewed_Person_Name;
        private String dealed_Person_name;
        private String checked_Person_Name;
        private double longitude;
        private double latitude;

        public String getTasknumber() {
            return tasknumber;
        }

        public void setTasknumber(String tasknumber) {
            this.tasknumber = tasknumber;
        }

        public int getDi_id() {
            return di_id;
        }

        public void setDi_id(int di_id) {
            this.di_id = di_id;
        }

        public int getPhaseindication() {
            return phaseindication;
        }

        public void setPhaseindication(int phaseindication) {
            this.phaseindication = phaseindication;
        }

        public String getUploadtime() {
            return uploadtime;
        }

        public void setUploadtime(String uploadtime) {
            this.uploadtime = uploadtime;
        }

        public String getReviewedTime() {
            return reviewedTime;
        }

        public void setReviewedTime(String reviewedTime) {
            this.reviewedTime = reviewedTime;
        }

        public String getSendedTime() {
            return sendedTime;
        }

        public void setSendedTime(String sendedTime) {
            this.sendedTime = sendedTime;
        }

        public String getDealedTime() {
            return dealedTime;
        }

        public void setDealedTime(String dealedTime) {
            this.dealedTime = dealedTime;
        }

        public String getPostedTime() {
            return postedTime;
        }

        public void setPostedTime(String postedTime) {
            this.postedTime = postedTime;
        }

        public String getCheckedTime() {
            return checkedTime;
        }

        public void setCheckedTime(String checkedTime) {
            this.checkedTime = checkedTime;
        }

        public String getUpload_Person_name() {
            return upload_Person_name;
        }

        public void setUpload_Person_name(String upload_Person_name) {
            this.upload_Person_name = upload_Person_name;
        }

        public String getReviewed_Person_Name() {
            return reviewed_Person_Name;
        }

        public void setReviewed_Person_Name(String reviewed_Person_Name) {
            this.reviewed_Person_Name = reviewed_Person_Name;
        }

        public String getDealed_Person_name() {
            return dealed_Person_name;
        }

        public void setDealed_Person_name(String dealed_Person_name) {
            this.dealed_Person_name = dealed_Person_name;
        }

        public String getChecked_Person_Name() {
            return checked_Person_Name;
        }

        public void setChecked_Person_Name(String checked_Person_Name) {
            this.checked_Person_Name = checked_Person_Name;
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
    }
}
