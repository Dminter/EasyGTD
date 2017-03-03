package com.zncm.mxgtd.data;

import java.io.Serializable;

public class DetailsData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    private int id;
    private Long time;
    private String content;
    private int business_type;//业务类型 1-笔记本组 2-笔记本 3-笔记本回复 4-清单
    private int status;//状态 1-正常 3-删除
    private int tk_id;//笔记本 id
    private Long remind_time;//提醒时间
    private int type;//类型 0-一次提醒 1-重复提醒【不存在过期概念】




    public DetailsData() {
    }


    public DetailsData(String content) {
        this.content = content;
    }

    public DetailsData(int id, Long time, String content, int business_type, int status, int tk_id, Long remind_time, int type) {
        this.id = id;
        this.time = time;
        this.content = content;
        this.business_type = business_type;
        this.status = status;
        this.tk_id = tk_id;
        this.remind_time = remind_time;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
