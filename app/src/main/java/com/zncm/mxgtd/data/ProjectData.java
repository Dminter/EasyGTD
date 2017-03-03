package com.zncm.mxgtd.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxgtd.utils.XUtil;

import java.io.Serializable;

public class ProjectData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String describe;
    @DatabaseField
    private Long start_time;
    @DatabaseField
    private Long stop_time;
    @DatabaseField
    private int status;//状态 1-进行中 2-完成 3-删除
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private int undone_task;//未完成的笔记本组数
    @DatabaseField
    private String tag;


    public ProjectData() {
    }


    public ProjectData(String title, String describe, Long start_time, Long stop_time) {
        this.title = title;
        this.describe = describe;
        this.start_time = start_time;
        this.stop_time = stop_time;
        this.status = EnumData.StatusEnum.ON.getValue();
        this.time = XUtil.getLongTime();
        this.modify_time = XUtil.getLongTime();
        this.undone_task = 0;
    }


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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getStop_time() {
        return stop_time;
    }

    public void setStop_time(Long stop_time) {
        this.stop_time = stop_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getModify_time() {
        return modify_time;
    }

    public void setModify_time(Long modify_time) {
        this.modify_time = modify_time;
    }

    public int getUndone_task() {
        return undone_task;
    }

    public void setUndone_task(int undone_task) {
        this.undone_task = undone_task;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
