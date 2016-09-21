package com.zncm.js.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.js.utils.XUtil;

import java.io.Serializable;

public class RemindData extends BaseData implements Serializable {

    public static final int STATUS_FOCUS = 5;

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String content;
    @DatabaseField
    private int type;//类型 0-一次提醒 1-重复提醒【不存在过期概念】
    @DatabaseField
    private int status;//状态 1-正常 3-删除 4-过期
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private int tk_id;//日记本 id  5 计划
    @DatabaseField
    private Long remind_time;//提醒时间

    public RemindData() {
    }

    public RemindData(String content, int status, int tk_id, Long remind_time) {
        this.content = content;
        this.status = status;
        this.tk_id = tk_id;
        this.remind_time = remind_time;
        this.time = XUtil.getLongTime();
        this.modify_time = XUtil.getLongTime();
    }

    public RemindData(String content, int status, int tk_id, Long remind_time, int type) {
        this.content = content;
        this.status = status;
        this.tk_id = tk_id;
        this.remind_time = remind_time;
        this.time = XUtil.getLongTime();
        this.modify_time = null;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getTk_id() {
        return tk_id;
    }

    public void setTk_id(int tk_id) {
        this.tk_id = tk_id;
    }

    public Long getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(Long remind_time) {
        this.remind_time = remind_time;
    }
}
