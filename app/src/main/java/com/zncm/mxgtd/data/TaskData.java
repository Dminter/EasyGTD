package com.zncm.mxgtd.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxgtd.utils.XUtil;

import java.io.Serializable;

public class TaskData extends BaseData implements Serializable {

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
    private int level;//优先级 1 无 2 低 3中 4高
    @DatabaseField
    private int pj_id;//笔记本组 id
    @DatabaseField
    private Long remind_time;//提醒时间
    @DatabaseField
    private String tag;


    public TaskData() {
    }


    public TaskData(String title) {
        this.title = title;
        this.id = -1;
    }

    public TaskData(String title, String describe, Long start_time, Long stop_time, int level, int pj_id, Long remind_time, String tag) {
        this.title = title;
        this.describe = describe;
        this.start_time = start_time;
        this.stop_time = stop_time;
        this.time = XUtil.getLongTime();
        this.modify_time = XUtil.getLongTime();
        this.level = level;
        this.pj_id = pj_id;
        this.remind_time = remind_time;
        this.tag = tag;
        this.status = EnumData.StatusEnum.ON.getValue();
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPj_id() {
        return pj_id;
    }

    public void setPj_id(int pj_id) {
        this.pj_id = pj_id;
    }

    public Long getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(Long remind_time) {
        this.remind_time = remind_time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
