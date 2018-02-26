package com.xytsz.xytaj.global;

/**
 * Created by admin on 2017/1/11.
 *
 * 基础设施
 */
public class Data {


    /**
     * 当前处置状态
     */
    public static final String[] phaseIndaciton = {"审核", "下派", "处置", "验收", "验收通过", "审核未通过"};
    /*
    * 处置等级*/
    public static String[] grades = new String[]{
            "一般病害", "应急抢险", "违章施工", "日常养护", "社会举报", "网格上报"};
    public static String[] departments = new String[]
            {
            "监控指挥中心","道路设施部门","桥梁设施部门","照明设施部门","交通设施部门","公共设施部门", "排水设施部门","河道设施部门" ,"城市环境部门"
            };

    //病害名称
    public static String[] pbname = new String[]{"一类病害", "二类病害", "三类病害"};

    //问题举报分类
    public static String[] reportSort = new String[]{"道路设施","交通设施","桥梁设施","照明设施",
            "公共设施", "排水设施","河道设施","城市环境"};






}
