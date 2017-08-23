package com.sky.model.menu.clock;

import java.io.Serializable;

/**
 * Created by Administrator on 17-8-11.
 */

public class Clock implements Serializable {
    private int id;
    //闹钟标题
    private String title;
    //闹钟时间
    private String time;
    //小睡
    private String sleep;
    //响铃周期
    private String cycle;
    //铃声设置
    private String bell;
    private String bellType;
    //日期
    private String date;

    private long triggerTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getBell() {
        return bell;
    }

    public void setBell(String bell) {
        this.bell = bell;
    }

    public String getBellType() {
        return bellType;
    }

    public void setBellType(String bellType) {
        this.bellType = bellType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(long triggerTime) {
        this.triggerTime = triggerTime;
    }
}
