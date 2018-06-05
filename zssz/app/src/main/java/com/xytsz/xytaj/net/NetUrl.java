package com.xytsz.xytaj.net;

/**
 * Created by admin on 2017/2/13.
 *
 * 网络层
 */
public class NetUrl {


    public static final String SERVERURL = "http://aj.xytgps.com/SZWEBSERVICE/aj.asmx";
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
    public static final String getdealtask = "GetListZG";
    //获取当前人员要处置的信息
    public static final String getManagementList = "GetMyTask";

    public static final String sendmethodName = "ToDispatching";

    public static final String sendbackmethodName = "ToDispatching";


    public static final String reviewmethodName = "GetRepeatTask";
    public static final String dealmethodName = "ToNextState";


    /*
    * 上传位置信息到服务器*/
    public static final String uploadLocationmethodName = "toUploadLocation";

    /*
    * 获取人员位置信息*/
    public static final String getpersonLocation = "getAllPersonLocation";

    /**
     * 我的界面的
     */
    public static final String getTaskCountOfReport ="GetTaskCountOfReport";
    public static final String getTaskCountOfDeal ="GetTaskCountOfDeal";
    public static final String getTaskCountOfReview ="GetCountEXByPersonID";


    /**
     * 获取所有病害列表 用于病害定位
     */
    public static final String getAllTaskMethodName = "GetAllTask";
    /**
     * 获取所有病害的图片Url
     */
    public static final String getAllImageURLmethodName = "GetImg";
    /**
     * 获取报验阶段的三类图片
     */
    public static final String getPostImageURLmethodName = "getPostImgUrl";
    public static final String getPreImageURLmethodName = "getPreImgUrl";
    public static final String getRngImageURLmethodName = "getRngImgUrl";
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
     * 获取自己上报处置的方法
     */
    public static final String getALlReportByPersonID = "getALlReportByPersonID";
    public static final String getALlDealByPersonID = "getAllDealByPersonID";
    /**
     * 上传头像
     */
    public static final String uploadHeadImg = "uploadHeadImg";

    /*
    * 方法*/
    public static final  String photo_SOAP_ACTION = "http://AJ.org/toReportImg";

    public static final  String getTasklist_SOAP_ACTION = "http://AJ.org/GetDeviceCheckInfoByState";
    public static final  String toExamine_SOAP_ACTION = "http://AJ.org/ToExamine";
    public static final  String toDispatching_SOAP_ACTION = "http://AJ.org/ToDispatching";
    public static final  String toManagement_SOAP_ACTION = "http://AJ.org/ToManagement";
    public static final  String toInspection_SOAP_ACTION = "http://AJ.org/ToInspection";
    public static final  String getPersonlocation_SOAP_ACTION = "http://AJ.org/getAllPersonLocation";
    public static final  String toUploadlocation_SOAP_ACTION = "http://AJ.org/toUploadLocation";


    public static final String getManagementList_SOAP_ACTION = "http://AJ.org/GetMyTask";

    public static final String getTaskCountOfReport_SOAP_ACTION = "http://AJ.org/GetTaskCountOfReport";
    public static final String getTaskCountOfDeal_SOAP_ACTION = "http://AJ.org/GetTaskCountOfDeal";
    public static final String getTaskCountOfReview_SOAP_ACTION = "http://AJ.org/GetTaskCountOfReview";

    public static final String getAllImageURL_SOAP_ACTION = "http://AJ.org/GetImg";

    public static final String getAllTask_SOAP_ACTION = "http://AJ.org/GetAllTask";

    public static final String getPostImageURL_SOAP_ACTION ="http://AJ.org/getPostImgUrl" ;

    public static final String getPreImageURLSoap_Action = "http://AJ.org/getPreImgUrl";
    public static final String getRngImageURLSoap_Action = "http://AJ.org/getRngImgUrl";


    public static final String getPersonList_SOAP_ACTION ="http://AJ.org/GetPersonByDept";

    public static final String getVersionInfo_SOAP_ACTION = "http://AJ.org/getVersionInfo";
    public static final String dealtype_SOAP_ACTION = "http://AJ.org/getAllSZDealType";

    public static final String getALlReportByPersonID_SOAP_ACTION = "http://AJ.org/getALlReportByPersonID";
    public static final String getALlDealByPersonID_SOAP_ACTION = "http://AJ.org/getAllDealByPersonID";

    public static final String uploadHeadImg_SOAP_ACTION = "http://AJ.org/uploadHeadImg";
    //修改
    public static final String sendBack_SOAP_ACTION = "http://AJ.org/uploadHeadImg";


    public static final String getALlReviewByPersonID = "getAllExamineByPersonID";
    public static final String getALlReviewByPersonID_SOAP_ACTION = "http://AJ.org/getAllExamineByPersonID";
    public static final String getenlist = "GetEnList";
    public static final String getenlist_SOAP_ACTION = "http://AJ.org/GetEnList";
    public static final String citypersonreportemethodName = "ToSoReporting";
    public static final String getAllUserCountMethodName = "GetALLUserCount";
    public static final String getAllUserCount_SOAP_ACITION = "http://AJ.org/GetALLUserCount";
    public static final String getszUserCountInfomethod = "GetSZDisCountInfo";
    public static final String getszUserCountInfo_SOAP_ACTION = "http://AJ.org/GetSZDisCountInfo";
    public static final String getSZPeoplemethodName = "GetSZPeople";
    public static final String getSZPeople_SOAP_ACTION = "http://AJ.org/GetSZPeople";
    public static final String isSignMethodname = "ToSign";
    public static final String isSign_SOAP_ACITON = "http://AJ.org/ToSign";
    public static final String updatePesronmethodName ="ToUpdateSZPeople";
    public static final String updatePesron_SOAP_ACTION ="http://AJ.org/ToUpdateSZPeople";

    public static final String getHistoryMethodname = "GetSoReport";
    public static final String getHistory_SOAP_ACTION = "http://AJ.org/GetSoReport";
    public static final String getScoreDetailMethodname ="GetIntegralInfo";
    public static final String getScoreDetail_SOAP_ACTION ="http://AJ.org/GetIntegralInfo";
    public static final String getScoreMethodname ="GetIntegral";
    public static final String getScore_SOAP_ACTION ="http://AJ.org/GetIntegral";
    public static final String audiomethodName = "ToUploadAudio";

    public static final String getAudioMethodName ="GetAudio";
    public static final String getVideoMethodName ="GetMp4ByTNum";
    public static final String getAudio_SOAP_ACTION ="http://AJ.org/GetAudio";
    public static final String appraisemethodName = "ToFeedBack";
    public static final String appraise_SOAP_ACTION = "http://AJ.org/ToFeedBack";
    public static final String videomethodName ="toReportMp4";
    public static final String modificaitonmethodName = "toChangePwd";
    public static final String getTaskCountOfSend ="GetTaskCountOfIssued";
    public static final String replacePerson ="ToChangeRe_Person_ID";
    public static final String getAllSendByPersonID = "GetAllIssuedByPersonID";
    public static final String getReportDataMethodname = "GetCheckTermByDeviceId";
    public static final String getReport_SOAP_ACTION = "http://AJ.org/GetCheckTermByDeviceId";
    public static final String getSignpersonMethodname ="";
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
    public static final String EndSignmethod ="EndSign";
    public static final String trainTestdetailmethod = "";
    public static final String testcollectmethod ="GetGrades";
    public static final String TrainphotomethodName ="ToUploadTrainImg";
    public static final String gettestmethod = "GetExamByTrainId";
    public static final String uploadtestmethod ="ToAddExam";
    public static final String getnoPatrolTaskByPersonID ="GetTodayNotCheck";


    public static final String MeetingPhotoMethodName= "ToUploadImgOfMeeting";
    public static final String getSystemmanagelist ="GetSystemManagement";
    public static final String getcontingencylist ="GetEmergencyPlan";
    public static final String getExerciselist = "GetExercise";
    public static final String getsupMeetingList ="GetMeetingByPersonId";
    public static final String getnoPersonList = "GetPersonNameByNotSign";
    public static final String getALLPersonList = "GetALLPerson";
    public static final String uploadMeeting ="ToSignOfMeeting";
    public static final String getScrollerData = "";
    public static final String getCompanyData ="";
    public static final String getthirdData ="";
    public static final String getCompanyDetailData ="";
    public static final String getConpanyList ="";
    public static final String getnoCheckTaskByPersonID ="";
}
