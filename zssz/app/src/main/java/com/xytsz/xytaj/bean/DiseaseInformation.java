package com.xytsz.xytaj.bean;

/**
 *
 */

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * 任务单号          TaskNumber  区分任务的标示
 * 移动设备编号      MobileDeviceNumber
 * 处置等级编号      DisposalLevel_ID
 * 设施类型编号      FacilityType_ID
 * 病害类型编号      DiseaseType_ID
 * 设施名称编号      FacilityName_ID
 * 设施规格编号      FacilitySpecifications_ID
 * 街道地址编号      StreetAddress_ID
 * 地址描述          AddressDescription
 * 病害描述          DiseaseDescription
 * 上报渠道编号      Channel   1为 手持上报  2 为网络上报 3 电话上报 4 微信上报     手机默认为手持上报
 * 任务阶段标示      PhaseIndication                      主要标示任务的处理阶段 上报完成 派工完成 处理完成
 * 上报时间          UploadTime
 * 任务上报人编号    Upload_Person_ID
 * <p/>
 * 任务下达人编号    Issued_Person_ID
 * 任务下达时间      IssuedTime
 * <p/>
 * 要求完成人        RequirementsComplete_Person_ID
 * 要求完成时间      RequirementsCompleteTime
 * <p/>
 * 实际完成人        ActualCompletion_Person_ID
 * 实际完成时间      ActualCompletionTime
 * <p/>
 * 实际完成情况      ActualCompletionInfo
 * <p/>
 * 上报时候的
 * 经度             Longitude
 * 维度             Latitude
 */
public class DiseaseInformation implements KvmSerializable {

    public int id;
    public int level;
    public String diviceNum;
    public String problemID;
    public String diseaseDescription;
    public int channel;
    public String fileName;
    public int phaseIndication;
    public String uploadTime;
    public int upload_Person_ID;
    public int issued_Person_ID;
    public String issuedTime;
    public int requirementsComplete_Person_ID;
    public String requirementsCompleteTime;
    public int actualCompletion_Person_ID;
    public String actualCompletionTime;
    public String actualCompletionInfo;
    public String longitude;
    public String latitude;
    public int department_ID;
    public String photoName;
    public String encode;
    public int dealtype_ID;
    public String locationDesc;
    public int problemTag;
    public String problem;
    public String taskNumber;

    public String encodeBase64File;
    public String audioName;
    public String videoName;
    public String audioTime;

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }
}
