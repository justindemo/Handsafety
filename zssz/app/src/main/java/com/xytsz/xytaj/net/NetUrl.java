package com.xytsz.xytaj.net;

/**
 * Created by admin on 2017/2/13.
 *
 * 网络层
 */
public class NetUrl {

    public static final String SERVERURL = "http://test03.xytgps.com/SZWEBSERVICE/aj.asmx";
//    public static final String SERVERURL = "http://otaj.xytgps.com/SZWEBSERVICE/aj.asmx";
//    public static final String SERVERURL = "http://192.168.1.179:10801";
    public static final String AllURL = "http://ajgyl.xytgps.com";
    public static final String SERVERURL2 = "http://ajgyl.xytgps.com/Api/SupplyChain/";
    /**
     *命名空间
     */
    public static final String nameSpace = "http://AJ.org/";
    /**
     * 方法名
     */

    public static final String loginmethodName = "ToLogin";
    public static final String photomethodName = "ToUploadImg";
    public static final String reportmethodName = "ToCheck";
    public static final String randomreportmethodName = "ToCheckIsNotDevice";
    public static final String getdealtask = "GetListZG";
    //获取当前人员要处置的信息
    public static final String getManagementList = "GetMyTask";

    public static final String sendmethodName = "ToDispatching";



    public static final String reviewmethodName = "GetRepeatTask";
    public static final String dealmethodName = "ToNextState";


    /*
    * 上传位置信息到服务器*/
    public static final String uploadLocationmethodName = "toUploadLocation";



    /**
     * 获取所有病害的图片Url
     */
    public static final String getAllImageURLmethodName = "GetImg";

    /**
     * 获取当前版本
     */
    public static final String getVersionInfoMethodName = "getVersionInfo";
    /**
     * 获取人员列表的方法
     */
    public static final String getPersonList ="GetPersonByDept";
    public static final String getallPersonList ="getPersonByYH";


    /**
     * 上传头像
     */
    public static final String uploadHeadImg = "uploadHeadImg";

    /*
    * 方法*/

    public static final  String getTasklist_SOAP_ACTION = "http://AJ.org/GetDeviceCheckInfoByState";

    public static final  String toDispatching_SOAP_ACTION = "http://AJ.org/ToDispatching";

    public static final  String toInspection_SOAP_ACTION = "http://AJ.org/ToInspection";
    public static final  String toUploadlocation_SOAP_ACTION = "http://AJ.org/toUploadLocation";


    public static final String getManagementList_SOAP_ACTION = "http://AJ.org/GetMyTask";


    public static final String getAllImageURL_SOAP_ACTION = "http://AJ.org/GetImg";


    public static final String getPreImageURLSoap_Action = "http://AJ.org/getPreImgUrl";
    public static final String getRngImageURLSoap_Action = "http://AJ.org/getRngImgUrl";


    public static final String getPersonList_SOAP_ACTION ="http://AJ.org/GetPersonByDept";

    public static final String getVersionInfo_SOAP_ACTION = "http://AJ.org/getVersionInfo";

    public static final String uploadHeadImg_SOAP_ACTION = "http://AJ.org/uploadHeadImg";
    //修改
    public static final String getAllUserCountMethodName = "GetALLUserCount";
    public static final String getAllUserCount_SOAP_ACITION = "http://AJ.org/GetALLUserCount";
    public static final String getSZPeoplemethodName = "GetSZPeople";
    public static final String getSZPeople_SOAP_ACTION = "http://AJ.org/GetSZPeople";

    public static final String updatePesronmethodName ="ToUpdateSZPeople";
    public static final String updatePesron_SOAP_ACTION ="http://AJ.org/ToUpdateSZPeople";


    public static final String audiomethodName = "ToUploadAudio";

    public static final String getAudioMethodName ="GetAudio";

    public static final String getAudio_SOAP_ACTION ="http://AJ.org/GetAudio";
    public static final String appraisemethodName = "ToFeedBack";
    public static final String appraise_SOAP_ACTION = "http://AJ.org/ToFeedBack";
    public static final String videomethodName ="toReportMp4";
    public static final String modificaitonmethodName = "toChangePwd";
    public static final String getReportDataMethodname = "GetCheckTermByDeviceId";
    public static final String getReport_SOAP_ACTION = "http://AJ.org/GetCheckTermByDeviceId";

    public static final String MoringSignmethod ="ToSign";
    public static final String TrainSignmethod ="ToTrainSign";
    public static final String getTaskByPersonID = "GetMyTaskToCheck";
    public static final String REVIEWSTATEMETHOD = "ToRepeatTask";

    public static final String CHECKMETHODNAME = "GetYSTask";
    public static final String getMytaskmethod = "GetCountMyCheck";
    public static final String getRepeatmethod ="GetCountRepeat";
    public static final String getDealcountMethod = "GetCountZG";
    public static final String getreviewcountmethod ="GetCountDispatching";
    public static final String getpostcountmethod ="GetCountMyTask";
    public static final String getcheckcountmethod ="GetCountYSTask";
    public static final String getsendtask = "GetListDispatching";
    public static final String getImageCode = "GetAPPQRCode";
    public static final String trainTestshowmethod = "GetALLTrain";

    public static final String REVIEWSTATEBACKMETHOD = "ToRepeatTaskBack";
    public static final String getMoringMeeting = "GetMorningMeeting";
    public static final String getSignCheck ="GetSignCheckDic";

    public static final String testcollectmethod ="GetGrades";
    public static final String TrainphotomethodName ="ToUploadTrainImg";
    public static final String gettestmethod = "GetExamByTrainId";
    public static final String uploadtestmethod ="SubmitExam";
    public static final String getnoPatrolTaskByPersonID ="GetTodayNotCheck";

    public static final String MeetingPhotoMethodName= "ToUploadImgOfMeeting";
    public static final String getSystemmanagelist ="GetSystemManagement";
    public static final String getcontingencylist ="GetEmergencyPlan";
    public static final String getExerciselist = "GetExercise";
    public static final String getsupMeetingList ="GetMeetingByPersonId";
    public static final String getnoPersonList = "GetPersonNameByNotSign";
    public static final String getALLPersonList = "GetALLPerson";
    public static final String uploadMeeting ="ToSignOfMeeting";
    public static final String getScrollerData = "GetAJAdCompanys";
    public static final String getCompanyData ="GetCompany";
    public static final String getCompanyDetailData ="SelectCompany/";
    public static final String getnoCheckTaskByPersonID ="GetTodayNotReview";
    public static final String getProduceList ="GetProduct";

    public static final String getCaseList = "GetCompanyCase";
    public static final String getCategoryList ="GetProductClass";


    public static final String getFacilityCategroyData ="GetProductCompany";
    public static final String isMeetingSign ="CheckMeetSign";
    public static final String tag ="?AJPersonID=";
    public static final String UesrExit ="ToLogout";
    public static final String  getStandard ="GetSafetyUrl";


    public static final String getNoPatrolCount = "GetTodayNotCheckCount";
    public static final String getNoCheckCount= "GetTodayNotReviewCount";


    public static final String getMeetingCount ="GetMeetingByPersonIdCount";
    public static final String getTraintestCount = "GetALLTrainCount";
}
