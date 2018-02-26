package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/27.
 *
 * 关于我自己上报和处置的单子
 *
 */
public class ForMyDis implements Serializable{


    /**
     * DI_ID : 1
     * Level : 0
     * TaskNumber : 20170515112743
     * DisposalLevel_ID : 1
     * DealType_Name : 道路巡察
     * FacilityType_Name : 步道
     * DiseaseType_Name : 沉陷
     * FacilityName_Name : 高粱红花岗岩砖
     * FacilitySpecifications_Name : 200*400*60
     * StreetAddress_Name : 道路1
     * AddressDescription :
     * DiseaseDescription :
     * Channel : 0
     * PhaseIndication : 2
     * UploadTime : 2017/5/15 11:27:18
     * Upload_Person_ID : 1
     * reviewed_Person_ID : 1
     * reviewedTime : 2017/5/15 18:09:26
     * Issued_Person_ID : 1
     * IssuedTime : 2017/5/15 18:09:59
     * RequirementsComplete_Person_ID : 1
     * RequirementsCompleteTime : 2017/5/16 18:10:00
     * postedTime : null
     * DealedTime : 2017/5/16 17:16:35
     * ActualCompletion_Person_ID : 0
     * ActualCompletionTime : null
     * ActualCompletionInfo :
     * Checked_Person_ID : null
     * CheckedTime : null
     * Department_ID : 1
     * Longitude : 116.355997
     * Latitude : 39.768725
     * node : null
     */

    private int DI_ID;
    private int Level;
    private String TaskNumber;
    private int DisposalLevel_ID;
    private String DealType_Name;
    private String FacilityType_Name;
    private String DiseaseType_Name;
    private String FacilityName_Name;
    private String FacilitySpecifications_Name;
    private String StreetAddress_Name;
    private String AddressDescription;
    private String DiseaseDescription;
    private int Channel;
    private int PhaseIndication;
    private String UploadTime;
    private int Upload_Person_ID;
    private int reviewed_Person_ID;
    private String reviewedTime;
    private int Issued_Person_ID;
    private String IssuedTime;
    private int RequirementsComplete_Person_ID;
    private String RequirementsCompleteTime;
    private Object postedTime;
    private String DealedTime;
    private int ActualCompletion_Person_ID;
    private Object ActualCompletionTime;
    private String ActualCompletionInfo;
    private Object Checked_Person_ID;
    private Object CheckedTime;
    private int Department_ID;
    private String Longitude;
    private String Latitude;
    private Object node;

    public int getDI_ID() {
        return DI_ID;
    }

    public void setDI_ID(int DI_ID) {
        this.DI_ID = DI_ID;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public String getTaskNumber() {
        return TaskNumber;
    }

    public void setTaskNumber(String TaskNumber) {
        this.TaskNumber = TaskNumber;
    }

    public int getDisposalLevel_ID() {
        return DisposalLevel_ID;
    }

    public void setDisposalLevel_ID(int DisposalLevel_ID) {
        this.DisposalLevel_ID = DisposalLevel_ID;
    }

    public String getDealType_Name() {
        return DealType_Name;
    }

    public void setDealType_Name(String DealType_Name) {
        this.DealType_Name = DealType_Name;
    }

    public String getFacilityType_Name() {
        return FacilityType_Name;
    }

    public void setFacilityType_Name(String FacilityType_Name) {
        this.FacilityType_Name = FacilityType_Name;
    }

    public String getDiseaseType_Name() {
        return DiseaseType_Name;
    }

    public void setDiseaseType_Name(String DiseaseType_Name) {
        this.DiseaseType_Name = DiseaseType_Name;
    }

    public String getFacilityName_Name() {
        return FacilityName_Name;
    }

    public void setFacilityName_Name(String FacilityName_Name) {
        this.FacilityName_Name = FacilityName_Name;
    }

    public String getFacilitySpecifications_Name() {
        return FacilitySpecifications_Name;
    }

    public void setFacilitySpecifications_Name(String FacilitySpecifications_Name) {
        this.FacilitySpecifications_Name = FacilitySpecifications_Name;
    }

    public String getStreetAddress_Name() {
        return StreetAddress_Name;
    }

    public void setStreetAddress_Name(String StreetAddress_Name) {
        this.StreetAddress_Name = StreetAddress_Name;
    }

    public String getAddressDescription() {
        return AddressDescription;
    }

    public void setAddressDescription(String AddressDescription) {
        this.AddressDescription = AddressDescription;
    }

    public String getDiseaseDescription() {
        return DiseaseDescription;
    }

    public void setDiseaseDescription(String DiseaseDescription) {
        this.DiseaseDescription = DiseaseDescription;
    }

    public int getChannel() {
        return Channel;
    }

    public void setChannel(int Channel) {
        this.Channel = Channel;
    }

    public int getPhaseIndication() {
        return PhaseIndication;
    }

    public void setPhaseIndication(int PhaseIndication) {
        this.PhaseIndication = PhaseIndication;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String UploadTime) {
        this.UploadTime = UploadTime;
    }

    public int getUpload_Person_ID() {
        return Upload_Person_ID;
    }

    public void setUpload_Person_ID(int Upload_Person_ID) {
        this.Upload_Person_ID = Upload_Person_ID;
    }

    public int getReviewed_Person_ID() {
        return reviewed_Person_ID;
    }

    public void setReviewed_Person_ID(int reviewed_Person_ID) {
        this.reviewed_Person_ID = reviewed_Person_ID;
    }

    public String getReviewedTime() {
        return reviewedTime;
    }

    public void setReviewedTime(String reviewedTime) {
        this.reviewedTime = reviewedTime;
    }

    public int getIssued_Person_ID() {
        return Issued_Person_ID;
    }

    public void setIssued_Person_ID(int Issued_Person_ID) {
        this.Issued_Person_ID = Issued_Person_ID;
    }

    public String getIssuedTime() {
        return IssuedTime;
    }

    public void setIssuedTime(String IssuedTime) {
        this.IssuedTime = IssuedTime;
    }

    public int getRequirementsComplete_Person_ID() {
        return RequirementsComplete_Person_ID;
    }

    public void setRequirementsComplete_Person_ID(int RequirementsComplete_Person_ID) {
        this.RequirementsComplete_Person_ID = RequirementsComplete_Person_ID;
    }

    public String getRequirementsCompleteTime() {
        return RequirementsCompleteTime;
    }

    public void setRequirementsCompleteTime(String RequirementsCompleteTime) {
        this.RequirementsCompleteTime = RequirementsCompleteTime;
    }

    public Object getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(Object postedTime) {
        this.postedTime = postedTime;
    }

    public String getDealedTime() {
        return DealedTime;
    }

    public void setDealedTime(String DealedTime) {
        this.DealedTime = DealedTime;
    }

    public int getActualCompletion_Person_ID() {
        return ActualCompletion_Person_ID;
    }

    public void setActualCompletion_Person_ID(int ActualCompletion_Person_ID) {
        this.ActualCompletion_Person_ID = ActualCompletion_Person_ID;
    }

    public String getActualCompletionTime() {
        return ActualCompletionTime +"";
    }

    public void setActualCompletionTime(Object ActualCompletionTime) {
        this.ActualCompletionTime = ActualCompletionTime;
    }

    public String getActualCompletionInfo() {
        return ActualCompletionInfo;
    }

    public void setActualCompletionInfo(String ActualCompletionInfo) {
        this.ActualCompletionInfo = ActualCompletionInfo;
    }

    public Object getChecked_Person_ID() {
        return Checked_Person_ID;
    }

    public void setChecked_Person_ID(Object Checked_Person_ID) {
        this.Checked_Person_ID = Checked_Person_ID;
    }

    public Object getCheckedTime() {
        return CheckedTime;
    }

    public void setCheckedTime(Object CheckedTime) {
        this.CheckedTime = CheckedTime;
    }

    public int getDepartment_ID() {
        return Department_ID;
    }

    public void setDepartment_ID(int Department_ID) {
        this.Department_ID = Department_ID;
    }

    public double getLongitude() {
        return Double.parseDouble(Longitude);
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public double getLatitude() {
        return Double.parseDouble(Latitude);
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public Object getNode() {
        return node;
    }

    public void setNode(Object node) {
        this.node = node;
    }
}
