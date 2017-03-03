package com.zncm.mxgtd.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxgtd.utils.XUtil;

import java.io.Serializable;

public class ProgressData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String content;
    @DatabaseField
    private int type;//类型 1-sys 2-人
    @DatabaseField
    private int business_type;//业务类型 1-笔记本组 2-笔记本 3-笔记本回复 4-清单
    @DatabaseField
    private int status;//状态 1-正常 3-删除
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private int pj_id;//笔记本组 id
    @DatabaseField
    private int tk_id;//笔记本 id
    @DatabaseField
    private int action;//行为 1 add 2 del 3 update 4 finish 5 重启


    public ProgressData() {
    }


    public ProgressData(String content, int type, int business_type, int status, int pj_id, int tk_id, int action) {
        this.content = content;
        this.type = type;
        this.business_type = business_type;
        this.status = status;
        this.time = XUtil.getLongTime();
        this.modify_time =  XUtil.getLongTime();
        this.pj_id = pj_id;
        this.tk_id = tk_id;
        this.action = action;
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

    public int getPj_id() {
        return pj_id;
    }

    public void setPj_id(int pj_id) {
        this.pj_id = pj_id;
    }

    public int getTk_id() {
        return tk_id;
    }

    public void setTk_id(int tk_id) {
        this.tk_id = tk_id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
