package com.xytsz.xytaj.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/28.
 *
 *
 */
public class AudioUrl implements Serializable{

    private String audiourl;
    private String time;

    public String getAudioUrl() {
        return audiourl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audiourl = audioUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
