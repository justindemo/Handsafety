package com.xytsz.xytaj.ui;

/**
 * Created by admin on 2017/3/23.
 *
 */
public class TimeChoiceButtonManager {
    private  static  TimeChoiceButtonManager manager = new TimeChoiceButtonManager();
    private TimeChoiceButtonManager(){}
    public static TimeChoiceButtonManager getInstance(){
        return manager;
    }

    private TimeChoiceButton timeChoiceButton;

    public TimeChoiceButton getTimeChoiceButton() {
        return timeChoiceButton;
    }

    public void setTimeChoiceButton(TimeChoiceButton timeChoiceButton) {
        this.timeChoiceButton = timeChoiceButton;
    }
}
