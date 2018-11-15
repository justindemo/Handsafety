package com.xytsz.xytaj.bean;

/**
 * Created by admin on 2018/7/3.
 * </p>
 */
public class ExamResult {


    /**
     * data : 80
     * msg : 及格，您已参加过考试，本次不记录！
     * staus : -1
     */

    private int data;
    private String msg;
    private int staus;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStaus() {
        return staus;
    }

    public void setStaus(int staus) {
        this.staus = staus;
    }
}
